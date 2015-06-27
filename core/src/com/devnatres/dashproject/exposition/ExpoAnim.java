package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.dnagdx.DnaAnimation;

/**
 * Helper class that represents a foe in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class ExpoAnim extends ExpoAgent {

    private SequenceAction sequenceAnimAction;

    private final Agent agent;

    public ExpoAnim(DnaAnimation dnaAnimation, Vector2 center) {
        sequenceAnimAction = new SequenceAction();

        agent = new Agent(dnaAnimation);

        agent.setCenter(center);
    }

    @Override
    public void assignToFigure(Figure figure) {
        addAgentToFigure(agent, sequenceAnimAction, figure);
    }

    public void addMoveToCenterAction(Vector2 center, float time) {
        addMoveToCenterAndWaitAction(agent, sequenceAnimAction, center, time);
    }

    public void addMoveToCenterAction(Vector2 center) {
        addMoveToCenterAction(center, 0);
    }

    public void addHideAction() {
        addHideAndWaitAction(agent, sequenceAnimAction, 0);
    }

    public void addHideAction(float time) {
        addHideAndWaitAction(agent, sequenceAnimAction, time);
    }

    public void addDisplayAction(float time) {
        addDisplayAndWaitAction(agent, sequenceAnimAction, time);
    }

    public void addDisplayAction() {
        addDisplayAndWaitAction(agent, sequenceAnimAction, 0);
    }

    public void addHideAndDisplayAction(float time) {
        addHideAndDisplayAction(agent, sequenceAnimAction, time);
    }

    public void addDisplayAndHideAction(float time) {
        addDisplayAndHideAction(agent, sequenceAnimAction, time);
    }
}
