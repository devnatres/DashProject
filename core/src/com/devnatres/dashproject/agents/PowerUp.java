package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.Tools;

/**
 * Created by DevNatres on 10/02/2015.
 */
public class PowerUp extends Agent {

    private static final int FAVORABLE_CASES = 1;
    private static final int POSSIBLE_CASES = 3;

    private enum EPowerUpType {
        TIME,
        LIFE,
        DASH,
        IMMUNITY
    }
    private static EPowerUpType[] powerUpTypes = EPowerUpType.values();

    public static void generatePowerUp(HyperStore hyperStore, LevelScreen levelScreen) {
        boolean thereIsLuck = Tools.randomBoolean(FAVORABLE_CASES, POSSIBLE_CASES);
        if (!thereIsLuck) return;

        int index = Tools.randomInt(0, powerUpTypes.length-1);
        int initialIndex = index;
        EPowerUpType powerUpType = powerUpTypes[index];
        while (!validPowerUp(powerUpType)) {
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

        if (powerUpType != null) {
            // TODO: where and register in levelScreen
        }
    }

    private static boolean validPowerUp(EPowerUpType powerUpType) {
        // TODO
        return true;
    }

    private PowerUp(Animation animation) {
        super(animation);
    }
}
