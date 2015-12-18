LOCAL_PATH := $(call my-dir)
SUB_MK_FILES := $(call all-subdir-makefiles)

#==========================FeatureExtraction===================================
include $(CLEAR_VARS)

TARGET_ARCH_ABI := armeabi-v7a
OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
include $(LOCAL_PATH)/../../../../../dlib/src/main/dlib/jni/opencv/jni/OpenCV.mk

LOCAL_MODULE := FeatureExtraction

LOCAL_C_INCLUDES +=  \
    $(LOCAL_PATH)/CLM/include \
    $(LOCAL_PATH)/FaceAnalyser/include \

LOCAL_SRC_FILES := jnilib_ex/FeatureExtraction/FeatureExtraction.cpp

LOCAL_STATIC_LIBRARIES += boost_filesystem_static boost_system_static boost_serialization_static
LOCAL_STATIC_LIBRARIES += CLM FaceAnalyser

LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11
LOCAL_CFLAGS  += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE
LOCAL_LDLIBS  += -lstdc++

LOCAL_ARM_MODE := arm
ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
LOCAL_ARM_NEON := true
endif

include $(BUILD_EXECUTABLE)
#==========================FeatureExtraction===================================

$(call import-module,boost/1.59.0)

include $(SUB_MK_FILES)
