package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.devnatres.dashproject.agents.Hero;
import com.devnatres.dashproject.agents.HordeGroup;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 10/01/2015.
 */
public class Score {
    private static final int MAX_SCORE_COUNT_PHASE = 7;
    private static final int HORDE_COMBO_SCORE_DURATION = 90;
    private static final int SCORE_COUNT_PHASE_DURATION = 90;

    private static final int MAX_HORDE_COMBO_SCORE_FACTOR = 500;
    private static final int LIFE_SCORE_FACTOR = 200;
    private static final int TIME_SCORE_FACTOR = 250;
    private static final float FULL_HORDE_COMBO_SCORE_FACTOR = 1.5f;

    private final Texture youWinMessage;
    private final int screenWidth;
    private final int screenHeight;

    private int scoreCountPhaseDuration;
    private int scoreCountPhase;

    private final LevelScreen levelScreen;
    private final Hero hero;
    private final HordeGroup hordeGroup;

    private int actionScore;
    private int lastActionScore;
    private String actionScoreHubString = "0";
    private String hordeComboScoreString;
    private int hordeComboScoreDuration;

    private String actionScoreString;
    private String maxHordeComboScoreString;
    private String lifeScoreString;
    private String timeScoreString;
    private String fullHordeComboScoreString;
    private String totalScoreString;

    private int totalScore;

    private boolean isFinalCountCalculated;

    public Score(HyperStore hyperStore, LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
        youWinMessage = hyperStore.getTexture("message_youwin.png");
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        this.hero = levelScreen.getHero();
        this.hordeGroup = levelScreen.getHordeGroup();
    }

    public void updateScore() {
        if (lastActionScore != actionScore) {
            lastActionScore = actionScore;
            actionScoreHubString = String.valueOf(actionScore);
        }
    }

    public void sumFoeScore(int foeScore) {
        actionScore += foeScore;
    }

    public void sumHordeComboScore(int hordeComboScore) {
        actionScore += hordeComboScore;
        hordeComboScoreString = String.valueOf(hordeComboScore);
        hordeComboScoreDuration = HORDE_COMBO_SCORE_DURATION;
    }

    public void renderActionScore(Batch preparedBatch, BitmapFont font) {
        font.draw(preparedBatch, actionScoreHubString, 10, screenHeight - 10);
        if (hordeComboScoreDuration > 0) {
            font.draw(preparedBatch, hordeComboScoreString, screenWidth/2, screenHeight - 100);
            hordeComboScoreDuration--;
        }
    }

    public void renderFinalCount(Batch preparedBatch, BitmapFont font) {
        if (!isFinalCountCalculated) {
            calculateFinalCount();
        }

        preparedBatch.draw(youWinMessage,
                (screenWidth - youWinMessage.getWidth())/2,
                screenHeight - youWinMessage.getHeight());

        if (scoreCountPhaseDuration > 0) {
            scoreCountPhaseDuration--;
        }
        if (scoreCountPhaseDuration == 0 && scoreCountPhase <= MAX_SCORE_COUNT_PHASE) {
            scoreCountPhase++;
            scoreCountPhaseDuration = SCORE_COUNT_PHASE_DURATION;
        }

        if (scoreCountPhase > 1) {
            font.draw(preparedBatch, actionScoreString, 50, 600);
        }
        if (scoreCountPhase > 2) {
            font.draw(preparedBatch, maxHordeComboScoreString, 50, 550);
        }
        if (scoreCountPhase > 3) {
            font.draw(preparedBatch, lifeScoreString, 50, 500);
        }
        if (scoreCountPhase > 4) {
            font.draw(preparedBatch, timeScoreString, 50, 450);
        }
        if (scoreCountPhase > 5) {
            if (fullHordeComboScoreString != "") {
                font.draw(preparedBatch, fullHordeComboScoreString, 50, 400);
            } else {
                scoreCountPhaseDuration = 0;
            }
        }
        if (scoreCountPhase > 6) {
            font.draw(preparedBatch, totalScoreString, 50, 350);
        }
    }

    private void calculateFinalCount() {
        actionScoreString = String.valueOf("Action: " + actionScore);
        totalScore = actionScore;

        final int maxHordeCombo = hordeGroup.getMaxConsecutiveHordeComboCount();
        final int maxHordeComboScore = maxHordeCombo * MAX_HORDE_COMBO_SCORE_FACTOR;
        maxHordeComboScoreString = String.valueOf("Max. Horde Combo: "
                + maxHordeCombo
                + " x " + MAX_HORDE_COMBO_SCORE_FACTOR
                + " = " + maxHordeComboScore);
        totalScore += maxHordeComboScore;

        final int life = hero.getLife();
        final int lifeScore = life * LIFE_SCORE_FACTOR;
        lifeScoreString = String.valueOf("Life: "
                + life
                + " x " + LIFE_SCORE_FACTOR
                + " = " + lifeScore);
        totalScore += lifeScore;

        final float time = ((int)(levelScreen.getTime()*10))/10f;
        final int timeScore = (int)(time * TIME_SCORE_FACTOR);
        timeScoreString = String.valueOf("Time: "
                + time
                + " x " + TIME_SCORE_FACTOR
                + " = " + timeScore);
        totalScore += timeScore;

        if (hordeGroup.isFullHordeComboAvailable()) {
            int fullHordeComboScore = (int)(totalScore * FULL_HORDE_COMBO_SCORE_FACTOR);
            fullHordeComboScoreString = "Full Horde Combo: "
                    + totalScore
                    + " x " + FULL_HORDE_COMBO_SCORE_FACTOR
                    + " = " + fullHordeComboScore;
            totalScore += fullHordeComboScore;
        } else {
            fullHordeComboScoreString = "";
        }

        totalScoreString = "TOTAL: " + String.valueOf(totalScore);

        isFinalCountCalculated = true;
    }
}
