package com.devnatres.dashproject.gameinput;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by DevNatres on 06/12/2014.
 */
public class InputTranslator implements InputProcessor {
    private boolean isDragged;
    private int initialDraggedScreenX;
    private int initialDraggedScreenY;
    private int lastDraggedScreenX;
    private int lastDraggedScreenY;

    private boolean touchDownPointPrepared;
    private Vector2 touchDownPoint;

    private boolean resetRequested;
    private final int resetKeycode;

    public InputTranslator() {
        touchDownPoint = new Vector2();

        resetKeycode = (Gdx.app.getType() == Application.ApplicationType.Android) ? Input.Keys.MENU : Input.Keys.R;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == resetKeycode) {
            resetRequested = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDownPointPrepared = true;
        touchDownPoint.set(screenX, screenY);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isDragged) {
            isDragged = true;
            initialDraggedScreenX = screenX;
            initialDraggedScreenY = screenY;
        }

        lastDraggedScreenX = screenX;
        lastDraggedScreenY = screenY;

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean isDraggedFrom(Rectangle screenRectangle) {
        return isDragged
                && screenRectangle.contains(initialDraggedScreenX, initialDraggedScreenY);
    }

    public boolean isDraggedInsideOut(Rectangle screenRectangle) {
        return isDragged
                && screenRectangle.contains(initialDraggedScreenX, initialDraggedScreenY)
                && !screenRectangle.contains(lastDraggedScreenX, lastDraggedScreenY);
    }

    public Vector2 getTouchDownPoint() {
        if (touchDownPointPrepared) {
            touchDownPointPrepared = false;
            return touchDownPoint;
        } else {
            return null;
        }
    }

    public boolean isResetRequested() {
        return resetRequested;
    }

}
