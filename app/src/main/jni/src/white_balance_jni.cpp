#include <jni.h>

#include "log.hpp"

#include <filatti/white_balance.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    WhiteBalance* obj = new WhiteBalance();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    WhiteBalance* obj = (WhiteBalance*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((WhiteBalance*) thiz)->has_effect();
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeGetPercent
(JNIEnv *env, jclass clazz, jlong thiz)
{
    WhiteBalance* obj = (WhiteBalance*) thiz;
    return (jfloat) obj->get_percent();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeSetPercent
(JNIEnv *env, jclass clazz, jlong thiz, jfloat percent)
{
    try {
        ((WhiteBalance*) thiz)->set_percent(percent);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_WhiteBalanceAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    WhiteBalance* obj = (WhiteBalance*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
