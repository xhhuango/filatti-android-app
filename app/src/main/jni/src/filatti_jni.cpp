#include <jni.h>

#include <filatti/saturation.hpp>

using namespace filatti;

extern "C" {

JNIEXPORT jlong JNICALL
Java_com_filatti_effects_adjusts_SaturationNativeAdjust_setSaturation(JNIEnv *env, jclass clazz)
{
    Saturation *self = new Saturation();
    self->set_saturation(0.7);
    double s = self->get_saturation();
    return (jlong) (s * 700);
}

} // extern "C"
