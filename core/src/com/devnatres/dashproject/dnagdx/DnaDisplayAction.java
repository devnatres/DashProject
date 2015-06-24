package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * Sets the agent's "displayable" value. <br>
 * The setActor(Actor) method has no effect.
 * Instead, the agent (not just a base Actor) must be passed through the constructor
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class DnaDisplayAction extends Action {
    private boolean displayable;

    public DnaDisplayAction(Agent agent) {
        super.setActor(agent);
    }

    @Override
    public void setActor (Actor actor) {
    }

    public boolean act(float delta) {
        ((Agent)actor).setDisplayable(displayable);
        return true;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }
}
