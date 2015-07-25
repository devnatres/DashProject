package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.space.DirectionSelector;

/**
 * Extends the OrthographicCamera Gdx class to increase functionality.<br>
 *     <br>
 * Created by DevNatres on 07/01/2015.
 */
public class DnaCamera extends OrthographicCamera {
    private float halfCameraWidth;
    private float halfCameraHeight;

    private final FitViewport fitViewport;

    private final DirectionSelector auxOutDirectionSelector = new DirectionSelector();

    public DnaCamera() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fitViewport = new FitViewport(DashGame.SCREEN_WIDTH, DashGame.SCREEN_HEIGHT, this);
        setToOrtho(false, DashGame.SCREEN_WIDTH, DashGame.SCREEN_HEIGHT);
    }

    @Override
    public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight) {
        updateInternals();
        super.setToOrtho(yDown, viewportWidth, viewportHeight);
    }

    private void updateInternals() {
        halfCameraWidth = DashGame.SCREEN_WIDTH/2f;
        halfCameraHeight = DashGame.SCREEN_HEIGHT/2f;
    }

    public void updateViewport(int width, int height) {
        fitViewport.update(width, height);
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
        Vector2 point = agent.getCenter();
        return isOnCamera(point.x, point.y);
    }
}
