package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Screen;
import com.devnatres.dashproject.DashGame;

/**
 * Created by DevNatres on 04/12/2014.
 */
abstract public class LevelCreator {

    /**
     * Creates a new level, not always from LevelScreen class. It depends on "levelName".
     *
     */
    public static Screen createLevel(DashGame game, String levelName) {
        return new LevelScreen(game, levelName);
    }

    private LevelCreator() {}
}
