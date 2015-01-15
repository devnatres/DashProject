package com.devnatres.dashproject.levelsystem;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class LevelId {
    private final String mapName;
    private final String scriptName;

    public LevelId(String levelName) {
        String[] strings = levelName.split("\\.");
        mapName = "map" + String.format("%03d", Integer.parseInt(strings[0]));
        scriptName = "levelscript" + strings[1];
    }

    public String getMapName() {
        return mapName;
    }

    public String getScriptName() {
        return scriptName;
    }

}
