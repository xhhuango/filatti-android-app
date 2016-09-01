#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/temperature.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

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

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeGetKelvin
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Temperature* obj = (Temperature*) thiz;
    return (jint) obj->get_kelvin();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeSetKelvin
(JNIEnv *env, jclass clazz, jlong thiz, jint kelvin)
{
    Temperature* obj = (Temperature*) thiz;
    return (jboolean) obj->set_kelvin(kelvin);
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeGetStrength
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Temperature* obj = (Temperature*) thiz;
    return (jdouble) obj->get_strength();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TemperatureAdjust_nativeSetStrength
(JNIEnv *env, jclass clazz, jlong thiz, jdouble strength)
{
    Temperature* obj = (Temperature*) thiz;
    return (jboolean) obj->set_strength(strength);
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
