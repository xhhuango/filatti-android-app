#include <jni.h>

#include "log.hpp"

#include <filatti/vibrance.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Vibrance* obj = new Vibrance();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Vibrance* obj = (Vibrance*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Vibrance*) thiz)->has_effect();
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeGetVibrance
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Vibrance* obj = (Vibrance*) thiz;
    return (jfloat) obj->get_vibrance();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeSetVibrance
(JNIEnv *env, jclass clazz, jlong thiz, jfloat vibrance)
{
    try {
        ((Vibrance*) thiz)->set_vibrance(vibrance);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_VibranceAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Vibrance* obj = (Vibrance*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
