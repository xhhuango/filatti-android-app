#include <filatti/curves.hpp>

#include "interpolator/spline.hpp"
#include "interpolator/linear.hpp"

using namespace filatti;

Curves::Curves() {
    _value_points = std::make_pair(POINTS_NONE, POINTS_NONE);
    _blue_points = std::make_pair(POINTS_NONE, POINTS_NONE);
    _green_points = std::make_pair(POINTS_NONE, POINTS_NONE);
    _red_points = std::make_pair(POINTS_NONE, POINTS_NONE);
}

Curves::~Curves() {
}

inline bool Curves::check_input_points(const std::vector<uchar>& from, const std::vector<uchar>& to) {
    return !(!within((int) from.size(), POINTS_SIZE_MIN, POINTS_SIZE_MAX)
             || (from.size() != to.size())
             || from.front() != POINTS_NONE.front()
             || from.back() != POINTS_NONE.back());
}

std::vector<uchar> Curves::get_curves(const std::pair<std::vector<uchar>, std::vector<uchar>>& points) {
    Spline spline(std::vector<double>(points.first.begin(), points.first.end()),
                  std::vector<double>(points.second.begin(), points.second.end()));
    std::vector<uchar> curves(256);
    for (int i = 0; i < 256; ++i)
        curves[i] = cv::saturate_cast<uchar>(spline[i]);
    return curves;
}

bool Curves::set_value_points(std::vector<uchar> from, std::vector<uchar> to) {
    if (!check_input_points(from, to))
        return false;

    _value_points = std::make_pair(std::move(from), std::move(to));
    build_lut();
    return true;
}

std::pair<std::vector<uchar>, std::vector<uchar>> Curves::get_value_points() {
    return _value_points;
}

std::vector<uchar> Curves::get_value_curves() {
    return get_curves(_value_points);
}

bool Curves::set_blue_points(std::vector<uchar> from, std::vector<uchar> to) {
    if (!check_input_points(from, to))
        return false;

    _blue_points = std::make_pair(std::move(from), std::move(to));
    build_lut();
    return true;
}

std::pair<std::vector<uchar>, std::vector<uchar>> Curves::get_blue_points() {
    return _blue_points;
}

std::vector<uchar> Curves::get_blue_curves() {
    return get_curves(_blue_points);
}

bool Curves::set_green_points(std::vector<uchar> from, std::vector<uchar> to) {
    if (!check_input_points(from, to))
        return false;

    _green_points = std::make_pair(std::move(from), std::move(to));
    build_lut();
    return true;
}

std::pair<std::vector<uchar>, std::vector<uchar>> Curves::get_green_points() {
    return _green_points;
}

std::vector<uchar> Curves::get_green_curves() {
    return get_curves(_green_points);
}

bool Curves::set_red_points(std::vector<uchar> from, std::vector<uchar> to) {
    if (!check_input_points(from, to))
        return false;

    _red_points = std::make_pair(std::move(from), std::move(to));
    build_lut();
    return true;
}

std::pair<std::vector<uchar>, std::vector<uchar>> Curves::get_red_points() {
    return _red_points;
}

std::vector<uchar> Curves::get_red_curves() {
    return get_curves(_red_points);
}

bool Curves::apply(const cv::Mat& src, cv::Mat& dst) {
    if (!has_effect()) {
        return false;
    } else {
        cv::LUT(src, _lut, dst);
        return true;
    }
}

bool Curves::has_effect() {
    return !(_value_points.first == POINTS_NONE && _value_points.second == POINTS_NONE
             && _blue_points.first == POINTS_NONE && _blue_points.second == POINTS_NONE
             && _green_points.first == POINTS_NONE && _green_points.second == POINTS_NONE
             && _red_points.first == POINTS_NONE && _red_points.second == POINTS_NONE);
}

void Curves::build_lut() {
    if (!has_effect()) {
        if (!_lut.empty())
            _lut.release();
        return;
    }

    if (_lut.empty())
        _lut.create(256, 1, CV_8UC3);

    Interpolator* value_interpolator;
    if (_value_points.first.size() > 2)
        value_interpolator = new Spline(std::vector<double>(_value_points.first.begin(), _value_points.first.end()),
                                        std::vector<double>(_value_points.second.begin(), _value_points.second.end()));
    else
        value_interpolator = new Linear(std::vector<double>(_value_points.first.begin(), _value_points.first.end()),
                                        std::vector<double>(_value_points.second.begin(), _value_points.second.end()));

    Interpolator* blue_interpolator;
    if (_blue_points.first.size() > 2)
        blue_interpolator = new Spline(std::vector<double>(_blue_points.first.begin(), _blue_points.first.end()),
                                       std::vector<double>(_blue_points.second.begin(), _blue_points.second.end()));
    else
        blue_interpolator = new Linear(std::vector<double>(_blue_points.first.begin(), _blue_points.first.end()),
                                       std::vector<double>(_blue_points.second.begin(), _blue_points.second.end()));

    Interpolator* green_interpolator;
    if (_green_points.first.size() > 2)
        green_interpolator = new Spline(std::vector<double>(_green_points.first.begin(), _green_points.first.end()),
                                        std::vector<double>(_green_points.second.begin(), _green_points.second.end()));
    else
        green_interpolator = new Linear(std::vector<double>(_green_points.first.begin(), _green_points.first.end()),
                                        std::vector<double>(_green_points.second.begin(), _green_points.second.end()));

    Interpolator* red_interpolator;
    if (_red_points.first.size() > 2)
        red_interpolator = new Spline(std::vector<double>(_red_points.first.begin(), _red_points.first.end()),
                                      std::vector<double>(_red_points.second.begin(), _red_points.second.end()));
    else
        red_interpolator = new Linear(std::vector<double>(_red_points.first.begin(), _red_points.first.end()),
                                      std::vector<double>(_red_points.second.begin(), _red_points.second.end()));

    for (int i = 0; i < 256; ++i) {
        double value = (*value_interpolator)[i];
        _lut.at<cv::Vec3b>(i, 0) = cv::Vec3b{cv::saturate_cast<uchar>((*blue_interpolator)[value]),
                                             cv::saturate_cast<uchar>((*green_interpolator)[value]),
                                             cv::saturate_cast<uchar>((*red_interpolator)[value])};
    }

    delete value_interpolator;
    delete blue_interpolator;
    delete green_interpolator;
    delete red_interpolator;
}
