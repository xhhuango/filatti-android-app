#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/hls.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Hls* obj = new Hls();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Hls* obj = (Hls*) thiz;
    delete obj;
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeGetHue
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Hls* obj = (Hls*) thiz;
    return (jint) obj->get_hue();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeSetHue
(JNIEnv *env, jclass clazz, jlong thiz, jint hue)
{
    Hls* obj = (Hls*) thiz;
    return (jboolean) obj->set_hue(hue);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeGetLightness
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Hls* obj = (Hls*) thiz;
    return (jdouble) obj->get_lightness();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeSetLightness
(JNIEnv *env, jclass clazz, jlong thiz, jdouble lightness)
{
    Hls* obj = (Hls*) thiz;
    return (jboolean) obj->set_lightness(lightness);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeGetSaturation
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Hls* obj = (Hls*) thiz;
    return (jdouble) obj->get_saturation();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeSetSaturation
(JNIEnv *env, jclass clazz, jlong thiz, jdouble saturation)
{
    Hls* obj = (Hls*) thiz;
    return (jboolean) obj->set_saturation(saturation);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HlsAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Hls* obj = (Hls*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
