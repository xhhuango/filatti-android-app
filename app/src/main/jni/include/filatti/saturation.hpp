#ifndef __FILATTI_SATURATION_HPP__
#define __FILATTI_SATURATION_HPP__

#include "adjustment.hpp"

namespace filatti {
    class Saturation : public Adjustment {
    private:
        double _saturation;
        cv::Mat _lut;

        void build_lut();

    public:
        const double SATURATION_NONE = 0;
        const double SATURATION_MIN = -1;
        const double SATURATION_MAX = 1;

        Saturation();

        ~Saturation();

        double get_saturation();

        bool set_saturation(double saturation);

        virtual bool apply(cv::Mat& src, cv::Mat& dst) override;
    };
};

#endif //__FILATTI_SATURATION_HPP__
