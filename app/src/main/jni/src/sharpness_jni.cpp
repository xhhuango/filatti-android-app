#include <jni.h>

#include "log.hpp"

#include <filatti/sharpness.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

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
    try {
        ((Sharpness*) thiz)->set_sharpness(sharpness);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_SharpnessAdjust_nativeSetRebuildBlurred
(JNIEnv *env, jclass clazz, jlong thiz, jboolean does_rebuild_blurred)
{
    ((Sharpness*) thiz)->set_rebuild_blurred(does_rebuild_blurred);
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
