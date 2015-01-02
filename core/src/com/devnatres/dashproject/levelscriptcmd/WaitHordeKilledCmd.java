package com.devnatres.dashproject.levelscriptcmd;

import com.devnatres.dashproject.agents.Horde;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by David on 29/12/2014.
 */
public class WaitHordeKilledCmd extends Cmd {

    private Horde horde;

    public WaitHordeKilledCmd(LevelScreen levelScreen, Horde horde) {
        super(levelScreen);
        this.horde = horde;
    }

    @Override
    public ECmdState execute() {
        return horde.isKilled() ? ECmdState.TERMINATED : ECmdState.IN_PROGRESS;
    }
}
