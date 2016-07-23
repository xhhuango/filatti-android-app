#include <jni.h>

#include <opencv2/core.hpp>
#include <filatti/contrast.hpp>

extern "C" {

using namespace cv;
using namespace filatti;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Contrast* obj = new Contrast();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Contrast* obj = (Contrast*) thiz;
    delete obj;
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeGetContrast
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Contrast* obj = (Contrast*) thiz;
    return (jdouble) obj->get_contrast();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeSetContrast
(JNIEnv *env, jclass clazz, jlong thiz, jdouble contrast)
{
    Contrast* obj = (Contrast*) thiz;
    return (jboolean) obj->set_contrast(contrast);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Contrast* obj = (Contrast*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
