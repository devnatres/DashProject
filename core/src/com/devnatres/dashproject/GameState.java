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
    private enum ELevelKeys {
        LAST_TOTAL {
            @Override
            String key(LevelId levelId) {
                return levelId.getLevelKey() + "_last_score";
            }
        },
        BEST_TOTAL {
            @Override
            String key(LevelId levelId) {
                return levelId.getLevelKey() + "_best_score";
            }
        },
        ;
        abstract String key(LevelId levelId);
    }

    private enum EOptionKeys {
        CAMERA_ASSISTANT {
            @Override
            String key() {
                return "camera_assistant";
            }
        },
        SOUND {
            @Override
            String key() {
                return "sound";
            }
        },
        ;
        abstract String key();
    }

    private final int maxLevelIndex;
    private int currentLevelIndex;

    private final Array<LevelId> levelIds;
    private final Array<Integer> levelRecords;

    private final Preferences preferences;

    private boolean isSoundActivated;
    private boolean isCameraAssistantActivated;

    public GameState() {
        preferences = Gdx.app.getPreferences("com.devnatres.dashproject");

        isSoundActivated = preferences.getBoolean(EOptionKeys.SOUND.key(), true);
        updateGlobalSound();

        isCameraAssistantActivated = preferences.getBoolean(EOptionKeys.CAMERA_ASSISTANT.key(), true);

        levelRecords = new Array();
        levelIds = new Array();

        final Array<String> levelSequence = readLevelSequence();

        maxLevelIndex = levelSequence.size - 1;
        for (int i = 0; i <= maxLevelIndex; i++) {
            LevelId levelId = new LevelId(levelSequence.get(i));
            levelIds.add(levelId);

            String value = preferences.getString(ELevelKeys.BEST_TOTAL.key(levelId), "0");
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

    public int getCurrentLevelLastScore() {
        LevelId levelId = levelIds.get(currentLevelIndex);
        String scoreString = preferences.getString(ELevelKeys.LAST_TOTAL.key(levelId), "0");
        return Integer.parseInt(scoreString);
    }

    public void updateCurrentLevelScore(int score) {
        LevelId levelId = levelIds.get(currentLevelIndex);

        String bestScoreString = preferences.getString(ELevelKeys.BEST_TOTAL.key(levelId), "0");
        int bestScore = Integer.parseInt(bestScoreString);

        preferences.putString(ELevelKeys.LAST_TOTAL.key(levelId), ""+score);
        if (score > bestScore) {
            preferences.putString(ELevelKeys.BEST_TOTAL.key(levelId), ""+score);
        }
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

        preferences.putBoolean(EOptionKeys.SOUND.key(), isSoundActivated);
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

        preferences.putBoolean(EOptionKeys.CAMERA_ASSISTANT.key(), isCameraAssistantActivated);
        preferences.flush();
    }

    public boolean isCameraAssistantActivated() {
        return isCameraAssistantActivated;
    }
}
