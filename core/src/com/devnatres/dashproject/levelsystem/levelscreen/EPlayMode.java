package com.devnatres.dashproject.levelsystem.levelscreen;

/**
 * This auxiliary enum helps LevelScreen to switch the proper method according to the play mode.<br>
 *     <br>
 * Created by DevNatres on 10/01/2015.
 */
public enum EPlayMode {
    GAME_PLAY {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_GamePlay();
        }
    },
    READY {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_Ready();
        }
    },
    HERO_DEAD {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_HeroDead();
        }
    },
    TIME_OUT {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_TimeOut();
        }
    },
    SCORE_COUNT {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_ScoreCount();
        }
    },
    MENU {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_Menu();
        }
    }
    ;

    abstract public void render(LevelScreen levelScreen);
}