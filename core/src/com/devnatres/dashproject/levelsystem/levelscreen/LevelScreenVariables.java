package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.nonagentgraphics.Number;
import com.devnatres.dashproject.nonagentgraphics.Number.ENumberType;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Auxiliary structure for the in-game variables of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenVariables {
    float bulletTime;
    private String timeString;
    int waitingTime;
    boolean comboCameraChasing;
    final Vector3 cameraTarget;

    final Vector2 savedCameraPosition;
    final Vector2 cameraMovementDone;

    private final Number timeNumber;
    private final Agent timeHalo;

    public LevelScreenVariables(HyperStore hyperStore) {
        timeNumber = new Number(EAnimMedley.NUMBERS_SILVER.create(hyperStore), ENumberType.DECIMAL1);
        timeNumber.setUnitPosition(240, 700);

        timeHalo = new Agent(EAnimMedley.TIME_HALO.create(hyperStore));
        timeHalo.setCenter(timeNumber.getUnitX()+timeNumber.getDigitWidth()+timeNumber.getDecimalSeparatorWidth()/2,
                timeNumber.getUnitY()+timeNumber.getDigitHeight()/2);

        cameraTarget = new Vector3();

        savedCameraPosition = new Vector2();
        cameraMovementDone = new Vector2();
    }

    public float getTime() {
        return timeNumber.getValue();
    }

    public Number getTimeNumber() {
        return timeNumber;
    }

    public void setTime(float time) {
        timeNumber.setValueDirectly(time);
        timeString = String.valueOf(((int)(time *10))/10f);
    }

    public Agent getTimeHalo() {
        return timeHalo;
    }

    public String getTimeString() {
        return timeString;
    }
}
