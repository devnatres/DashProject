package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.devnatres.dashproject.gameconstants.Time;

/**
 * An agent is a basic independent entity in the game.
 *
 * Created by DevNatres on 04/12/2014.
 */
public class Agent extends Actor {
    private static final float DEFAULT_FRAME_DURATION = 1f;
    private static final float DEFAULT_PARENT_ALPHA = 1f;
    private static final float DEFAULT_SPEED = 1f;

    protected final Vector2 positionRef;
    protected final Rectangle areaRef;
    protected Vector2 centerRef;

    private Animation animation;
    private float speed;
    protected float animationStateTime;

    private float frameWidth;
    private float frameHeight;
    private float frameXDisplacement;
    private float frameYDisplacement;

    public Agent(Animation animation) {
        TextureRegion textureRegion = animation.getKeyFrame(0);
        areaRef = new Rectangle(textureRegion.getRegionX(),
                textureRegion.getRegionY(),
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight());
        positionRef = new Vector2();
        centerRef = new Vector2();

        setAnimation(animation);
        setPosition(0f, 0f);
        setSize(areaRef.getWidth(), areaRef.getHeight());
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
        positionRef.set(getX(), getY());
        areaRef.setPosition(positionRef);
        areaRef.getCenter(centerRef);
    }

    @Override
    public void sizeChanged() {
        areaRef.setSize(getWidth(), getHeight());
        areaRef.getCenter(centerRef);
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;

        frameWidth = animation.getKeyFrame(0).getRegionWidth();
        frameHeight = animation.getKeyFrame(0).getRegionHeight();
        frameXDisplacement = (frameWidth - areaRef.width)/2;
        frameYDisplacement = (frameHeight - areaRef.height)/2;

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
            float frameX = getX() - frameXDisplacement;
            float frameY = getY() - frameYDisplacement;

            batch.draw(animation.getKeyFrame(animationStateTime),
                    frameX, frameY,
                    frameWidth, frameHeight);
        }
    }

    public void draw(Batch batch) {
        draw(batch, DEFAULT_PARENT_ALPHA);
    }

    public float getSpeed() {
        return speed;
    }

    public Vector2 getCenterRef() {
        return centerRef;
    }

    public Vector2 getPositionRef() {
        return positionRef;
    }

}
