#include <jni.h>

#include "log.hpp"

#include <filatti/temperature.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Temperature* obj = new Temperature();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Temperature* obj = (Temperature*) thiz;
    delete obj;
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeHasEffect
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return ((Temperature*) thiz)->has_effect();
}

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeGetTemperature
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Temperature* obj = (Temperature*) thiz;
    return (jfloat) obj->get_temperature();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeSetTemperature
(JNIEnv *env, jclass clazz, jlong thiz, jfloat temperature)
{
    try {
        ((Temperature*) thiz)->set_temperature(temperature);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Temperature* obj = (Temperature*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
