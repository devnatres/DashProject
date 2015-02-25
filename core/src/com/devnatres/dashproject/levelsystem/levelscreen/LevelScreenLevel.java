package com.devnatres.dashproject.levelsystem.levelscreen;

import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.LevelMap;

/**
 * Auxiliary structure for the level basics of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenLevel {
    LevelId levelId;
    LevelMap map;
    LevelScript levelScript;

    public LevelScreenLevel(LevelScreenSet set, LevelId levelId) {
        this.levelId = levelId;
        map = new LevelMap(levelId.getMapName(), set.mainBatch);
        levelScript = new LevelScript();
    }
}
