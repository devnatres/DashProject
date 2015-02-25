package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.space.CoordinateInt;
import com.devnatres.dashproject.tools.Tools;

/**
 * Creates PowerUp objects. <br>
 * <br>
 * Created by DevNatres on 21/02/2015.
 */
public class PowerUpGenerator {
    private static final int FAVORABLE_CASES = 3;
    private static final int POSSIBLE_CASES = 3;
    private static final int ZONE_MARGIN = 4;

    public static void generatePowerUpIfLucky(HyperStore hyperStore,
                                              LevelScreen levelScreen,
                                              Vector2 baseCenter) {

        boolean thereIsLuck = Tools.randomBoolean(FAVORABLE_CASES, POSSIBLE_CASES);
        if (!thereIsLuck) return;

        EPowerUpType type = selectRandomType(levelScreen);

        if (type != null) {
            CoordinateInt coordinateInt = selectRandomPosition(levelScreen, baseCenter);
            Vector2 targetCenter = new Vector2();
            levelScreen.getMap().setThisCellCenter(coordinateInt.a, coordinateInt.b, targetCenter);
            PowerUp powerUP = new PowerUp(levelScreen, hyperStore, type, baseCenter, targetCenter);
            levelScreen.register(powerUP, AgentRegistry.EAgentLayer.FLOOR);
        }
    }

    private static EPowerUpType selectRandomType(LevelScreen levelScreen) {
        int index = Tools.randomInt(0, EPowerUpType.powerUpTypes.length-1);
        int initialIndex = index;
        EPowerUpType powerUpType = EPowerUpType.powerUpTypes[index];
        while (!powerUpType.isConvenient(levelScreen)) {
            index++;
            if (index == EPowerUpType.powerUpTypes.length) {
                index = 0;
            }

            // Security check: if there isn't a convenient power-up then don't iterate any more.
            if (initialIndex == index) {
                powerUpType = null;
                break;
            } else {
                powerUpType = EPowerUpType.powerUpTypes[index];
            }
        }

        return powerUpType;
    }

    private static CoordinateInt selectRandomPosition(LevelScreen levelScreen, Vector2 referencePosition) {
        final Hero hero = levelScreen.getHero();
        final LevelMap levelMap = levelScreen.getMap();
        final int centerColumn = levelMap.getColumn(referencePosition.x);
        final int centerRow = levelMap.getRow(referencePosition.y);

        final int iniColumn = Tools.limitInteger(centerColumn - ZONE_MARGIN, 0, levelMap.getMapWidth()-1);
        final int endColumn = Tools.limitInteger(centerColumn + ZONE_MARGIN, 0, levelMap.getMapWidth()-1);
        final int iniRow = Tools.limitInteger(centerRow - ZONE_MARGIN, 0, levelMap.getMapHeight()-1);
        final int endRow = Tools.limitInteger(centerRow + ZONE_MARGIN, 0, levelMap.getMapHeight()-1);

        Vector2 center = new Vector2();
        boolean isLookingForAPosition = true;
        boolean thereIsTargetPosition = false;
        int column = Tools.randomInt(iniColumn, endColumn);
        int row = Tools.randomInt(iniRow, endRow);
        while (isLookingForAPosition) {
            levelMap.setThisCellCenter(column, row, center);
            float distance2 = hero.getCenter().dst2(center);
            if (!levelMap.isBlockCell(column, row) && distance2 > PowerUp.POWER_UP_RADIO2) {
                thereIsTargetPosition = true;
                isLookingForAPosition = false;
            } else {
                column++;
                if (column > endColumn) {
                    column = iniColumn;
                    row++;
                    if (row > endRow) row = iniRow;

                    if (column == iniColumn && row == iniRow) isLookingForAPosition = false;
                }
            }
        }
        return thereIsTargetPosition ? new CoordinateInt(column, row) : null;
    }


    private PowerUpGenerator() {}
}
