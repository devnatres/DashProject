package com.devnatres.dashproject.levelscriptcmd;

import com.devnatres.dashproject.agentsystem.Horde;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Represents a command that register a horde in the specified levelScreen. <br>
 *     <br>
 * Created by DevNatres on 29/12/2014.
 */
public class RegisterHordeCmd extends Cmd {

    private Horde horde;

    public RegisterHordeCmd(LevelScreen levelScreen, Horde horde) {
        super(levelScreen);
        this.horde = horde;
    }

    @Override
    public ECmdState execute() {
        levelScreen.addHorde(horde);
        return ECmdState.TERMINATED;
    }
}
