package com.devnatres.dashproject.levelsystem;

/**
 * Created by DevNatres on 10/01/2015.
 */
public enum EPlayMode {
    GAME_PLAY {
        @Override
        public void properUpdate(LevelScreen levelScreen) {
            levelScreen.playModeUpdate_GamePlay();
        }
        @Override
        public void properDraw(LevelScreen levelScreen) {
        }
    },
    HERO_DEAD {
        @Override
        public void properUpdate(LevelScreen levelScreen) {
            levelScreen.playModeUpdate_HeroDead();
        }
        @Override
        public void properDraw(LevelScreen levelScreen) {
            levelScreen.playModeDraw_HeroDead();
        }
    },
    SCORE_COUNT {
        @Override
        public void properUpdate(LevelScreen levelScreen) {
            levelScreen.playModeUpdate_ScoreCount();
        }
        @Override
        public void properDraw(LevelScreen levelScreen) {
            levelScreen.playModeDraw_ScoreCount();
        }
    },
    ;

    abstract public void properUpdate(LevelScreen levelScreen);
    abstract public void properDraw(LevelScreen levelScreen);
}