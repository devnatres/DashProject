package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Hero;
import com.devnatres.dashproject.agentsystem.HordeGroup;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.DnaShadowedFont;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.nonagentgraphics.Number;
import com.devnatres.dashproject.nonagentgraphics.Number.ENumberType;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents the current score. <br>
 *     <br>
 * Created by DevNatres on 10/01/2015.
 */
public class Score {
    private static final int Y_MARGIN = 10;
    private static final int X_ACTION_POSITION = 100;
    private static final int X_COUNTING_POSITION = 240;
    private static final int Y_COUNTING_POSITION = 580;
    private static final int X_LINE_POSITION = 100;
    private static final int Y_LINE_POSITION = 600;
    private static final int Y_LINE_ICR = -50;
    private static final int CHAIN_SCORE_DURATION = 90;
    private static final int SCORE_COUNT_PHASE_DURATION = 90;
    private static final int MAX_CHAIN_SCORE_FACTOR = 500;
    private static final int LIFE_SCORE_FACTOR = 200;
    private static final int TIME_SCORE_FACTOR = 250;
    private static final float FULL_CHAIN_SCORE_FACTOR = 0.2f;

    private enum EPhase {
        STAR {
            @Override
            void start(Score score) {
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                Number number = score.getTotalScoreCountingNumber();
                number.setUnitPosition(X_COUNTING_POSITION, Y_COUNTING_POSITION);

                Texture texture = score.getBackgroundTexture();
                preparedBatch.draw(texture,
                        (DashGame.getInstance().getScreenWidth() - texture.getWidth()) / 2,
                        Y_COUNTING_POSITION - texture.getHeight() + number.getDigitHeight()*1.35f);

                number.render(preparedBatch);
            }
        },
        ACTION {
            @Override
            void start(Score score) {
                score.getTotalScoreCountingNumber().sumValue(score.getActionScore());
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                shadowedFont.draw(preparedBatch, score.getActionScoreString(), X_LINE_POSITION, yLine(line));
            }
        },
        TIME {
            @Override
            void start(Score score) {
                score.getTotalScoreCountingNumber().sumValue(score.getTimeScore());
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                shadowedFont.draw(preparedBatch, score.getTimeScoreString(), X_LINE_POSITION, yLine(line));
            }
        },
        LIFE {
            @Override
            void start(Score score) {
                score.getTotalScoreCountingNumber().sumValue(score.getLifeScore());
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                shadowedFont.draw(preparedBatch, score.getLifeScoreString(), X_LINE_POSITION, yLine(line));
            }
        },
        MAX_CHAIN {
            @Override
            void start(Score score) {
                score.getTotalScoreCountingNumber().sumValue(score.getChainScore());
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                shadowedFont.draw(preparedBatch, score.getMaxChainScoreString(), X_LINE_POSITION, yLine(line));
            }
        },
        FULL {
            @Override
            void start(Score score) {
                score.getTotalScoreCountingNumber().sumValue(score.getFullChainScore());
            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                if (score.getFullChainScoreString() != "") {
                    shadowedFont.draw(preparedBatch, score.getFullChainScoreString(), X_LINE_POSITION, yLine(line));
                } else {
                    score.resetScoreCountPhaseDuration();
                }
            }
        },
        TROPHY {
            @Override
            void start(Score score) {

            }

            @Override
            void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont) {
                Texture trophyTexture = score.getTrophyTexture();
                if (trophyTexture != null) {
                    preparedBatch.draw(trophyTexture,
                            DashGame.getInstance().getScreenWidth()/2 + trophyTexture.getWidth(),
                            Y_COUNTING_POSITION-4);
                }
            }
        },
        ;
        abstract void start(Score score);
        abstract void draw(int line, Score score, Batch preparedBatch, DnaShadowedFont shadowedFont);
    }
    private static EPhase[] phases = EPhase.values();
    private static int yLine(int line) {
        return Y_LINE_POSITION + (line * Y_LINE_ICR);
    }

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

    private int timeScore;
    private int lifeScore;
    private int chainScore;
    private int fullChainScore;
    private int totalScore;

    private boolean isFinalCountCalculated;

    private final Number hubActionScoreNumber;
    private final Number totalScoreCountingNumber;

    private final Texture bg_texture;
    private final Texture trophy_a;
    private final Texture trophy_b;
    private final Texture trophy_c;

    private final GameState gameState;
    private final LevelId levelId;

    public Score(LevelScreen levelScreen, HyperStore hyperStore) {
        this.levelScreen = levelScreen;
        youWinMessage = hyperStore.getTexture("message_youwin.png");
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        this.hero = levelScreen.getHero();
        this.hordeGroup = levelScreen.getHordeGroup();

        hubActionScoreNumber = new Number(EAnimMedley.NUMBERS_GOLD.create(hyperStore), ENumberType.INTEGER);
        hubActionScoreNumber.setUnitPosition(X_ACTION_POSITION,
                DashGame.getInstance().getScreenHeight() - hubActionScoreNumber.getDigitHeight() - Y_MARGIN);

        DnaAnimation numberAnimation = EAnimMedley.NUMBERS_GOLD.create(hyperStore);
        totalScoreCountingNumber = new Number(numberAnimation, ENumberType.INTEGER);

        bg_texture = hyperStore.getTexture("bg_scoring.png");
        trophy_a = hyperStore.getTexture("trophies/trophy_a.png");
        trophy_b = hyperStore.getTexture("trophies/trophy_b.png");
        trophy_c = hyperStore.getTexture("trophies/trophy_c.png");

        gameState = DashGame.getInstance().getGameState();
        levelId = gameState.getCurrentLevelId();

        scoreCountPhase = -1;
    }

    public void updateScore() {
        if (lastActionScore != hubActionScoreNumber.getIntValue()) {
            lastActionScore = hubActionScoreNumber.getIntValue();
            actionScoreHubString = String.valueOf(hubActionScoreNumber.getIntValue());
        }
    }

    public void sumFoeScore(int foeScore) {
        hubActionScoreNumber.sumValue(foeScore);
    }

    public void sumChainScore(int chainScore) {
        hubActionScoreNumber.sumValue(chainScore);
        chainScoreString = String.valueOf(chainScore);
        chainScoreDuration = CHAIN_SCORE_DURATION;
    }

    public void renderActionScore(Batch preparedBatch) {
        //font.draw(preparedBatch, actionScoreHubString, 10, screenHeight - 10);
        if (chainScoreDuration > 0) {
            //font.draw(preparedBatch, chainScoreString, screenWidth/2, screenHeight - 100);
            chainScoreDuration--;
        }

        hubActionScoreNumber.render(preparedBatch);
    }

    public void renderFinalCount(Batch preparedBatch, DnaShadowedFont shadowedFont) {
        if (!isFinalCountCalculated) {
            calculateFinalCount();
        }

        preparedBatch.draw(youWinMessage,
                (screenWidth - youWinMessage.getWidth())/2,
                screenHeight - youWinMessage.getHeight()*2);

        updatePhases();

        drawPhases(preparedBatch, shadowedFont);
    }

    private void updatePhases() {
        if (scoreCountPhaseDuration > 0) {
            scoreCountPhaseDuration--;
        }

        if (scoreCountPhaseDuration == 0 && scoreCountPhase < phases.length-1) {
            scoreCountPhase++;
            scoreCountPhaseDuration = SCORE_COUNT_PHASE_DURATION;

            phases[scoreCountPhase].start(this);
        }
    }

    private void drawPhases(Batch preparedBatch, DnaShadowedFont shadowedFont) {
        for (int i = 0, line = 0; i <= scoreCountPhase; i++, line++) {
            phases[i].draw(line, this, preparedBatch, shadowedFont);
        }
    }

    void resetScoreCountPhaseDuration() {
        scoreCountPhaseDuration = 0;
    }

    public void calculateFinalCount() {
        actionScoreString = String.valueOf("Action: " + hubActionScoreNumber.getIntValue());

        totalScore = hubActionScoreNumber.getIntValue();

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
        return hubActionScoreNumber.getIntValue();
    }

    String getActionScoreString() {
        return actionScoreString;
    }

    public int getTimeScore() {
        return timeScore;
    }

    String getTimeScoreString() {
        return timeScoreString;
    }

    public int getLifeScore() {
        return lifeScore;
    }

    String getLifeScoreString() {
        return lifeScoreString;
    }

    public int getChainScore() {
        return chainScore;
    }

    String getMaxChainScoreString() {
        return maxChainScoreString;
    }

    public int getFullChainScore() {
        return fullChainScore;
    }

    String getFullChainScoreString() {
        return fullChainScoreString;
    }

    public int getTotalScore() {
        return totalScore;
    }

    Number getTotalScoreCountingNumber() {
        return totalScoreCountingNumber;
    }

    Texture getBackgroundTexture() {
        return bg_texture;
    }

    Texture getTrophyTexture() {
        if (totalScore >= levelId.getTrophyA()) {
            return trophy_a;
        } else if (totalScore >= levelId.getTrophyB()) {
            return trophy_b;
        } else if (totalScore >= levelId.getTrophyC()) {
            return trophy_c;
        } else {
            return null;
        }
    }
}
