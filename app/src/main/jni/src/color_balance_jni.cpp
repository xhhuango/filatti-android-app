#include <jni.h>

#include "log.hpp"

#include <filatti/color_balance.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    ColorBalance* obj = new ColorBalance();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    ColorBalance* obj = (ColorBalance*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((ColorBalance*) thiz)->has_effect();
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeGetRedCyan
(JNIEnv *env, jclass clazz, jlong thiz, jint tone)
{
    ColorBalance* obj = (ColorBalance*) thiz;
    return (jint) obj->get_red_cyan(static_cast<Tone>((ToneRawType) tone));
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeSetRedCyan
(JNIEnv *env, jclass clazz, jlong thiz, jint tone, jint value)
{
    try {
        ((ColorBalance*) thiz)->set_red_cyan(static_cast<Tone>((ToneRawType) tone), value);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeGetGreenMagenta
(JNIEnv *env, jclass clazz, jlong thiz, jint tone)
{
    ColorBalance* obj = (ColorBalance*) thiz;
    return (jint) obj->get_green_magenta(static_cast<Tone>((ToneRawType) tone));
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeSetGreenMagenta
(JNIEnv *env, jclass clazz, jlong thiz, jint tone, jint value)
{
    try {
        ((ColorBalance*) thiz)->set_green_magenta(static_cast<Tone>((ToneRawType) tone), value);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeGetBlueYellow
(JNIEnv *env, jclass clazz, jlong thiz, jint tone)
{
    ColorBalance* obj = (ColorBalance*) thiz;
    return (jint) obj->get_blue_yellow(static_cast<Tone>((ToneRawType) tone));
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeSetBlueYellow
(JNIEnv *env, jclass clazz, jlong thiz, jint tone, jint value)
{
    try {
        ((ColorBalance*) thiz)->set_blue_yellow(static_cast<Tone>((ToneRawType) tone), value);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ColorBalanceAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    ColorBalance* obj = (ColorBalance*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
