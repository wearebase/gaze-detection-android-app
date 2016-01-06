LOCAL_PATH := $(call my-dir)
SUB_MK_FILES := $(call all-subdir-makefiles)

#include $(LOCAL_PATH)/../../../../../dlib/src/main/dlib/jni/dlib/Android.mk

include $(CLEAR_VARS)
LOCAL_MODULE    := boost_serialization_static
LOCAL_SRC_FILES := /home/manuel/github/dlib-android-app/clm/src/main/clm/jni/boost/armv7a/lib/libboost_serialization-gcc-mt-1_59.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := boost_filesystem_static
LOCAL_SRC_FILES := /home/manuel/github/dlib-android-app/clm/src/main/clm/jni/boost/armv7a/lib/libboost_filesystem-gcc-mt-1_59.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := boost_system_static
LOCAL_SRC_FILES := /home/manuel/github/dlib-android-app/clm/src/main/clm/jni/boost/armv7a/lib/libboost_system-gcc-mt-1_59.a
include $(PREBUILT_STATIC_LIBRARY)



#==========================FeatureExtraction===================================
include $(CLEAR_VARS)

TARGET_ARCH_ABI := armeabi-v7a
OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
include $(LOCAL_PATH)/../../../../../dlib/src/main/dlib/jni/opencv/jni/OpenCV.mk

LOCAL_MODULE := FeatureExtraction

LOCAL_C_INCLUDES +=  \
    $(LOCAL_PATH)/boost/armv7a/include \
    $(LOCAL_PATH)/CLM/include \
    $(LOCAL_PATH)/FaceAnalyser/include \

LOCAL_SRC_FILES := jnilib_ex/FeatureExtraction/FeatureExtraction.cpp

LOCAL_STATIC_LIBRARIES += boost_filesystem_static boost_system_static boost_serialization_static
LOCAL_STATIC_LIBRARIES += CLM FaceAnalyser

LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11
LOCAL_CFLAGS  += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE

LOCAL_ARM_MODE := arm
ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
LOCAL_ARM_NEON := true
endif

include $(BUILD_EXECUTABLE)
#==========================FeatureExtraction===================================

include $(SUB_MK_FILES)
