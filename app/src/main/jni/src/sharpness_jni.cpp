#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/sharpness.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Sharpness* obj = new Sharpness();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Sharpness* obj = (Sharpness*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeGetSharpness
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Sharpness* obj = (Sharpness*) thiz;
    return (jdouble) obj->get_sharpness();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeSetSharpness
(JNIEnv *env, jclass clazz, jlong thiz, jdouble sharpness)
{
    Sharpness* obj = (Sharpness*) thiz;
    return (jboolean) obj->set_sharpness(sharpness);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Sharpness* obj = (Sharpness*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
