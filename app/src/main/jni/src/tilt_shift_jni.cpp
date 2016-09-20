#include <jni.h>

#include "log.hpp"

#include <filatti/tilt_shift.hpp>
#include <filatti/exception.hpp>

extern "C" {

using namespace cv;
using namespace filatti;
using libException = filatti::Exception;

static const char* TAG = __FILE__;

JNIEXPORT jlong JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeCreateObject
(JNIEnv* env, jclass clazz)
{
    TiltShift* obj = new TiltShift();
    return (jlong) obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeDestroyObject
(JNIEnv* env, jclass clazz, jlong thiz)
{
    TiltShift* obj = (TiltShift*) thiz;
    delete obj;
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jdoubleArray arr)
{
    TiltShift* obj = (TiltShift*) thiz;
    jdouble *center_arr = env->GetDoubleArrayElements(arr, JNI_FALSE);
    cv::Point2d center = obj->get_center();
    center_arr[0] = center.x;
    center_arr[1] = center.y;
    env->ReleaseDoubleArrayElements(arr, center_arr, 0);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetCenter
(JNIEnv *env, jclass clazz, jlong thiz, jdouble x, jdouble y)
{
    try {
        ((TiltShift*) thiz)->set_center(cv::Point2d{x, y});
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetRadius
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return (jdouble) ((TiltShift*) thiz)->get_radius();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetRadius
(JNIEnv *env, jclass clazz, jlong thiz, jdouble radius)
{
    try {
        ((TiltShift*) thiz)->set_radius(radius);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetStrength
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return (jdouble) ((TiltShift*) thiz)->get_strength();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetStrength
(JNIEnv *env, jclass clazz, jlong thiz, jdouble strength)
{
    try {
        ((TiltShift*) thiz)->set_strength(strength);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetFeathering
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return (jdouble) ((TiltShift*) thiz)->get_feathering();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetFeathering
(JNIEnv *env, jclass clazz, jlong thiz, jdouble feathering)
{
    try {
        ((TiltShift*) thiz)->set_feathering(feathering);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT jdouble JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetAngle
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return (jdouble) ((TiltShift*) thiz)->get_angle();
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetAngle
(JNIEnv *env, jclass clazz, jlong thiz, jdouble angle)
{
    try {
        ((TiltShift*) thiz)->set_angle(angle);
        return true;
    } catch (libException& e) {
        LOGW(TAG, e.what());
        return false;
    }
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetRebuildBlurred
(JNIEnv *env, jclass clazz, jlong thiz, jboolean does_rebuild_blurred)
{
    ((TiltShift*) thiz)->set_rebuild_blurred(does_rebuild_blurred);
}

JNIEXPORT jint JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeGetMaskType
(JNIEnv *env, jclass clazz, jlong thiz)
{
    return static_cast<unsigned int>(((TiltShift*) thiz)->get_mask_type());
}

JNIEXPORT void JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeSetMaskType
(JNIEnv *env, jclass clazz, jlong thiz, jint mask_type)
{
    TiltShift::MaskType type = static_cast<TiltShift::MaskType>((unsigned int) mask_type);
    ((TiltShift*) thiz)->set_mask_type(type);
}

JNIEXPORT jboolean JNICALL Java_com_filatti_effects_adjusts_TiltShiftAdjust_nativeApply
(JNIEnv *env, jclass clazz, jlong thiz, jlong src, jlong dst)
{
    TiltShift* obj = (TiltShift*) thiz;
    Mat* src_mat = (Mat*) src;
    Mat* dst_mat = (Mat*) dst;
    return (jboolean) obj->apply(*src_mat, *dst_mat);
}

} // extern "C"
