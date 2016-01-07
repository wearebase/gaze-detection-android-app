package org.draxus.clm;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GazeDetection {
    private static final String TAG = "GazeDetection";

    public String runDetection(@NonNull final String path, Context context) {
        try {
            // Executes the command.
            //Log.d(TAG, "Folder = " + mContext.getFilesDir().getPath());
            Log.d(TAG, "Running FeatureExtraction");
            File nativeFolder = context.getDir("nativeFolder", Context.MODE_PRIVATE);
            File archFolder = new File(nativeFolder, "armeabi-v7a");
            File fileWithinMyDir = new File(archFolder, "FeatureExtraction");
            if (!fileWithinMyDir.exists()) {
                return "FeatureExtraction doesn't exist";
            }
            if (!fileWithinMyDir.canExecute()) {
                return "FeatureExtraction is not executable";
            }

            File videoFile = new File(archFolder, "video30fps.mp4");

            ProcessBuilder processBuilder = new ProcessBuilder(
                    fileWithinMyDir.getAbsolutePath(),
                    "-rigid",
                    "-q",
                    "-verbose",
                    "-fdir",
                    Environment.getExternalStorageDirectory().getPath() + "/DCIM/Dominika/",
                    "-asvid",
                    //"-f",
                    //Environment.getExternalStorageDirectory().getPath() + "/DCIM/video30fps.mp4",
                    "-ogaze",
                    Environment.getExternalStorageDirectory().getPath() + "/DCIM/features.txt");
            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line + "\n");
            }

            BufferedReader bufferedErrorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String errorLine;
            while ((errorLine = bufferedErrorReader.readLine()) != null) {
                log.append(errorLine + "\n");
            }

            // Waits for the command to finish.
            process.waitFor();

            return log.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
