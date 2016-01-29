## About this repository

This repository contains a proof of concept gaze detection Android app.

This work integrates code from the following open-source projects:
* [OpenCV](https://github.com/Itseez/opencv) the major open-source computer vision library.
* [Cambridge face tracker](https://github.com/TadasBaltrusaitis/CLM-framework): a Constrained Local Model (CLM) framework developed by Tadas Baltrušaitis from the University of Cambridge.
* [dlib](http://dlib.net/): a C++ library for machine learning.
* [dlib-android-app](https://github.com/tzutalin/dlib-android-app) integrates dlib library in Android.
* [Boost](https://github.com/DraXus/Boost-for-Android): a collection of C++ libraries.
* [Weka](https://github.com/rjmarsan/Weka-for-Android): the main library for machine learning algorithms.

### Here be dragons

This project is in a highly experimental state. We are not responsible for any damage that can happen to your device. Currently the app has been tested in the following devices:
* Nexus 10
* Samsung Galaxy Note 10.1

### Download

Note: Currently the APK only works for devices with ARMv7-a processor.

[gaze-detection.apk](https://github.com/DraXus/gaze-detection-android-app/raw/master/demo/gaze-detection.apk) 8.4MB v1.0
md5sum: a5bef6fb6e54e56a1786b9f77688abce

Additional required files: [Gazer.zip](https://github.com/DraXus/gaze-detection-android-app/raw/master/demo/Gazer.zip) 21M  
md5sum: a6122e017046bf927a148f8f3927a152

### Install

Enable installation of apps from unknown sources in the settings of your device. Then open the APK file from your device to install it.

Extract Gazer.zip into your /sdcard folder.

From command line: `$ adb install demo/gaze-detection.apk`

### Get the sources

`$ git clone https://github.com/DraXus/gaze-detection-android-app`

`$ git submodule update --init --recursive`

### Development

The project is ready to work in Android studio 1.5.

Requirements:
* Android SDK 23 (min API level is 18)
* Android NDK 10e

### License

See LICENSE file.

External libraries used in this project have their own licenses and copyrights.

### Acknowledgement

This work has been possible thanks to the Knowledge Transfer Partnership (KTP) between We Are Base and Bournemouth University.
