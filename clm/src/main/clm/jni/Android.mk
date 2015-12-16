LOCAL_PATH := $(call my-dir)
SUB_MK_FILES := $(call all-subdir-makefiles)

TARGET_ARCH_ABI := armeabi-v7a

#==========================FeatureExtraction===================================
include $(CLEAR_VARS)

OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
OPENCV_3RDPARTY_COMPONENTS := libjpeg libwebp libpng libtiff libjasper IlmImf tbb
OPENCV_EXTRA_COMPONENTS := z dl m log
include $(LOCAL_PATH)/../../../../../dlib/src/main/dlib/jni/opencv/jni/OpenCV.mk

LOCAL_MODULE := FeatureExtraction

LOCAL_C_INCLUDES :=  \
    $(LOCAL_PATH)/CLM/include \
    $(LOCAL_PATH)/FaceAnalyser/include \
    $(LOCAL_PATH)/../../../../../dlib/src/main/dlib/jni/opencv/jni/include \
    $(DLIB_SOURCES_PATH)

LOCAL_SRC_FILES := jnilib_ex/FeatureExtraction/FeatureExtraction.cpp

LOCAL_STATIC_LIBRARIES := boost_filesystem_static boost_system_static boost_serialization_static
LOCAL_STATIC_LIBRARIES += CLM FaceAnalyser dlib libjpeg libwebp libpng libtiff libjasper IlmImf tbb
#LOCAL_STATIC_LIBRARIES += opencv_calib3d opencv_core opencv_features2d opencv_flann  opencv_highgui opencv_imgcodecs opencv_imgproc opencv_java3 opencv_ml opencv_objdetect opencv_photo opencv_shape opencv_stitching opencv_superres opencv_ts opencv_video opencv_videoio opencv_videostab
LOCAL_STATIC_LIBRARIES += opencv_objdetect opencv_calib3d opencv_videoio opencv_highgui opencv_imgproc opencv_imgcodecs opencv_core opencv_hal

LOCAL_CPPFLAGS += -fexceptions -frtti -std=c++11
LOCAL_CFLAGS  += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE
#LOCAL_LDLIBS  := -lm -llog -ldl -lz -lstdc++

LOCAL_ARM_MODE := arm
ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
LOCAL_ARM_NEON := true
endif

include $(BUILD_EXECUTABLE)
#==========================FeatureExtraction===================================

$(call import-module,boost/1.59.0)

include $(SUB_MK_FILES)
