package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * Helper class that agents in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
abstract public class ExpoAgent {
    protected static RepeatAction createRepeatAction(SequenceAction sequenceAction) {
        final RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);

        return repeatAction;
    }

    protected static MoveToAction createMoveToCenterAction(Agent agent, Vector2 center) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(center.x - agent.getWidth() / 2f, center.y - agent.getHeight() / 2f);
        return moveToAction;
    }
}
