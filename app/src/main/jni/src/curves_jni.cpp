#include <jni.h>

#include "log.hpp"

#include <filatti/curves.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Curves* obj = new Curves();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Curves* obj = (Curves*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Curves*) thiz)->has_effect();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Curves* obj = (Curves*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

void get_curves(JNIEnv* env, jintArray arr, const std::vector<uchar>& curves)
{
    jint* curves_arr = env->GetIntArrayElements(arr, NULL);
    for (int i = 0, j = curves.size(); i < j; ++i)
        curves_arr[i] = (int) curves[i];
    env->ReleaseIntArrayElements(arr, curves_arr, 0);
}

jintArray get_points(JNIEnv* env, const std::pair<std::vector<uchar>, std::vector<uchar>>& points)
{
    jintArray arr = env->NewIntArray(points.first.size() * 2);
    jint* points_arr = env->GetIntArrayElements(arr, NULL);
    for (int i = 0, j = points.first.size(); i < j; ++i) {
        int n = i * 2;
        points_arr[n] = (int) points.first[i];
        points_arr[n + 1] = (int) points.second[i];
    }
    env->ReleaseIntArrayElements(arr, points_arr, 0);
    return arr;
}

void set_points(JNIEnv* env, jintArray arr, std::vector<uchar>& from, std::vector<uchar>& to)
{
    jint* points_arr = env->GetIntArrayElements(arr, NULL);
    jsize len = env->GetArrayLength(arr);
    for (int i = 0; i < len; i += 2) {
        from.push_back(cv::saturate_cast<uchar>(points_arr[i]));
        to.push_back(cv::saturate_cast<uchar>(points_arr[i + 1]));
    }
    env->ReleaseIntArrayElements(arr, points_arr, 0);
}

/**
 * Value
 */

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetValueCurves
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    get_curves(env, arr, ((Curves*) thiz)->get_value_curves());
}

JNIEXPORT jintArray JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetValuePoints
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return get_points(env, ((Curves*) thiz)->get_value_points());
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeSetValuePoints
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    std::vector<uchar> from;
    std::vector<uchar> to;
    set_points(env, arr, from, to);
    try {
        ((Curves*) thiz)->set_value_points(from, to);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

/**
 * Blue
 */

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetBlueCurves
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    get_curves(env, arr, ((Curves*) thiz)->get_blue_curves());
}

JNIEXPORT jintArray JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetBluePoints
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return get_points(env, ((Curves*) thiz)->get_blue_points());
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeSetBluePoints
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    std::vector<uchar> from;
    std::vector<uchar> to;
    set_points(env, arr, from, to);
    try {
        ((Curves*) thiz)->set_blue_points(from, to);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

/**
 * Green
 */

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetGreenCurves
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    get_curves(env, arr, ((Curves*) thiz)->get_green_curves());
}

JNIEXPORT jintArray JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetGreenPoints
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return get_points(env, ((Curves*) thiz)->get_green_points());
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeSetGreenPoints
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    std::vector<uchar> from;
    std::vector<uchar> to;
    set_points(env, arr, from, to);
    try {
        ((Curves*) thiz)->set_green_points(from, to);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

/**
 * Red
 */

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetRedCurves
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    get_curves(env, arr, ((Curves*) thiz)->get_red_curves());
}

JNIEXPORT jintArray JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeGetRedPoints
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return get_points(env, ((Curves*) thiz)->get_red_points());
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_CurvesAdjust_nativeSetRedPoints
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    std::vector<uchar> from;
    std::vector<uchar> to;
    set_points(env, arr, from, to);
    try {
        ((Curves*) thiz)->set_red_points(from, to);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

} // extern "C"
