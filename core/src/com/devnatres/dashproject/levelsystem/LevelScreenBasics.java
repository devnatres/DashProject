package com.devnatres.dashproject.levelsystem;

import com.devnatres.dashproject.levelscriptcmd.LevelScript;

/**
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenBasics {
    LevelId levelId;
    LevelMap map;
    LevelScript levelScript;

    public LevelScreenBasics(LevelScreen levelScreen, LevelId levelId) {
        LevelScreenSet set = levelScreen.getSet();
        this.levelId = levelId;
        map = new LevelMap(levelId.getMapName(), set.mainBatch);
        levelScript = new LevelScript();
    }
}
