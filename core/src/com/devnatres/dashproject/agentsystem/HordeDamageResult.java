package com.devnatres.dashproject.agentsystem;

/**
 * Represents the result of an attack to a horde. <br>
 * <br>
 * Created by DevNatres on 05/01/2015.
 */
public class HordeDamageResult {
    private int foeScore;
    private int comboScore;
    private int deadInComboCount;
    private boolean isChain;

    public void sumFoeScore(int score) {
        foeScore += score;
    }

    public void sumDeadInCombo() {
        deadInComboCount++;
    }

    public void markHordeCombo() {
        isChain = true;
    }

    public void setComboScore(int score) {
        comboScore = score;
    }

    public boolean isDeadInChain() {
        return isChain;
    }

    public int getFoeScore() {
        return foeScore;
    }

    public int getDeadInComboCount() {
        return deadInComboCount;
    }

    public int getComboScore() {
        return comboScore;
    }
}
