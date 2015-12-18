LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_ARCH_ABI := armeabi-v7a

OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
include $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/opencv/jni/OpenCV.mk

LOCAL_MODULE := FaceAnalyser

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../CLM/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/dlib

FILE_LIST := $(wildcard $(LOCAL_PATH)/src/*.cpp)
LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

LOCAL_STATIC_LIBRARIES += boost_serialization_static boost_filesystem dlib

LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11
LOCAL_CFLAGS  += -pie -fPIE

include $(BUILD_STATIC_LIBRARY)

$(call import-module,boost/1.59.0)
