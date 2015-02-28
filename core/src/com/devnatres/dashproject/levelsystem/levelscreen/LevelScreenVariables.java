package com.devnatres.dashproject.levelsystem.levelscreen;

import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.agentsystem.Number;
import com.devnatres.dashproject.agentsystem.Number.ENumberType;
import com.devnatres.dashproject.gameconstants.EAnimation;
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

    private final Number timeNumber;
    private final Agent timeHalo;

    public LevelScreenVariables(HyperStore hyperStore) {
        timeNumber = new Number(EAnimation.NUMBERS_GOLD.create(hyperStore), ENumberType.DECIMAL1);
        timeNumber.setPosition(240, 700);

        timeHalo = new Agent(EAnimation.TIME_HALO.create(hyperStore));
        timeHalo.setCenter(timeNumber.getX()+timeNumber.getDigitWidth()+timeNumber.getDecimalSeparatorWidth()/2,
                timeNumber.getY()+timeNumber.getDigitHeight()/2);
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
