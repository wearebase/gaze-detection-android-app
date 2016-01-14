package org.draxus.clm;

import android.util.Log;

import org.opencv.core.Mat;

public class GazeDetection {
    private static final String TAG = "GazeDetection";

    static {
        try {
            System.loadLibrary("SharedFeatureExtraction");
            Log.d(TAG, "jniNativeClassInit success");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "library not found!");
            throw e;
        }
    }

    public GazeDetection(String modelFile, String faceDetectorFile) {
        jniNativeClassInit(modelFile, faceDetectorFile);
    }

    public String runDetection(Mat frame) {

        Log.d(TAG, "Running FeatureExtraction");

        Log.d(TAG, "ABI = " + stringFromJNI());

        Log.d(TAG, "Frame size = " + frame.rows() + "x" + frame.cols());

        Log.i(TAG, jniGazeDet(frame.getNativeObjAddr()));

        return "Done";
    }

    public void init() {
        jniInit();
    }

    public void deInit() {
        jniDeInit();
    }

    @SuppressWarnings("JniMissingFunction")
    private native static void jniNativeClassInit(String modelFile, String faceDetectorFile);

    @SuppressWarnings("JniMissingFunction")
    private native int jniInit();

    @SuppressWarnings("JniMissingFunction")
    private native int jniDeInit();

    @SuppressWarnings("JniMissingFunction")
    private native String jniGazeDet(long frameAddress);

    @SuppressWarnings("JniMissingFunction")
    public native String stringFromJNI();
}
