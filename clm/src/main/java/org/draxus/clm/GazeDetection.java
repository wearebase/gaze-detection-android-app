package org.draxus.clm;

import android.util.Log;

import org.opencv.core.Mat;

public class GazeDetection {
    private static final String TAG = "GazeDetection";

    private String modelFile;

    static {
        try {
            System.loadLibrary("SharedFeatureExtraction");
            Log.d(TAG, "jniNativeClassInit success");
        } catch (UnsatisfiedLinkError e) {
            Log.d(TAG, "library not found!");
        }
    }

    public GazeDetection(String modelFile) {
        this.modelFile = modelFile;
    }

    public String runDetection(Mat frame) {

        Log.d(TAG, "Running FeatureExtraction");

        Log.d(TAG, "ABI = " + stringFromJNI());

        Log.d(TAG, "Frame size = " + frame.rows() + "x" + frame.cols());

        Log.i(TAG, jniGazeDet(frame.getNativeObjAddr(), modelFile));

        return "Done";
    }

    public void init() {
        jniInit();
    }

    public void deInit() {
        jniDeInit();
    }

    @SuppressWarnings("JniMissingFunction")
    private native static void jniNativeClassInit();

    @SuppressWarnings("JniMissingFunction")
    private native int jniInit();

    @SuppressWarnings("JniMissingFunction")
    private native int jniDeInit();

    @SuppressWarnings("JniMissingFunction")
    private native String jniGazeDet(long frameAddress, String modelFile);

    @SuppressWarnings("JniMissingFunction")
    public native String stringFromJNI();
}
