package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.space.Volume;

/**
 * An agent is a basic independent entity in the game. <br>
 * It needs an animation. If a texture is passed it's created an one-frame animation.<br>
 * <br>
 * Areas involved: <br>
 * <br>
 * - Graphic area <br>
 *   Dimensions: actor's getWidth() and getHeight().
 *      Their values are established from the first frame of the animation.<br>
 *   Position: actor's getX() and getY().<br>
 * <br>
 * - Physic area <br>
 *   Dimensions and position: "volumeRect" rectangle <br>
 *   Auxiliary properties to avoid frequently calculations: volumePosition and volumeCenter.<br>
 * <br>
 * This implementation create both areas as the same from the first animation (first frame).
 * Graphic area can be modified but physic area remains equal and centered.
 * <br>
 * Created by DevNatres on 04/12/2014.
 */
public class Agent extends Actor {

    private static final float DEFAULT_FRAME_DURATION = 1f;
    private static final float DEFAULT_PARENT_ALPHA = 1f;
    private static final float DEFAULT_SPEED = 1f;

    private final Volume volume;
    private Animation animation;
    private float speed;

    protected float animationStateTime;

    public Agent(Animation animation) {
        TextureRegion textureRegion = animation.getKeyFrame(0);

        volume = new Volume(0f, 0f, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        this.animation = animation;
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        setPosition(0f, 0f);

        speed = DEFAULT_SPEED;
    }

    public Agent(Texture texture) {
        this(new Animation(DEFAULT_FRAME_DURATION, new TextureRegion(texture)));
    }

    public void setCenter(float x, float y) {
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }

    public void setCenter(Vector2 center) {
        setCenter(center.x, center.y);
    }

    @Override
    public void positionChanged() {
        volume.setCenter(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public Vector2 getVolumePosition() {
        return volume.getPosition();
    }

    public void setAnimation(Animation animation) {
        if (animation != null) {
            this.animation = animation;

            TextureRegion textureRegion = animation.getKeyFrame(0);
            setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
            setCenter(getCenter());
        }
        animationStateTime = 0;
    }

    protected void addStateTime(float delta) {
        if (animationStateTime > Time.MAX_TIME_FLOAT) {
            animationStateTime = 0;
        } else {
            animationStateTime += delta;
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            addStateTime(delta);
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            batch.draw(animation.getKeyFrame(animationStateTime), getX(), getY());
        }

        //if (Debug.DEBUG) Debug.addRectangleRef(volume.getRectangle(), Color.YELLOW);
    }

    public void draw(Batch batch) {
        draw(batch, DEFAULT_PARENT_ALPHA);
    }

    public float getSpeed() {
        return speed;
    }

    public Vector2 getCenter() {
        return volume.getCenter();
    }

    public float getCenterX() {
        return volume.getCenterX();
    }

    public float getCenterY() {
        return volume.getCenterY();
    }

    public Vector2 getPosition() {
        return volume.getPosition();
    }

    public float getVolumeX() {
        return volume.getX();
    }

    public float getVolumeY() {
        return volume.getY();
    }

    public float getVolumeWidth() {
        return volume.getWidth();
    }

    public float getVolumeHeight() {
        return volume.getHeight();
    }

    public Rectangle getVolumeRectangle() {
        return volume.getRectangle();
    }
}
