#include <jni.h>

#include "log.hpp"

#include <filatti/hls.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

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
    try {
        ((Hls*) thiz)->set_hue(hue);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
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
    try {
        ((Hls*) thiz)->set_lightness(lightness);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
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
    try {
        ((Hls*) thiz)->set_saturation(saturation);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
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
