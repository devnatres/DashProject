package com.devnatres.dashproject.agentsystem;

/**
 * Created by DevNatres on 21/02/2015.
 */
public class Shake {
    private final int displacements[] = new int[]{-1, 1};
    private int index;

    private int maxFrameDuration;
    private int curFrameDuration;

    private int maxTotalDuration;
    private int curTotalDuration;

    public Shake(int totalDuration, int frameDuration) {
        this.maxTotalDuration = totalDuration;
        this.maxFrameDuration = frameDuration;
    }

    public void shake() {
        if (curFrameDuration < maxFrameDuration) {
            curFrameDuration++;
        } else {
            curFrameDuration = 0;
            index = (index < displacements.length - 1) ? index + 1 : 0;
        }
        curTotalDuration++;
    }

    public int getDisplacement() {
        return displacements[index];
    }

    public boolean canStillShake() {
        return curTotalDuration < maxTotalDuration;
    }
}
