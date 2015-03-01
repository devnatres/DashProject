package com.devnatres.dashproject.debug;

import com.devnatres.dashproject.gameconstants.Time;

/**
 * Created by DevNatres on 01/03/2015.
 */
abstract public class DebugFPS {
    static float measuredFPS;
    static int frameCount;
    static float initialFrameTime;
    static float currentFrameTime;
    static float[] measuredFpsList;
    static int measuredFpsListIndex;
    static float avgFps;
    static float minFps;
    static float maxFps;

    static void start() {
        measuredFPS = 0;
        frameCount = 0;
        initialFrameTime = System.nanoTime();
        currentFrameTime = 0;
        measuredFpsList = new float[5]; // Measurements for statistics.
        measuredFpsListIndex = 0;
        avgFps = 0;
        minFps = 0;
        maxFps = 0;
    }

    static void update() {
        frameCount++;
        currentFrameTime = System.nanoTime();
        float deltaFrameTime = (currentFrameTime - initialFrameTime) / Time.NANO_TIME;
        if (deltaFrameTime >= 1f) {
            initialFrameTime = currentFrameTime;
            measuredFPS = (int)(frameCount / deltaFrameTime * 10f) / 10f;
            frameCount = 0;
            measuredFpsList[measuredFpsListIndex] = measuredFPS;
            measuredFpsListIndex++;
            if (measuredFpsListIndex == measuredFpsList.length) {
                measuredFpsListIndex = 0;
            }

            minFps = 0;
            maxFps = 0;
            avgFps = 0;
            for (int i = 0; i < measuredFpsList.length; i++) {
                avgFps += measuredFpsList[i];
                if (measuredFpsList[i] > 0) {
                    if (minFps == 0 || measuredFpsList[i] < minFps) {
                        minFps = measuredFpsList[i];
                    }

                    if (maxFps == 0 || measuredFpsList[i] > maxFps) {
                        maxFps = measuredFpsList[i];
                    }
                }
            }
            avgFps /= measuredFpsList.length;
            avgFps = (int)(avgFps * 10f) / 10f;
        }
    }

    private DebugFPS(){}
}
