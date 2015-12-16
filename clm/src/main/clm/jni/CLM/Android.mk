LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/dlib/Android.mk

OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
OPENCV_3RDPARTY_COMPONENTS:=libjpeg libwebp libpng libtiff libjasper IlmImf tbb
OPENCV_EXTRA_COMPONENTS:=z dl m log
include $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/opencv/jni/OpenCV.mk

LOCAL_MODULE := CLM

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/opencv/jni/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../../../../../dlib/src/main/dlib/jni/dlib

FILE_LIST := $(wildcard $(LOCAL_PATH)/src/*.cpp)

LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

LOCAL_STATIC_LIBRARIES := boost_serialization_static libjpeg libwebp libpng libtiff libjasper IlmImf tbb dlib
LOCAL_STATIC_LIBRARIES += opencv_objdetect opencv_calib3d opencv_videoio opencv_highgui opencv_imgproc opencv_imgcodecs opencv_core opencv_hal

LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11
LOCAL_CFLAGS  += -pie -fPIE

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
include $(BUILD_STATIC_LIBRARY)
$(call import-module,boost/1.59.0)

