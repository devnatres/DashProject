package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * A transient agent sets itself invisible when its animation ends.
 *
 * Created by DevNatres on 22/12/2014.
 *
 */
public class TransientAgent extends Agent {
    public TransientAgent(Animation animation) {
        super(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getAnimation().isAnimationFinished(animationStateTime)) {
            setVisible(false);
        }
    }
}
