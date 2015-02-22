package com.devnatres.dashproject.gameinput;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.devnatres.dashproject.dnagdx.DnaCamera;

/**
 * An InputTranslator object listens and manages inputs to answer questions about user gestures. <br>
 *     <br>
 * Try to create only an InputTranslator and to share it between the screens.
 * The reason is that it's registered as a listener in LibGdx and there isn't a way to remove it.<br>
 *     <br>
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
    private boolean menuRequested;
    private final int menuKeycode;

    private final Vector3 clickCoordinates;
    private final Vector2 touchDownPointOnCamera;

    public InputTranslator() {
        touchDownPoint = new Vector2();
        clickCoordinates = new Vector3();
        touchDownPointOnCamera = new Vector2();

        resetKeycode = (Gdx.app.getType() == Application.ApplicationType.Android) ? Input.Keys.BACK : Input.Keys.R;
        menuKeycode = (Gdx.app.getType() == Application.ApplicationType.Android) ? Input.Keys.MENU : Input.Keys.M;

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Clear all flags
     */
    public void clear() {
        isDragged = false;
        touchDownPointPrepared = false;
        resetRequested = false;
        menuRequested = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == resetKeycode) {
            resetRequested = true;
        } else if (keycode == menuKeycode) {
            menuRequested = true;
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

    public Vector2 getTouchDownPointOnCamera(DnaCamera camera) {
        Vector2 point = getTouchDownPoint();
        if (point != null) {
            clickCoordinates.set(touchDownPoint.x, touchDownPoint.y, 0);
            Vector3 position = camera.unproject(clickCoordinates);
            touchDownPointOnCamera.set(position.x, position.y);
            return touchDownPointOnCamera;
        } else {
            return null;
        }
    }

    public boolean isTouchDown() {
        return getTouchDownPoint() != null;
    }

    public boolean isResetRequested() {
        boolean value = resetRequested;
        resetRequested = false;
        return value;
    }

    public boolean isMenuRequested() {
        boolean value = menuRequested;
        menuRequested = false;
        return value;
    }
}
