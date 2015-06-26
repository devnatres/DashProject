package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper class that represents a finger pointer in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class ExpoFinger extends ExpoAgent {
    private final SequenceAction sequenceFingerAction;
    private final Agent finger;

    public ExpoFinger(HyperStore hyperStore, Vector2 center) {
        sequenceFingerAction = new SequenceAction();

        finger = new Agent(hyperStore.getTexture("help/help_finger.png"));
        finger.setCenter(center);
    }

    @Override
    public void assignToFigure(Figure figure) {
        addAgentToFigure(finger, sequenceFingerAction, figure);
    }

    public void addDisplayAction(float time) {
        addDisplayAndWaitAction(finger, sequenceFingerAction, time);
    }

    public void addDisplayAction() {
        addDisplayAction(0);
    }

    public void addHideAction(float time) {
        addHideAndWaitAction(finger, sequenceFingerAction, time);
    }
}
