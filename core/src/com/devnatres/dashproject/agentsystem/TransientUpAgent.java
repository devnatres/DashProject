package com.devnatres.dashproject.agentsystem;

import com.devnatres.dashproject.dnagdx.DnaAnimation;

/**
 * Transient objects that set up their position while they are alive.<br>
 *     <br>
 * Created by DevNatres on 14/02/2015.
 */
public class TransientUpAgent extends TransientAgent {
    private static final float SPEED = .75f;

    public TransientUpAgent(DnaAnimation animation) {
        super(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isVisible()) {
            setY(getY()+SPEED);
        }
    }
}
