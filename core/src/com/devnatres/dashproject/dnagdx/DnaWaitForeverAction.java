package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Wait forever.<br>
 * "What the...?" <br>
 * Well, it's just a trick to make a sequence of actions to wait until
 * an external sequenceAction.restart() is executed.
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */

public class DnaWaitForeverAction extends Action {

    @Override
    public boolean act(float delta) {
        return false;
    }
}
