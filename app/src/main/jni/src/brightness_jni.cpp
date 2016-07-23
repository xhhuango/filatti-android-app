#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/brightness.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_BrightnessAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Brightness* obj = new Brightness();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_BrightnessAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Brightness* obj = (Brightness*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_BrightnessAdjust_nativeGetBrightness
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Brightness* obj = (Brightness*) thiz;
    return (jdouble) obj->get_brightness();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_BrightnessAdjust_nativeSetBrightness
(JNIEnv *env, jclass clazz, jlong thiz, jdouble brightness)
{
    Brightness* obj = (Brightness*) thiz;
    return (jboolean) obj->set_brightness(brightness);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_BrightnessAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Brightness* obj = (Brightness*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
