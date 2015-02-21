package com.devnatres.dashproject.levelscriptcmd;

import com.devnatres.dashproject.agentsystem.Horde;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by DevNatres on 29/12/2014.
 */
public class CreateHordeCmd extends Cmd {

    private Horde horde;

    public CreateHordeCmd(LevelScreen levelScreen, Horde horde) {
        super(levelScreen);
        this.horde = horde;
    }

    @Override
    public ECmdState execute() {
        levelScreen.addHorde(horde);
        return ECmdState.TERMINATED;
    }
}
