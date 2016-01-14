package org.draxus.clm;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import weka.classifiers.lazy.IBk;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GazeDetection {
    private static final String TAG = "GazeDetection";

    private IBk classifierX, classifierY;
    private Instances dataX, dataY;

    static {
        try {
            System.loadLibrary("SharedFeatureExtraction");
            Log.d(TAG, "jniNativeClassInit success");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "library not found!");
            throw e;
        }
    }

    public GazeDetection(String modelFile, String faceDetectorFile, String dataFile) {
        jniNativeClassInit(modelFile, faceDetectorFile);
        try {
            buildClassifier(dataFile + "_x.arff", dataFile + "_y.arff");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildClassifier(String dataFilenameX, String dataFilenameY) throws Exception {
        DataSource sourceX = new DataSource(dataFilenameX);
        dataX = sourceX.getDataSet();
        dataX.setClassIndex(dataX.numAttributes() - 1);

        DataSource sourceY = new DataSource(dataFilenameY);
        dataY = sourceY.getDataSet();
        dataY.setClassIndex(dataY.numAttributes() - 1);

        classifierX = new IBk();
        classifierX.buildClassifier(dataX);

        classifierY = new IBk();
        classifierY.buildClassifier(dataY);

        Log.d(TAG, "Classifiers build SUCCESS");
    }

    public Point runDetection(Mat frame) {

        Log.d(TAG, "Running FeatureExtraction");

        Log.d(TAG, "ABI = " + stringFromJNI());

        Log.d(TAG, "Frame size = " + frame.rows() + "x" + frame.cols());

        String gazeVectors = jniGazeDet(frame.getNativeObjAddr());

        String[] gazeFeatures = gazeVectors.split(",");

        if (gazeFeatures.length == 12 && classifierX != null && classifierY != null) {

            Instance instance = new DenseInstance(dataX.numAttributes());

            for (int i = 0; i < 12; i++) {
                instance.setValue(i, Double.valueOf(gazeFeatures[i]));
            }

            try {
                instance.setDataset(dataX);
                double predictionX = classifierX.classifyInstance(instance);
                instance.setDataset(dataY);
                double predictionY = classifierY.classifyInstance(instance);
                Log.d(TAG, "Prediction = " + predictionX + ", " + predictionY);
                return new Point(predictionY / 3.3, predictionX / 3.2); // TODO fix this scaling
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
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
