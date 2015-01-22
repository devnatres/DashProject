package com.devnatres.dashproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.Score;
import com.devnatres.dashproject.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class GameState {
    private static final String ACTION_SCORE_SUBKEY = "_action_score";
    private static final String TIME_SCORE_SUBKEY = "_time_score";
    private static final String LIFE_SCORE_SUBKEY = "_life_score";
    private static final String CHAIN_SCORE_SUBKEY = "_chain_score";
    private static final String FULLCHAIN_SCORE_SUBKEY = "_fullchain_score";
    private static final String TOTAL_SCORE_SUBKEY = "_total_score";

    private static final String CAMERA_ASSISTANT_KEY = "camera_assistant";
    private static final String SOUND_KEY = "sound";

    private static String keyLast(LevelId levelId, String scoreString) {
        return levelId.getLevelKey() + "_last" + scoreString;
    }

    private static String keyBest(LevelId levelId, String scoreString) {
        return levelId.getLevelKey() + "_best" + scoreString;
    }

    private final int maxLevelIndex;
    private int levelIndex;

    private final Array<LevelId> levelIds = new Array();
    
    private final Array<Integer> lastActionScore = new Array();
    private final Array<Integer> lastTimeScore = new Array();
    private final Array<Integer> lastLifeScore = new Array();
    private final Array<Integer> lastChainScore = new Array();
    private final Array<Integer> lastFullChainScore = new Array();
    private final Array<Integer> lastTotalScore = new Array();

    private final Array<Integer> bestActionScore = new Array();
    private final Array<Integer> bestTimeScore = new Array();
    private final Array<Integer> bestLifeScore = new Array();
    private final Array<Integer> bestChainScore = new Array();
    private final Array<Integer> bestFullChainScore = new Array();
    private final Array<Integer> bestTotalScore = new Array();

    private int totalBestScore;

    private final Preferences preferences;

    private boolean isSoundActivated;
    private boolean isCameraAssistantActivated;

    public GameState() {
        preferences = Gdx.app.getPreferences("com.devnatres.dashproject");

        isSoundActivated = preferences.getBoolean(SOUND_KEY, true);
        updateGlobalSound();

        isCameraAssistantActivated = preferences.getBoolean(CAMERA_ASSISTANT_KEY, true);

        final Array<String> levelSequence = readLevelSequence();

        maxLevelIndex = levelSequence.size - 1;
        for (int i = 0; i <= maxLevelIndex; i++) {
            LevelId levelId = new LevelId(levelSequence.get(i));
            levelIds.add(levelId);

            lastActionScore.add(preferences.getInteger(keyLast(levelId, ACTION_SCORE_SUBKEY), 0));
            lastTimeScore.add(preferences.getInteger(keyLast(levelId, TIME_SCORE_SUBKEY), 0));
            lastLifeScore.add(preferences.getInteger(keyLast(levelId, LIFE_SCORE_SUBKEY), 0));
            lastChainScore.add(preferences.getInteger(keyLast(levelId, CHAIN_SCORE_SUBKEY), 0));
            lastFullChainScore.add(preferences.getInteger(keyLast(levelId, FULLCHAIN_SCORE_SUBKEY), 0));
            lastTotalScore.add(preferences.getInteger(keyLast(levelId, TOTAL_SCORE_SUBKEY), 0));

            bestActionScore.add(preferences.getInteger(keyBest(levelId, ACTION_SCORE_SUBKEY), 0));
            bestTimeScore.add(preferences.getInteger(keyBest(levelId, TIME_SCORE_SUBKEY), 0));
            bestLifeScore.add(preferences.getInteger(keyBest(levelId, LIFE_SCORE_SUBKEY), 0));
            bestChainScore.add(preferences.getInteger(keyBest(levelId, CHAIN_SCORE_SUBKEY), 0));
            bestFullChainScore.add(preferences.getInteger(keyBest(levelId, FULLCHAIN_SCORE_SUBKEY), 0));
            bestTotalScore.add(preferences.getInteger(keyBest(levelId, TOTAL_SCORE_SUBKEY), 0));
        }

        updateTotalBestScore();
    }

    private Array<String> readLevelSequence() {
        Array<String> lines = new Array();
        FileHandle file = Gdx.files.internal("maps/level.seq");
        BufferedReader reader = new BufferedReader(file.reader());

        try {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private void updateTotalBestScore() {
        totalBestScore = 0;
        for (int i = 0; i <= maxLevelIndex; i++) {
            totalBestScore += bestTotalScore.get(i);
        }
    }

    public LevelId getCurrentLevelId() {
        return levelIds.get(levelIndex);
    }

    public int getCurrentLevelLastActionScore() {
        return lastActionScore.get(levelIndex);
    }

    public int getCurrentLevelLastTimeScore() {
        return lastTimeScore.get(levelIndex);
    }

    public int getCurrentLevelLastLifeScore() {
        return lastLifeScore.get(levelIndex);
    }

    public int getCurrentLevelLastChainScore() {
        return lastChainScore.get(levelIndex);
    }

    public int getCurrentLevelLastFullChainScore() {
        return lastFullChainScore.get(levelIndex);
    }

    public int getCurrentLevelLastTotalScore() {
        return lastTotalScore.get(levelIndex);
    }

    public int getCurrentLevelBestActionScore() {
        return bestActionScore.get(levelIndex);
    }

    public int getCurrentLevelBestTimeScore() {
        return bestTimeScore.get(levelIndex);
    }

    public int getCurrentLevelBestLifeScore() {
        return bestLifeScore.get(levelIndex);
    }

    public int getCurrentLevelBestChainScore() {
        return bestChainScore.get(levelIndex);
    }

    public int getCurrentLevelBestFullChainScore() {
        return bestFullChainScore.get(levelIndex);
    }

    public int getCurrentLevelBestTotalScore() {
        return bestTotalScore.get(levelIndex);
    }

    public int getTotalBestScore() {
        return totalBestScore;
    }

    public void updateCurrentLevelScore(Score score) {
        LevelId levelId = levelIds.get(levelIndex);

        putScore(levelId, ACTION_SCORE_SUBKEY, lastActionScore, bestActionScore, score.getActionScore());
        putScore(levelId, TIME_SCORE_SUBKEY, lastTimeScore, bestTimeScore, score.getTimeScore());
        putScore(levelId, LIFE_SCORE_SUBKEY, lastLifeScore, bestLifeScore, score.getLifeScore());
        putScore(levelId, CHAIN_SCORE_SUBKEY, lastChainScore, bestChainScore, score.getChainScore());
        putScore(levelId, FULLCHAIN_SCORE_SUBKEY, lastFullChainScore, bestFullChainScore, score.getFullChainScore());
        putScore(levelId, TOTAL_SCORE_SUBKEY, lastTotalScore, bestTotalScore, score.getTotalScore());
        preferences.flush();

        updateTotalBestScore();

        if (levelIndex < maxLevelIndex) {
            levelIndex++;
        }
    }

    private void putScore(LevelId levelId,
                          String scoreSubKey,
                          Array<Integer> lastScoreArray,
                          Array<Integer> bestScoreArray,
                          int newScore) {
        if (newScore > bestScoreArray.get(levelIndex)) {
            bestScoreArray.set(levelIndex, newScore);
            preferences.putInteger(keyBest(levelId, scoreSubKey), newScore);
        }
        lastScoreArray.set(levelIndex, newScore);
        preferences.putInteger(keyLast(levelId, scoreSubKey), newScore);
    }

    public void displaceCurrentLevel(int displacement) {
        levelIndex += displacement;
        levelIndex = Tools.limitInteger(levelIndex, 0, maxLevelIndex);
    }

    public void activateSound(boolean isSoundActivated) {
        this.isSoundActivated = isSoundActivated;

        updateGlobalSound();

        preferences.putBoolean(SOUND_KEY, isSoundActivated);
        preferences.flush();
    }

    private void updateGlobalSound() {
        if (isSoundActivated) {
            GlobalAudio.enableAudio();
        } else {
            GlobalAudio.disableAudio();
        }
    }

    public boolean isSoundActivated() {
        return isSoundActivated;
    }

    public void activateCameraAssistant(boolean isCameraAssistantActivated) {
        this.isCameraAssistantActivated = isCameraAssistantActivated;

        preferences.putBoolean(CAMERA_ASSISTANT_KEY, isCameraAssistantActivated);
        preferences.flush();
    }

    public boolean isCameraAssistantActivated() {
        return isCameraAssistantActivated;
    }
}