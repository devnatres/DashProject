package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.space.Volume;

/**
 * An agent is a basic independent entity in the game. <br>
 * It needs an animation. If a texture is passed it's created an one-frame animation.<br>
 * <br>
 * Areas involved: <br>
 * <br>
 * - Graphic area <br>
 *   Actor's getWidth(), getHeight(), getX(), getY().
  * <br>
 * - Volume area <br>
 *   Agent's getCenter(), getVolumeRectangle(), etc.
 * <br>
 * This implementation create both areas as the same from the first frame of the original animation.
 * Graphic area can be modified but physic area remains the same and centered on the graphic area.
 * <br>
 * Created by DevNatres on 04/12/2014.
 */
public class Agent extends Actor {

    private static final float DEFAULT_FRAME_DURATION = 1f;
    private static final float DEFAULT_PARENT_ALPHA = 1f;
    private static final float DEFAULT_SPEED = 1f;

    private final Volume volume;
    private DnaAnimation animation;
    private float speed;

    public Agent(DnaAnimation animation) {
        TextureRegion textureRegion = animation.getKeyFrame(0);

        volume = new Volume(0f, 0f, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        this.animation = animation;
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        setPosition(0f, 0f);

        speed = DEFAULT_SPEED;
    }

    public Agent(Texture texture) {
        this(new DnaAnimation(DEFAULT_FRAME_DURATION, new TextureRegion(texture)));
    }

    public void setCenter(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
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

    public void setAnimation(DnaAnimation animation) {
        if (animation != null) {
            this.animation = animation;

            TextureRegion textureRegion = animation.getKeyFrame(0);
            setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
            setCenter(getCenter());

            animation.resetAnimation();
        }
    }

    public DnaAnimation getAnimation() {
        return animation;
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            animation.updateStateTime();
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            batch.draw(animation.getCurrentKeyFrame(), getX(), getY());
        }
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
