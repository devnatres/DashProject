package com.devnatres.dashproject.levelsystem.levelscreen;

import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.scroll.Scroll;

/**
 * Auxiliary structure for the level basics of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenLevel {
    final LevelId levelId;
    final LevelMap map;
    final LevelScript levelScript;
    Scroll backScroll;
    Scroll foreScroll;

    public LevelScreenLevel(LevelScreenSet set, LevelId levelId) {
        this.levelId = levelId;
        map = new LevelMap(levelId.getMapName(), set.mainBatch);
        levelScript = new LevelScript();
    }
}
