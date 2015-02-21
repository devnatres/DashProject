package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.DashGame;

/**
 * Created by DevNatres on 14/02/2015.
 */
public class TransientUpAgent extends TransientAgent {
    private static final float SPEED = .5f;

    public TransientUpAgent(Animation animation) {
        super(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isVisible() && getY() < DashGame.getGlobalScreenHeight()) {
            setY(getY()+SPEED);
        }
    }
}
