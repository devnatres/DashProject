package com.devnatres.dashproject.debug;

import com.devnatres.dashproject.gameconstants.Time;

/**
 * Created by DevNatres on 01/03/2015.
 */
abstract public class DebugFPS {
    static int lastFrameCount;
    static int currentFrameCount;
    static float initialFrameTime;
    static float currentFrameTime;
    static float[] lastFrameCounts;
    static int lastFrameCountsIndex;
    static float lastAvgFps;
    static float minFps;
    static float maxFps;

    static void start() {
        lastFrameCount = 0;
        currentFrameCount = 0;
        initialFrameTime = System.nanoTime();
        currentFrameTime = 0;
        lastFrameCounts = new float[5]; // Last seconds to consider for average fps.
        lastFrameCountsIndex = 0;
        lastAvgFps = 0;
        minFps = 0;
        maxFps = 0;
    }

    static void update() {
        currentFrameCount++;
        currentFrameTime = System.nanoTime();
        float deltaFrameTime = (currentFrameTime - initialFrameTime) / Time.NANO_TIME;
        if (deltaFrameTime >= 1f) {
            initialFrameTime = currentFrameTime;
            lastFrameCount = currentFrameCount;
            currentFrameCount = 0;

            lastFrameCounts[lastFrameCountsIndex] = lastFrameCount;
            lastFrameCountsIndex++;
            if (lastFrameCountsIndex == lastFrameCounts.length) lastFrameCountsIndex = 0;

            minFps = 0;
            maxFps = 0;
            lastAvgFps = 0;
            for (int i = 0; i < lastFrameCounts.length; i++) {
                lastAvgFps += lastFrameCounts[i];
                if (lastFrameCounts[i] > 0) {
                    if (minFps == 0 || lastFrameCounts[i] < minFps) {
                        minFps = lastFrameCounts[i];
                    }

                    if (maxFps == 0 || lastFrameCounts[i] > maxFps) {
                        maxFps = lastFrameCounts[i];
                    }
                }
            }
            lastAvgFps /= lastFrameCounts.length;
            lastAvgFps = (int)(lastAvgFps * 10f) / 10f;
        }
    }

    private DebugFPS(){}
}
