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
    private static final int CHAIN_SCORE_DURATION = 90;
    private static final int SCORE_COUNT_PHASE_DURATION = 90;

    private static final int MAX_CHAIN_SCORE_FACTOR = 500;
    private static final int LIFE_SCORE_FACTOR = 200;
    private static final int TIME_SCORE_FACTOR = 250;
    private static final float FULL_CHAIN_SCORE_FACTOR = 0.2f;

    private final Texture youWinMessage;
    private final int screenWidth;
    private final int screenHeight;

    private int scoreCountPhaseDuration;
    private int scoreCountPhase;

    private final LevelScreen levelScreen;
    private final Hero hero;
    private final HordeGroup hordeGroup;

    private int lastActionScore;
    private String actionScoreHubString = "0";
    private String chainScoreString;
    private int chainScoreDuration;

    private String actionScoreString;
    private String maxChainScoreString;
    private String lifeScoreString;
    private String timeScoreString;
    private String fullChainScoreString;
    private String totalScoreString;

    private int actionScore;
    private int timeScore;
    private int lifeScore;
    private int chainScore;
    private int fullChainScore;
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

    public void sumChainScore(int chainScore) {
        actionScore += chainScore;
        chainScoreString = String.valueOf(chainScore);
        chainScoreDuration = CHAIN_SCORE_DURATION;
    }

    public void renderActionScore(Batch preparedBatch, BitmapFont font) {
        font.draw(preparedBatch, actionScoreHubString, 10, screenHeight - 10);
        if (chainScoreDuration > 0) {
            font.draw(preparedBatch, chainScoreString, screenWidth/2, screenHeight - 100);
            chainScoreDuration--;
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
            font.draw(preparedBatch, timeScoreString, 50, 550);

        }
        if (scoreCountPhase > 3) {
            font.draw(preparedBatch, lifeScoreString, 50, 500);
        }
        if (scoreCountPhase > 4) {
            font.draw(preparedBatch, maxChainScoreString, 50, 450);
        }
        if (scoreCountPhase > 5) {
            if (fullChainScoreString != "") {
                font.draw(preparedBatch, fullChainScoreString, 50, 400);
            } else {
                scoreCountPhaseDuration = 0;
            }
        }
        if (scoreCountPhase > 6) {
            font.draw(preparedBatch, totalScoreString, 50, 350);
        }
    }

    public void calculateFinalCount() {
        actionScoreString = String.valueOf("Action: " + actionScore);
        totalScore = actionScore;

        final float time = ((int)(levelScreen.getTime()*10))/10f;
        timeScore = (int)(time * TIME_SCORE_FACTOR);
        timeScoreString = String.valueOf("Time: "
                + time
                + " x " + TIME_SCORE_FACTOR
                + " = " + timeScore);
        totalScore += timeScore;

        final int life = hero.getLife();
        lifeScore = life * LIFE_SCORE_FACTOR;
        lifeScoreString = String.valueOf("Life: "
                + life
                + " x " + LIFE_SCORE_FACTOR
                + " = " + lifeScore);
        totalScore += lifeScore;

        final int maxChain = hordeGroup.getMaxConsecutiveChainCount();
        chainScore = maxChain * MAX_CHAIN_SCORE_FACTOR;
        maxChainScoreString = String.valueOf("Max.Chain: "
                + maxChain
                + " x " + MAX_CHAIN_SCORE_FACTOR
                + " = " + chainScore);
        totalScore += chainScore;

        boolean isFullChain = hordeGroup.isFullChainAvailable();
        if (isFullChain) {
            fullChainScore = (int)(totalScore * FULL_CHAIN_SCORE_FACTOR);
            fullChainScoreString = "Full Chain: "
                    + " + " + FULL_CHAIN_SCORE_FACTOR *100 + "%"
                    + " = " + fullChainScore;
            totalScore += fullChainScore;
        } else {
            fullChainScore = 0;
            fullChainScoreString = "Full Chain: Not achieved";
        }

        totalScoreString = "TOTAL: " + String.valueOf(totalScore);

        isFinalCountCalculated = true;
    }

    public int getActionScore() {
        return actionScore;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public int getLifeScore() {
        return lifeScore;
    }

    public int getChainScore() {
        return chainScore;
    }

    public int getFullChainScore() {
        return fullChainScore;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
