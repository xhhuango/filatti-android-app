#include <jni.h>

#include "log.hpp"

#include <filatti/contrast.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

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

JNIEXPORT jfloat JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeGetContrast
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Contrast* obj = (Contrast*) thiz;
    return (jfloat) obj->get_contrast();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_ContrastAdjust_nativeSetContrast
(JNIEnv *env, jclass clazz, jlong thiz, jfloat contrast)
{
    try {
        ((Contrast*) thiz)->set_contrast(contrast);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
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
