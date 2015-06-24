package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.dnagdx.DnaDisplayAction;
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

    public void assignToFigure(Figure figure) {
        finger.addAction(createRepeatAction(sequenceFingerAction));
        figure.add(finger);
    }

    public void addMoveToCenterAndWaitAction(Vector2 center, float time) {
        sequenceFingerAction.addAction(createMoveToCenterAction(finger, center));
        sequenceFingerAction.addAction(new DelayAction(time));
    }

    public void addDisplayAndWaitAction(float time) {
        DnaDisplayAction dnaDisplayAction = new DnaDisplayAction(finger);
        dnaDisplayAction.setDisplayable(true);
        sequenceFingerAction.addAction(dnaDisplayAction);
        sequenceFingerAction.addAction(new DelayAction(time));
    }

    public void addDelayAction(float time) {
        sequenceFingerAction.addAction(new DelayAction(time));
    }

    public void addHideAndWaitAction(float time) {
        DnaDisplayAction dnaDisplayAction = new DnaDisplayAction(finger);
        dnaDisplayAction.setDisplayable(false);
        sequenceFingerAction.addAction(dnaDisplayAction);
        sequenceFingerAction.addAction(new DelayAction(time));
    }
}
