package com.devnatres.dashproject.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import java.util.Random;

/**
 * Created by DevNatres on 03/12/2014.
 */
abstract public class Tools {
    private static final Random random = new Random();

    public static float limit_f(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }

        return value;
    }

    public static MoveToAction getMoveToAction(Vector2 sourcePosition, Vector2 targetPosition, float speed) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(targetPosition.x, targetPosition.y);
        moveToAction.setDuration(sourcePosition.dst(targetPosition) / speed);

        return moveToAction;
    }

    public static MoveToAction getMoveToActionNext(Vector2 sourcePosition,
                                                   Vector2 targetPosition,
                                                   float speed) {
        MoveToAction moveToAction = getMoveToAction(sourcePosition, targetPosition, speed);
        sourcePosition.set(targetPosition);

        return moveToAction;
    }

    private Tools() {}
}
