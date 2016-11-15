#include <jni.h>

#include "log.hpp"

#include <filatti/exposure.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Exposure* obj = new Exposure();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Exposure* obj = (Exposure*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Exposure*) thiz)->has_effect();
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeGetExposure
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Exposure* obj = (Exposure*) thiz;
    return (jfloat) obj->get_exposure();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeSetExposure
(JNIEnv *env, jclass clazz, jlong thiz, jfloat exposure)
{
    try {
        ((Exposure*) thiz)->set_exposure(exposure);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ExposureAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Exposure* obj = (Exposure*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
