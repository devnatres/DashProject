package com.devnatres.dashproject.agents;

/**
 * Created by David on 04/01/2015.
 */
public class FoeDamageResult {
    private Foe foe;
    private int score;
    private boolean deadInCombo;

    public FoeDamageResult clear() {
        foe = null;
        score = 0;
        return this;
    }

    public Foe getFoe() {
        return foe;
    }

    public int getScore() {
        return score;
    }

    public boolean isDeadInCombo() {
        return deadInCombo;
    }

    public void setFoe(Foe foe) {
        this.foe = foe;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDeadInCombo(boolean deadInCombo) {
        this.deadInCombo = deadInCombo;
    }

}
