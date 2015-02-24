package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.agentsystem.Foe;
import com.devnatres.dashproject.agentsystem.HordeGroup;

/**
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenEnemy {
    final HordeGroup hordeGroup;
    int maxHordeCount;
    int lastHordeCount;
    int currentHordeCount;
    String currentHordeCountString;

    Array<Foe> comboLivingFoes = new Array();
    Foe firstComboFoe;
    Foe lastDeadFoe;

    public LevelScreenEnemy(LevelScreen levelScreen) {
        hordeGroup = new HordeGroup(levelScreen);

        LevelMap map = levelScreen.getBasics().map;
        LevelScreenSet set = levelScreen.getSet();
        LevelId levelId = levelScreen.getLevelId();
        maxHordeCount = map.extractLevelScript(levelScreen, set.hyperStore, levelId.getScriptName());

        lastHordeCount = maxHordeCount;
        currentHordeCount = maxHordeCount;
        currentHordeCountString = String.valueOf(currentHordeCount);
    }
}
