package com.devnatres.dashproject.levelscriptcmd;

import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;

/**
 * Root class for level-script commands <br>
 *     <br>
 * Created by DevNatres on 29/12/2014.
 */
abstract public class Cmd {
    public enum ECmdState {
        TERMINATED,
        IN_PROGRESS
    }

    protected LevelScreen levelScreen;

    public Cmd(LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
    }

    public ECmdState execute() {
        return ECmdState.TERMINATED;
    }
}
