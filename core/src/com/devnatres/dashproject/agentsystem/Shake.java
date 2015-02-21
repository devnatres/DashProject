package com.devnatres.dashproject.agentsystem;

/**
 * Auxiliary class to manage a shake by providing some displacements to apply to a position.<br>
 * <br>
 * Created by DevNatres on 21/02/2015.
 */
public class Shake {
    private final int displacements[] = new int[]{-1, 1};
    private int index;

    private int oneShakeDuration;
    private int curShakeDuration;
    private int totalDuration;

    public Shake(int totalDuration, int oneShakeDuration) {
        this.totalDuration = totalDuration;
        this.oneShakeDuration = oneShakeDuration;
    }

    public void shake() {
        if (totalDuration > 0) {
            if (curShakeDuration < oneShakeDuration) {
                curShakeDuration++;
            } else {
                curShakeDuration = 0;
                index = (index < displacements.length - 1) ? index + 1 : 0;
            }
            totalDuration--;
        }
    }

    public int getDisplacement() {
        return displacements[index];
    }

    public boolean canStillShake() {
        return totalDuration > 0;
    }
}
