package com.devnatres.dashproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelId;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class GameState {

    private final int maxLevelIndex;
    private int currentLevelIndex;

    private final Array<LevelId> levelIds;

    public GameState() {
        levelIds = new Array();

        final Array<String> levelSequence = readLevelSequence();

        maxLevelIndex = levelSequence.size - 1;
        for (int i = 0; i <= maxLevelIndex; i++) {
            levelIds.add(new LevelId(levelSequence.get(i)));
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

    public void notifyCurrentLevelSuccess() {
        if (currentLevelIndex < maxLevelIndex) {
            currentLevelIndex++;
        }
    }

    public LevelId getCurrentLevelId() {
        return levelIds.get(currentLevelIndex);
    }

}
