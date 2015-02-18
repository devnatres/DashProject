package com.devnatres.dashproject.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.Score;
import com.devnatres.dashproject.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class GameState {
    private static final String PREFERENCES_NAME = "com.devnatres.dashproject";

    private static final String ACTION_SCORE_SUBKEY = "_action_score";
    private static final String TIME_SCORE_SUBKEY = "_time_score";
    private static final String LIFE_SCORE_SUBKEY = "_life_score";
    private static final String CHAIN_SCORE_SUBKEY = "_chain_score";
    private static final String FULLCHAIN_SCORE_SUBKEY = "_fullchain_score";
    private static final String TOTAL_SCORE_SUBKEY = "_total_score";

    private static final String LEVEL_CAMERA_ASSISTANT_SUBKEY = "_camera_assistant";
    private static final String LEVEL_PLAY_COUNT_SUBKEY = "_play_count";
    private static final String TOTAL_PLAY_COUNT_KEY = "total_play_count";

    private static final String CAMERA_ASSISTANT_KEY = "camera_assistant";
    private static final String SOUND_KEY = "sound";
    private static final String COMPLETED_LEVELS_KEY = "completed_levels";

    private static final String TUTORIAL_VISITED_SUBKEY = "_tutorial_visited";

    private static String keyLast(LevelId levelId, String scoreString) {
        return levelId.getLevelKey() + "_last" + scoreString;
    }

    private static String keyBest(LevelId levelId, String scoreString) {
        return levelId.getLevelKey() + "_best" + scoreString;
    }

    private final int maxLevelIndex;
    private int levelIndex;
    private int completedLevels;

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

    private int trophyACount;
    private int trophyBCount;
    private int trophyCCount;

    public GameState() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);

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

        completedLevels = preferences.getInteger(COMPLETED_LEVELS_KEY, 0);
        levelIndex = Tools.limitInteger(completedLevels, 0, maxLevelIndex);

        updateGlobalStatistics();
    }

    private Array<String> readLevelSequence() {
        Array<String> lines = new Array();
        FileHandle file = Gdx.files.internal("maps/level.seq");
        BufferedReader reader = new BufferedReader(file.reader());

        try {
            String line = reader.readLine();
            while (line != null && line.length()>2) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private void updateGlobalStatistics() {
        totalBestScore = 0;
        trophyACount = 0;
        trophyBCount = 0;
        trophyCCount = 0;
        for (int i = 0; i <= maxLevelIndex; i++) {
            totalBestScore += bestTotalScore.get(i);

            int points = bestTotalScore.get(i);
            String trophy = obtainTrophy(i, points);
            if (trophy == "A") {
                trophyACount++;
            } else if (trophy == "B") {
                trophyBCount++;
            } else if (trophy == "C") {
                trophyCCount++;
            }
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

    public int getCompletedLevels() {
        return completedLevels;
    }

    public int getMaxLevels() {
        return maxLevelIndex+1;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getTrophyACount() {
        return trophyACount;
    }

    public int getTrophyBCount() {
        return trophyBCount;
    }

    public int getTrophyCCount() {
        return trophyCCount;
    }

    public int getCurrentLevelTrophyA() {
        return levelIds.get(levelIndex).getTrophyA();
    }

    public int getCurrentLevelTrophyB() {
        return levelIds.get(levelIndex).getTrophyB();
    }

    public int getCurrentLevelTrophyC() {
        return levelIds.get(levelIndex).getTrophyC();
    }

    public String getCurrentLevelLastTrophy() {
        int points = lastTotalScore.get(levelIndex);
        return obtainTrophy(levelIndex, points);
    }

    public String getCurrentLevelBestTrophy() {
        int points = bestTotalScore.get(levelIndex);
        return obtainTrophy(levelIndex, points);
    }

    private String obtainTrophy(int index, int points) {
        if (points >= levelIds.get(index).getTrophyA()) {
            return "A";
        } else if (points >= levelIds.get(index).getTrophyB()) {
            return "B";
        } else if (points >= levelIds.get(index).getTrophyC()) {
            return "C";
        } else {
            return "";
        }
    }

    public void updateCurrentLevelScore(Score score) {
        LevelId levelId = levelIds.get(levelIndex);

        putLevelScore(levelId, ACTION_SCORE_SUBKEY, lastActionScore, bestActionScore, score.getActionScore());
        putLevelScore(levelId, TIME_SCORE_SUBKEY, lastTimeScore, bestTimeScore, score.getTimeScore());
        putLevelScore(levelId, LIFE_SCORE_SUBKEY, lastLifeScore, bestLifeScore, score.getLifeScore());
        putLevelScore(levelId, CHAIN_SCORE_SUBKEY, lastChainScore, bestChainScore, score.getChainScore());
        putLevelScore(levelId, FULLCHAIN_SCORE_SUBKEY, lastFullChainScore, bestFullChainScore, score.getFullChainScore());
        putLevelScore(levelId, TOTAL_SCORE_SUBKEY, lastTotalScore, bestTotalScore, score.getTotalScore());

        updateGlobalStatistics();

        if (levelIndex == completedLevels) {
            completedLevels++;
            preferences.putInteger(COMPLETED_LEVELS_KEY, completedLevels);
        }

        final String levelPlayCountKey = levelId.getLevelKey() + LEVEL_PLAY_COUNT_SUBKEY;
        final int levelPlayCount = preferences.getInteger(levelPlayCountKey, 0);
        preferences.putInteger(levelPlayCountKey, levelPlayCount + 1);

        final int totalPlayCount = preferences.getInteger(TOTAL_PLAY_COUNT_KEY, 0);
        preferences.putInteger(TOTAL_PLAY_COUNT_KEY, totalPlayCount + 1);

        if (levelIndex < maxLevelIndex) {
            levelIndex++;
        }

        preferences.flush();
    }

    private void putLevelScore(LevelId levelId,
                               String scoreSubKey,
                               Array<Integer> lastScoreArray,
                               Array<Integer> bestScoreArray,
                               int newScore) {
        if (newScore > bestScoreArray.get(levelIndex)) {
            bestScoreArray.set(levelIndex, newScore);
            preferences.putInteger(keyBest(levelId, scoreSubKey), newScore);

            if (scoreSubKey == TOTAL_SCORE_SUBKEY) {
                preferences.putBoolean(keyBest(levelId, LEVEL_CAMERA_ASSISTANT_SUBKEY), isCameraAssistantActivated);
            }
        }
        lastScoreArray.set(levelIndex, newScore);
        preferences.putInteger(keyLast(levelId, scoreSubKey), newScore);
    }

    public void displaceCurrentLevel(int displacement) {
        int limit = Tools.min(maxLevelIndex, completedLevels);
        levelIndex += displacement;
        levelIndex = Tools.limitInteger(levelIndex, 0, limit);
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

    public void setTutorialVisited(LevelId levelId) {
        final String tutorialVisitedKey = levelId.getLevelKey() + TUTORIAL_VISITED_SUBKEY;
        preferences.putBoolean(tutorialVisitedKey, true);
        preferences.flush();
    }

    public boolean isTutorialVisited(LevelId levelId) {
        final String tutorialVisitedKey = levelId.getLevelKey() + TUTORIAL_VISITED_SUBKEY;
        return preferences.getBoolean(tutorialVisitedKey, false);
    }

    public void setAllTutorialsUnvisited() {
        for (int i = 0; i <= maxLevelIndex; i++) {
            final String tutorialVisitedKey = levelIds.get(i).getLevelKey() + TUTORIAL_VISITED_SUBKEY;
            preferences.putBoolean(tutorialVisitedKey, false);
        }
        preferences.flush();
    }

}
