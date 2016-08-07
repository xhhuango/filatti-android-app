LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libfilatti

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_LDLIBS := -lstdc++
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES += opencv_java3

LIB_FILATTI_FILES := $(wildcard $(LOCAL_PATH)/libfilatti/*.cpp)
LIB_FILATTI_FILES += $(wildcard $(LOCAL_PATH)/libfilatti/interpolator/*.cpp)
LOCAL_SRC_FILES := $(LIB_FILATTI_FILES:$(LOCAL_PATH)/%=%)

JNI_FILES := $(wildcard $(LOCAL_PATH)/src/*.cpp)
LOCAL_SRC_FILES += $(JNI_FILES:$(LOCAL_PATH)/%=%)

include $(BUILD_SHARED_LIBRARY)

######################
# Prebuilt libraries #
######################

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_java3
LOCAL_SRC_FILES := libopencv/$(TARGET_ARCH_ABI)/libopencv_java3.so
include $(PREBUILT_SHARED_LIBRARY)
