#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/saturation.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_SaturationAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Saturation* obj = new Saturation();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_SaturationAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Saturation* obj = (Saturation*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_SaturationAdjust_nativeGetSaturation
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Saturation* obj = (Saturation*) thiz;
    return (jdouble) obj->get_saturation();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_SaturationAdjust_nativeSetSaturation
(JNIEnv *env, jclass clazz, jlong thiz, jdouble saturation)
{
    Saturation* obj = (Saturation*) thiz;
    return (jboolean) obj->set_saturation(saturation);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_SaturationAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Saturation* obj = (Saturation*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
