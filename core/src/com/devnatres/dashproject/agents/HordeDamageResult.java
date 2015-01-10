package com.devnatres.dashproject.agents;

/**
 * Created by DevNatres on 05/01/2015.
 */
public class HordeDamageResult {
    private int foeScore;
    private int comboScore;
    private int deadInComboFoeCount;
    private boolean isHordeCombo;

    public void sumFoeScore(int score) {
        foeScore += score;
    }

    public void sumDeadInComboFoe() {
        deadInComboFoeCount++;
    }

    public void markHordeCombo() {
        isHordeCombo = true;
    }

    public void setComboScore(int score) {
        comboScore = score;
    }

    public boolean isDeadInHordeCombo() {
        return isHordeCombo;
    }

    public int getFoeScore() {
        return foeScore;
    }

    public int getDeadInComboFoeCount() {
        return deadInComboFoeCount;
    }

    public int getComboScore() {
        return comboScore;
    }
}
