package org.draxus.clm;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;

import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class GazeDetection {
    private static final String TAG = "GazeDetection";
    public static final int NUM_FEATURES = 12;
    private static final int NUM_TRAIN_INSTANCES = 250;

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

        ArrayList<Attribute> atts = new ArrayList<>(NUM_FEATURES + 1);
        atts.add(new Attribute("x1"));
        atts.add(new Attribute("y1"));
        atts.add(new Attribute("z1"));
        atts.add(new Attribute("x2"));
        atts.add(new Attribute("y2"));
        atts.add(new Attribute("z2"));
        atts.add(new Attribute("x3"));
        atts.add(new Attribute("y3"));
        atts.add(new Attribute("z3"));
        atts.add(new Attribute("x4"));
        atts.add(new Attribute("y4"));
        atts.add(new Attribute("z4"));
        atts.add(new Attribute("class"));

        dataX = new Instances("dataX", atts, 0);
        dataY = new Instances("dataY", atts, 0);

    }

    /**
     * @throws Exception
     */

    private void buildClassifier() throws Exception {
//        DataSource sourceX = new DataSource(dataFilenameX);
//        dataX = sourceX.getDataSet();
//        dataX.setClassIndex(dataX.numAttributes() - 1);

//        DataSource sourceY = new DataSource(dataFilenameY);
//        dataY = sourceY.getDataSet();
//        dataY.setClassIndex(dataY.numAttributes() - 1);
        
        classifierX = new IBk();
        dataX.setClassIndex(dataX.numAttributes() - 1);
        classifierX.buildClassifier(dataX);

        classifierY = new IBk();
        dataY.setClassIndex(dataY.numAttributes() - 1);
        classifierY.buildClassifier(dataY);

        Log.d(TAG, "Classifiers build SUCCESS");
    }

    /**
     * @param gazeFeatures
     * @return predicted Point
     */
    public Point predictLocation(Double[] gazeFeatures) {

        if (gazeFeatures == null || gazeFeatures.length != NUM_FEATURES) {
            Log.d(TAG, "Wrong features: " + (gazeFeatures == null ? "NULL" : Utils.arrayToString(gazeFeatures)));
            return null;
        }

        if (classifierX == null || classifierY == null) {
            Log.d(TAG, "Classifiers have not been trained");
            return null;
        }

        Log.d(TAG, "Trying to predict");

        Instance instance = new DenseInstance(dataX.numAttributes());

        for (int i = 0; i < NUM_FEATURES; i++) {
            instance.setValue(i, gazeFeatures[i]);
        }

        try {
            instance.setDataset(dataX);
            double predictionX = classifierX.classifyInstance(instance);
            instance.setDataset(dataY);
            double predictionY = classifierY.classifyInstance(instance);
            Log.d(TAG, "Prediction = " + predictionX + ", " + predictionY);
            //return new Point(predictionY / 3.3, predictionX / 3.2); // TODO fix this scaling
            return new Point(predictionY, predictionX);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param frame
     * @return vector of gaze features
     */
    public Double[] runDetection(Mat frame) {

        Log.d(TAG, "Running FeatureExtraction");

        //Log.d(TAG, "ABI = " + stringFromJNI());

        //Log.d(TAG, "Frame size = " + frame.rows() + "x" + frame.cols());

        String gazeVectors = jniGazeDet(frame.getNativeObjAddr());

        String[] gazeFeatures = gazeVectors.split(",");

        if (gazeFeatures.length != NUM_FEATURES) {
            Log.d(TAG, "Detection FAILED: " + Utils.arrayToString(gazeFeatures));
            return null;
        }

        Double[] gazeFeaturesDouble = new Double[gazeFeatures.length];

        for (int i = 0; i < gazeFeatures.length; i++) {
            gazeFeaturesDouble[i] = Double.parseDouble(gazeFeatures[i]);
        }

        return gazeFeaturesDouble;
    }

    /**
     * @param gazeFeatures
     * @param target
     * @return true if the models have been trained
     */
    public boolean train(Double[] gazeFeatures, Point target) {

        if (gazeFeatures == null || target == null || gazeFeatures.length != NUM_FEATURES) {
            return false;
        }

        int numAtts = dataX.numAttributes();
        double[] instanceX = new double[numAtts];
        double[] instanceY = new double[numAtts];

        for (int i = 0; i < numAtts - 1; i++) {
            instanceX[i] = gazeFeatures[i];
            instanceY[i] = gazeFeatures[i];
        }
        instanceX[numAtts - 1] = target.x;
        instanceY[numAtts - 1] = target.y;

        dataX.add(new DenseInstance(1.0, instanceX));
        dataY.add(new DenseInstance(1.0, instanceY));

        if (dataX.numInstances() > NUM_TRAIN_INSTANCES) {
            try {
                buildClassifier();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
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
