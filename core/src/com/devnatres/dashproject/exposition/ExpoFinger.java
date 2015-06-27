package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper class that represents a finger pointer in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class ExpoFinger extends ExpoAgent {
    static private final int X_DISPLACEMENT = 28;
    static private final int Y_DISPLACEMENT = -22;

    private SequenceAction sequenceFingerAction;
    private final Agent finger;

    public ExpoFinger(HyperStore hyperStore, Vector2 center) {
        this(hyperStore, center, false);
    }

    public ExpoFinger(HyperStore hyperStore, Vector2 center, boolean fast) {
        sequenceFingerAction = new SequenceAction();

        if (fast) {
            finger = new Agent(EAnimMedley.HELP_FAST_FINGER.create(hyperStore));
        } else {
            finger = new Agent(EAnimMedley.HELP_FINGER.create(hyperStore));
        }

        finger.setCenter(center.x + X_DISPLACEMENT, center.y + Y_DISPLACEMENT);
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

    public void addDisplayAndHideAnimAction() {
        addDisplayAndHideAnimAction(finger, sequenceFingerAction);
    }

    public void addHideAction(float time) {
        addHideAndWaitAction(finger, sequenceFingerAction, time);
    }
}
