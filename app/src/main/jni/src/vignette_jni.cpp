#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/vignette.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

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

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jdoubleArray arr)
{
    Vignette* obj = (Vignette*) thiz;
    jdouble *center_arr = env->GetDoubleArrayElements(arr, JNI_FALSE);
    std::vector<double> center = obj->get_center();
    center_arr[0] = center[0];
    center_arr[1] = center[1];
    env->ReleaseDoubleArrayElements(arr, center_arr, 0);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jdouble x, jdouble y)
{
    Vignette* obj = (Vignette*) thiz;
    return (jboolean) obj->set_center(x, y);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetRadius
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jdouble) obj->get_radius();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetRadius
(JNIEnv *env, jclass clazz, jlong thiz, jdouble radius)
{
    Vignette* obj = (Vignette*) thiz;
    return (jboolean) obj->set_radius(radius);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetStrength
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jdouble) obj->get_strength();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetStrength
(JNIEnv *env, jclass clazz, jlong thiz, jdouble strength)
{
    Vignette* obj = (Vignette*) thiz;
    return (jboolean) obj->set_strength(strength);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeGetFeathering
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vignette* obj = (Vignette*) thiz;
    return (jdouble) obj->get_feathering();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VignetteAdjust_nativeSetFeathering
(JNIEnv *env, jclass clazz, jlong thiz, jdouble feathering)
{
    Vignette* obj = (Vignette*) thiz;
    return (jboolean) obj->set_feathering(feathering);
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
    Vignette* obj = (Vignette*) thiz;
    uchar blue = cv::saturate_cast<uchar>(b);
    uchar green = cv::saturate_cast<uchar>(g);
    uchar red = cv::saturate_cast<uchar>(r);
    return (jboolean) obj->set_color(cv::Scalar_<uchar>{blue, green, red});
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
