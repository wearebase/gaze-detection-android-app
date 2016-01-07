NDK_TOOLCHAIN_VERSION := 4.9
APP_PLATFORM := android-21
APP_STL := gnustl_static
APP_CFLAGS += -std=c++11
APP_CPPFLAGS := -frtti -fexceptions -std=c++11
APP_ABI := armeabi-v7a #x86 #x86_64 #armeabi #all
APP_OPTIM := release