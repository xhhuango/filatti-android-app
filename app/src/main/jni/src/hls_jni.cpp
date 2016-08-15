#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/hsv.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Hsv* obj = new Hsv();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Hsv* obj = (Hsv*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeGetBrightness
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Hsv* obj = (Hsv*) thiz;
    return (jdouble) obj->get_brightness();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeSetBrightness
(JNIEnv *env, jclass clazz, jlong thiz, jdouble brightness)
{
    Hsv* obj = (Hsv*) thiz;
    return (jboolean) obj->set_brightness(brightness);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeGetSaturation
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Hsv* obj = (Hsv*) thiz;
    return (jdouble) obj->get_saturation();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeSetSaturation
(JNIEnv *env, jclass clazz, jlong thiz, jdouble saturation)
{
    Hsv* obj = (Hsv*) thiz;
    return (jboolean) obj->set_saturation(saturation);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Hsv* obj = (Hsv*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
