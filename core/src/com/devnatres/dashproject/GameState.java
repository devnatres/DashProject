package com.devnatres.dashproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class GameState {

    private final int maxLevelIndex;
    private int currentLevelIndex;

    private final Array<LevelId> levelIds;
    private final Array<Integer> levelRecords;

    private final Preferences preferences;

    private boolean isSoundActivated;

    public GameState() {
        preferences = Gdx.app.getPreferences("com.devnatres.dashproject");
        isSoundActivated = preferences.getBoolean("sound", true);
        updateGlobalSound();

        levelRecords = new Array();
        levelIds = new Array();

        final Array<String> levelSequence = readLevelSequence();

        maxLevelIndex = levelSequence.size - 1;
        for (int i = 0; i <= maxLevelIndex; i++) {
            LevelId levelId = new LevelId(levelSequence.get(i));
            levelIds.add(levelId);

            String value = preferences.getString(levelId.getLevelKey()+"_"+"score", "0");
            levelRecords.add(Integer.parseInt(value));
        }
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

    public LevelId getCurrentLevelId() {
        return levelIds.get(currentLevelIndex);
    }

    public int getCurrentLevelScore() {
        // TODO Avoid generating string each time
        String scoreString = preferences.getString(levelIds.get(currentLevelIndex).getLevelKey()+"_"+"score", "0");
        return Integer.parseInt(scoreString);
    }

    public void updateCurrentLevelScore(int score) {
        // TODO Avoid generating string each time
        preferences.putString(levelIds.get(currentLevelIndex).getLevelKey()+"_"+"score", ""+score);
        preferences.flush();

        if (currentLevelIndex < maxLevelIndex) {
            currentLevelIndex++;
        }
    }

    public void displaceCurrentLevel(int displacement) {
        currentLevelIndex += displacement;
        currentLevelIndex = Tools.limitInteger(currentLevelIndex, 0, maxLevelIndex);
    }

    public void activateSound(boolean isSoundActivated) {
        this.isSoundActivated = isSoundActivated;

        updateGlobalSound();

        preferences.putBoolean("sound", isSoundActivated);
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
}
