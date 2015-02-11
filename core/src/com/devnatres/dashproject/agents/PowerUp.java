package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.space.CoordinateInt;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.Tools;


/**
 * Created by DevNatres on 10/02/2015.
 */
public class PowerUp extends Agent {

    private static final int FAVORABLE_CASES = 1;
    private static final int POSSIBLE_CASES = 3;

    private static final int LATERAL_MARGIN = 4;

    private enum EPowerUpType {
        TIME {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return true;
            }
        },
        LIFE {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasMaxLife();
            }
        },
        DASH {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasExtraDash();
            }
        },
        IMMUNITY {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasImmunity();
            }
        };
        abstract boolean isConvenient(LevelScreen levelScreen);
    }
    private static EPowerUpType[] powerUpTypes = EPowerUpType.values();

    public static void generatePowerUpIfLucky(HyperStore hyperStore,
                                              LevelScreen levelScreen,
                                              Vector2 referencePosition) {

        boolean thereIsLuck = Tools.randomBoolean(FAVORABLE_CASES, POSSIBLE_CASES);
        if (!thereIsLuck) return;

        EPowerUpType type = selectType(levelScreen);

        if (type != null) {
            CoordinateInt coordinateInt = selectPosition(levelScreen, referencePosition);
        }

        // TODO: column row (if thereIsTargetPosition) is the place
        // TODO: register in levelScreen
    }

    private static EPowerUpType selectType(LevelScreen levelScreen) {
        int index = Tools.randomInt(0, powerUpTypes.length-1);
        int initialIndex = index;
        EPowerUpType powerUpType = powerUpTypes[index];
        while (!powerUpType.isConvenient(levelScreen)) {
            index++;
            if (index == powerUpTypes.length) {
                index = 0;
            }

            // Security check: if there isn't a valid power-up then do not iterate any more.
            if (initialIndex == index) {
                powerUpType = null;
                break;
            } else {
                powerUpType = powerUpTypes[index];
            }
        }

        return powerUpType;
    }

    private static CoordinateInt selectPosition(LevelScreen levelScreen, Vector2 referencePosition) {
        Hero hero = levelScreen.getHero();

        LevelMap levelMap = levelScreen.getMap();
        final int centerColumn = levelMap.getColumn(referencePosition.x);
        final int centerRow = levelMap.getRow(referencePosition.y);

        int iniColumn = centerColumn - LATERAL_MARGIN;
        if (iniColumn < 0) iniColumn = 0;

        int endColumn = centerColumn + LATERAL_MARGIN;
        if (endColumn >= levelMap.getMapWidth()) endColumn = levelMap.getMapWidth()-1;

        int iniRow = centerRow - LATERAL_MARGIN;
        if (iniRow < 0) iniRow = 0;

        int endRow = centerRow + LATERAL_MARGIN;
        if (endRow >= levelMap.getMapHeight()) endRow = levelMap.getMapHeight()-1;

        boolean thereIsTargetPosition = false;
        int column = iniColumn;
        int row = iniRow;
        while (!thereIsTargetPosition && column <= endColumn) {
            while (!thereIsTargetPosition && row <= endRow) {
                if (!levelMap.isBlockCell(column, row) && !hero.isOnCell(column, row)) {
                    thereIsTargetPosition = true;
                } else {
                    row++;
                }
            }
            if (!thereIsTargetPosition) column++;
        }

        return thereIsTargetPosition ? new CoordinateInt(column, row) : null;
    }

    private PowerUp(Animation animation) {
        super(animation);
    }
}
