package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Screen;
import com.devnatres.dashproject.DashGame;

/**
 * Helper class to create a new game level. <br>
 *     <br>
 * Created by DevNatres on 04/12/2014.
 */
final public class LevelCreator {
    /**
     * Creates a new level, not always from LevelScreen class.
     * It depends on the specific "levelId".
     */
    public static Screen createLevel(DashGame game, LevelId levelId) {
        return new LevelScreen(game, levelId);
    }

    private LevelCreator() {}
}
