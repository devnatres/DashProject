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
 * Created by DevNatres on 04/12/2014.
 */
public class Agent extends Actor {
    private static final float DEFAULT_STATE_TIME = 1f;
    private static final float DEFAULT_PARENT_ALPHA = 1f;
    private static final float DEFAULT_SPEED = 1f;

    protected final Vector2 auxPosition;
    protected final Rectangle auxArea;
    protected Vector2 auxCenter;

    private Animation animation;

    private float speed;

    protected float animationStateTime;

    public Agent(Animation animation) {
        this.animation = animation;

        TextureRegion textureRegion = animation.getKeyFrame(0);
        auxArea = new Rectangle(textureRegion.getRegionX(),
                textureRegion.getRegionY(),
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight());
        auxPosition = new Vector2();
        auxCenter = new Vector2();

        setPosition(0f, 0f);
        setSize(auxArea.getWidth(), auxArea.getHeight());

        speed = DEFAULT_SPEED;
    }

    public Agent(Texture texture) {
        this(new Animation(DEFAULT_STATE_TIME, new TextureRegion(texture)));
    }

    public void setCenter(float x, float y) {
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }

    @Override
    public void positionChanged() {
        auxPosition.set(getX(), getY());
        auxArea.setPosition(auxPosition);
        auxArea.getCenter(auxCenter);
    }

    @Override
    public void sizeChanged() {
        auxArea.setSize(getWidth(), getHeight());
        auxArea.getCenter(auxCenter);
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
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
            // TODO: not the best place for every frame
            float frameWidth = animation.getKeyFrame(0).getRegionWidth();
            float frameHeight = animation.getKeyFrame(0).getRegionHeight();
            float frameX = getX() - (frameWidth - auxArea.width)/2;
            float frameY = getY() - (frameHeight - auxArea.height)/2;

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

    public Vector2 getAuxCenter() {
        return auxCenter;
    }

    public Vector2 getAuxPosition() {
        return auxPosition;
    }

}
