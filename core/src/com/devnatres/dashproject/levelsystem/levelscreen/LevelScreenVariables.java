package com.devnatres.dashproject.levelsystem.levelscreen;

/**
 * Auxiliary structure for the in-game variables of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
public class LevelScreenVariables {
    float bulletTime;
    private float time;
    private String timeString;
    int waitingTime;
    boolean comboCameraChasing;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
        timeString = String.valueOf(((int)(time *10))/10f);
    }

    public String getTimeString() {
        return timeString;
    }
}
