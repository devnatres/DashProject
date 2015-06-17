package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.nonagentgraphics.Number;
import com.devnatres.dashproject.nonagentgraphics.Number.ENumberType;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Auxiliary structure for the in-game variables of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenVariables {
    private static final int Y_MARGIN = 10;

    float bulletTime;
    int waitingTime;
    boolean comboCameraChasing;
    final Vector3 cameraTarget;

    final Vector2 savedCameraPosition;
    final Vector2 cameraMovementDone;

    private final Number timeNumber;


    public LevelScreenVariables(HyperStore hyperStore) {
        timeNumber = new Number(EAnimMedley.NUMBERS_SILVER.create(hyperStore), ENumberType.DECIMAL1);
        timeNumber.setUnitPosition(DashGame.getInstance().getScreenWidth()/2 - timeNumber.getDigitWidth(),
                DashGame.getInstance().getScreenHeight() - timeNumber.getDigitHeight() - Y_MARGIN);

        cameraTarget = new Vector3();

        savedCameraPosition = new Vector2();
        cameraMovementDone = new Vector2();
    }

    public boolean isTimeOut() {
        return (int)(timeNumber.getValue()*10) == 0;
    }

    public float getTime() {
        return timeNumber.getValue();
    }

    public Number getTimeNumber() {
        return timeNumber;
    }

    public void setTime(float time) {
        timeNumber.setValueDirectly(time);
    }

}
