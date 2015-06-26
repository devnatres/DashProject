package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.dnagdx.DnaDisplayAction;
import com.devnatres.dashproject.dnagdx.DnaWaitForeverAction;

/**
 * Helper class that agents in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
abstract public class ExpoAgent {
    protected static MoveToAction createMoveToCenterAction(Agent agent, Vector2 center) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(center.x - agent.getWidth() / 2f, center.y - agent.getHeight() / 2f);
        return moveToAction;
    }

    protected static void addAgentToFigure(Agent agent, SequenceAction sequenceAction, Figure figure){
        sequenceAction.addAction(new DnaWaitForeverAction());
        agent.addAction(sequenceAction);
        figure.add(agent);
    }

    protected static void addDisplayAndWaitAction(Agent agent, SequenceAction sequenceAction, float time) {
        DnaDisplayAction dnaDisplayAction = new DnaDisplayAction(agent);
        dnaDisplayAction.setDisplayable(true);
        sequenceAction.addAction(dnaDisplayAction);
        if (time > 0) sequenceAction.addAction(new DelayAction(time));
    }

    protected static void addHideAndWaitAction(Agent agent, SequenceAction sequenceAction, float time) {
        DnaDisplayAction dnaDisplayAction = new DnaDisplayAction(agent);
        dnaDisplayAction.setDisplayable(false);
        sequenceAction.addAction(dnaDisplayAction);
        if (time > 0) sequenceAction.addAction(new DelayAction(time));
    }

    protected static void addMoveToCenterAndWaitAction(Agent agent, SequenceAction sequenceAction,
                                                       Vector2 center, float time) {
        sequenceAction.addAction(createMoveToCenterAction(agent, center));
        if (time > 0) sequenceAction.addAction(new DelayAction(time));
    }

    abstract public void assignToFigure(Figure figure);
}
