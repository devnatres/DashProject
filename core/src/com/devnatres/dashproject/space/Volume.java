package com.devnatres.dashproject.space;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Auxiliary class to optimize rectangular volume calculations.<br>
 * <br>
 * Created by DevNatres on 21/02/2015.
 */
public class Volume {
    private final Rectangle rectangle;
    private final Vector2 position;
    private final Vector2 center;

    public Volume(float x, float y, float width, float height) {
        position = new Vector2(x, y);
        rectangle = new Rectangle(x, y, width, height);
        center = new Vector2(x + width/2, y + height/2);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getX() {
        return rectangle.x;
    }

    public float getY() {
        return rectangle.y;
    }

    public float getCenterX() {
        return center.x;
    }

    public float getCenterY() {
        return center.y;
    }

    public float getWidth() {
        return rectangle.width;
    }

    public float getHeight() {
        return rectangle.height;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        rectangle.setPosition(x, y);
        center.set(x + rectangle.width / 2, y + rectangle.height / 2);
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(float centerX, float centerY) {
        position.set(centerX - rectangle.width / 2, centerY - rectangle.height / 2);
        rectangle.setPosition(position);
        center.set(centerX, centerY);
    }
}
