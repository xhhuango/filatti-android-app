#include <jni.h>

#include "log.hpp"

#include <filatti/vignette.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Vignette* obj = new Vignette();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Vignette*) thiz)->has_effect();
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jfloatArray arr)
{
    Vignette* obj = (Vignette*) thiz;
    jfloat *center_arr = env->GetFloatArrayElements(arr, JNI_FALSE);
    cv::Point2f center = obj->get_center();
    center_arr[0] = center.x;
    center_arr[1] = center.y;
    env->ReleaseFloatArrayElements(arr, center_arr, 0);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jfloat x, jfloat y)
{
    try {
        ((Vignette*) thiz)->set_center(cv::Point2f{x, y});
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetRadius
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jfloat) obj->get_radius();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetRadius
(JNIEnv *env, jclass clazz, jlong thiz, jfloat radius)
{
    try {
        ((Vignette*) thiz)->set_radius(radius);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetStrength
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jfloat) obj->get_strength();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetStrength
(JNIEnv *env, jclass clazz, jlong thiz, jfloat strength)
{
    try {
        ((Vignette*) thiz)->set_strength(strength);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetFeathering
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jfloat) obj->get_feathering();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetFeathering
(JNIEnv *env, jclass clazz, jlong thiz, jfloat feathering)
{
    try {
        ((Vignette*) thiz)->set_feathering(feathering);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetColor
(JNIEnv *env, jclass clazz, jlong thiz, jintArray arr)
{
    Vignette* obj = (Vignette*) thiz;
    jint *color_arr = env->GetIntArrayElements(arr, JNI_FALSE);
    cv::Scalar_<uchar> color = obj->get_color();
    color_arr[0] = color[0];
    color_arr[1] = color[1];
    color_arr[2] = color[2];
    env->ReleaseIntArrayElements(arr, color_arr, 0);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetColor
(JNIEnv *env, jclass clazz, jlong thiz, jint b, jint g, jint r)
{
    try {
        Vignette* obj = (Vignette*) thiz;
        uchar blue = cv::saturate_cast<uchar>(b);
        uchar green = cv::saturate_cast<uchar>(g);
        uchar red = cv::saturate_cast<uchar>(r);
        obj->set_color(cv::Scalar_<uchar>{blue, green, red});
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeIsFitToImage
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Vignette*) thiz)->is_fit_to_image();
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetFitToImage
(JNIEnv *env, jclass clazz, jlong thiz, jboolean is_fit_to_image)
{
    ((Vignette*) thiz)->set_fit_to_image((bool) is_fit_to_image);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Vignette* obj = (Vignette*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
