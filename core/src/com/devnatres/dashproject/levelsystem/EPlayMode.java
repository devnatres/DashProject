package com.devnatres.dashproject.levelsystem;

/**
 * Created by DevNatres on 10/01/2015.
 */
public enum EPlayMode {
    GAME_PLAY {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_GamePlay();
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
    PAUSE {
        @Override
        public void render(LevelScreen levelScreen) {
            levelScreen.renderPlayMode_Pause();
        }
    }
    ;

    abstract public void render(LevelScreen levelScreen);
}