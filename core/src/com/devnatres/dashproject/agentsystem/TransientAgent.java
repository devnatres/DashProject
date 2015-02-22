package com.devnatres.dashproject.agentsystem;

import com.devnatres.dashproject.dnagdx.DnaAnimation;

/**
 * High level entity that sets itself invisible when its animation ends.<br>
 * <br>
 * Created by DevNatres on 22/12/2014.
 *
 */
public class TransientAgent extends Agent {
    public TransientAgent(DnaAnimation animation) {
        super(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getAnimation().isAnimationFinished()) {
            setVisible(false);
        }
    }
}
