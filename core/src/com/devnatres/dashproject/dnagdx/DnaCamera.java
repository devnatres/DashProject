package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.agents.Agent;
import com.devnatres.dashproject.space.DirectionSelector;

/**
 * Created by DevNatres on 07/01/2015.
 */
public class DnaCamera extends OrthographicCamera {
    private float halfCameraWidth;
    private float halfCameraHeight;

    private final DirectionSelector auxOutDirectionSelector = new DirectionSelector();

    public DnaCamera() {
        super();
    }

    public DnaCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
        updateViewPortCalculus();
    }

    @Override
    public void setToOrtho (boolean yDown, float viewportWidth, float viewportHeight) {
        updateViewPortCalculus();
        super.setToOrtho(yDown, viewportWidth, viewportHeight);
    }

    private void updateViewPortCalculus() {
        halfCameraWidth = viewportWidth/2f;
        halfCameraHeight = viewportHeight/2f;
    }

    public float getUp() {
        return position.y + halfCameraHeight;
    }

    public float getRight() {
        return position.x + halfCameraWidth;
    }

    public float getDown() {
        return position.y - halfCameraHeight;
    }

    public float getLeft() {
        return position.x - halfCameraWidth;
    }

    public DirectionSelector getOutDirection(float pointX, float pointY) {
        auxOutDirectionSelector.clear();

        if (pointY > getUp()) {
            auxOutDirectionSelector.setUp();
        } else if (pointY < getDown()) {
            auxOutDirectionSelector.setDown();
        }

        if (pointX < getLeft()) {
            auxOutDirectionSelector.setLeft();
        } else if (pointX > getRight()) {
            auxOutDirectionSelector.setRight();
        }

        return auxOutDirectionSelector;
    }

    public boolean isOnCamera(float pointX, float pointY) {
        return !getOutDirection(pointX, pointY).hasDirection();
    }

    public boolean isOnCamera(Agent agent) {
        Vector2 point = agent.getAuxCenter();
        return isOnCamera(point.x, point.y);
    }
}
