#include <jni.h>

#include "log.hpp"

#include <filatti/highlight_shadow.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    HighlightShadow* obj = new HighlightShadow();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    HighlightShadow* obj = (HighlightShadow*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeGetAmount
(JNIEnv *env, jclass clazz, jlong thiz, jint tone)
{
    HighlightShadow* obj = (HighlightShadow*) thiz;
    return (jdouble) obj->get_amount(static_cast<Tone>((ToneRawType) tone));
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeSetAmount
(JNIEnv *env, jclass clazz, jlong thiz, jint tone, jdouble amount)
{
    try {
        ((HighlightShadow*) thiz)->set_amount(static_cast<Tone>((ToneRawType) tone), amount);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeGetToneWidth
(JNIEnv *env, jclass clazz, jlong thiz, jint tone)
{
    HighlightShadow* obj = (HighlightShadow*) thiz;
    return (jdouble) obj->get_tone_width(static_cast<Tone>((ToneRawType) tone));
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeSetToneWidth
(JNIEnv *env, jclass clazz, jlong thiz, jint tone, jdouble width)
{
    try {
        ((HighlightShadow*) thiz)->set_tone_width(static_cast<Tone>((ToneRawType) tone), width);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_HighlightShadowAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    HighlightShadow* obj = (HighlightShadow*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
