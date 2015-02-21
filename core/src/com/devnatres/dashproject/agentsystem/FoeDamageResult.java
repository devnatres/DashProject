package com.devnatres.dashproject.agentsystem;

/**
 * Represents the result of an attack. <br>
 * <br>
 * Created by DevNatres on 04/01/2015.
 */
public class FoeDamageResult {
    private int score;
    private boolean deadInCombo;

    public FoeDamageResult clear() {
        score = 0;
        return this;
    }

    public int getScore() {
        return score;
    }

    public boolean isDeadInCombo() {
        return deadInCombo;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDeadInCombo(boolean deadInCombo) {
        this.deadInCombo = deadInCombo;
    }

}
