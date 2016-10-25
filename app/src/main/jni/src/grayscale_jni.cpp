#include <jni.h>

#include "log.hpp"

#include <filatti/grayscale.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    Grayscale* obj = new Grayscale();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    Grayscale* obj = (Grayscale*) thiz;
    delete obj;
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeGetMode
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return static_cast<Grayscale::ModeRawType>(((Grayscale*) thiz)->get_mode());
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeSetMode
(JNIEnv *env, jclass clazz, jlong thiz, jint mode)
{
    ((Grayscale*) thiz)->set_mode(static_cast<Grayscale::Mode>((Grayscale::ModeRawType) mode));
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeGetBlurKernelSize
(JNIEnv *env, jclass clazz, jlong thiz)
{
    Grayscale* obj = (Grayscale*) thiz;
    return (jint) obj->get_blur_kernel_size();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeSetBlurKernelSize
(JNIEnv *env, jclass clazz, jlong thiz, jint blur_kernel_size)
{
    try {
        ((Grayscale*) thiz)->set_blur_kernel_size((unsigned int) blur_kernel_size);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_GrayscaleAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    Grayscale* obj = (Grayscale*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
