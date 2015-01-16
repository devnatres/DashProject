package com.devnatres.dashproject.levelsystem;

/**
 * Created by DevNatres on 15/01/2015.
 */
public class LevelId {
    private static final int LEVEL_INDEX = 0;
    private static final int TYPE_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final int TROPHY_A_INDEX = 3;
    private static final int TROPHY_B_INDEX = 4;
    private static final int TROPHY_C_INDEX = 5;
    private static final int NAME_INDEX = 6;

    private final String mapName;
    private final String scriptName;
    private final String type;
    private final float initialTime;
    private final int trophyA;
    private final int trophyB;
    private final int trophyC;
    private final String name;

    public LevelId(String levelString) {
        final String[] fragments = levelString.split(",");

        final String[] mapScriptFragment = fragments[LEVEL_INDEX].split("\\.");
        mapName = "map" + String.format("%03d", Integer.parseInt(mapScriptFragment[0]));
        scriptName = "levelscript" + Integer.parseInt(mapScriptFragment[1]);

        type = fragments[TYPE_INDEX];
        initialTime = Float.parseFloat(fragments[TIME_INDEX]);
        trophyA = Integer.parseInt(fragments[TROPHY_A_INDEX]);
        trophyB = Integer.parseInt(fragments[TROPHY_B_INDEX]);
        trophyC = Integer.parseInt(fragments[TROPHY_C_INDEX]);
        name = fragments[NAME_INDEX];
    }

    public String getMapName() {
        return mapName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public String getLevelName() {
        return name;
    }

}
