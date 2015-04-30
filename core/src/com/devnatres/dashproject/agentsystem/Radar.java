package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.dnagdx.AlphaModifier;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.space.DirectionSelector;

/**
 * High level class to represent the radar.<br>
 * <br>
 * Created by DevNatres on 30/04/2015.
 */
public class Radar extends Agent {
    private static final float MIN_ALPHA = 0.4f;
    private static final float MAX_ALPHA = 1f;
    private static final float ALPHA_DELTA = 0.05f;

    private final DnaAnimation rightAnimation;
    private final DnaAnimation upAnimation;
    private final DnaAnimation rightUpAnimation;

    private float currentAlpha = MIN_ALPHA;
    private float alphaDelta = ALPHA_DELTA;
    private final AlphaModifier alphaModifier;

    private final DnaCamera camera;
    private boolean flipX;
    private boolean flipY;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Sound radarSound;

    public Radar(LevelScreen levelScreen, HyperStore hyperStore) {
        super(EAnimation.RADAR_RIGHT.create(hyperStore));

        rightAnimation = getAnimation();
        upAnimation = EAnimation.RADAR_UP.create(hyperStore);
        rightUpAnimation = EAnimation.RADAR_RIGHT_UP.create(hyperStore);

        camera = levelScreen.getCamera();

        alphaModifier = new AlphaModifier();

        radarSound = hyperStore.getSound("sounds/radar.ogg");
    }
    public void lookAt(Vector2 referencePos) {
        DirectionSelector directionSelector = camera.getOutDirection(referencePos.x, referencePos.y);
        if (!directionSelector.hasDirection()) return;

        float spriteLeft, spriteBottom;

        if (directionSelector.isUp()) {
            spriteBottom = camera.getUp() - getHeight();
            spriteLeft = getSpriteLeft(directionSelector, referencePos);
        } else if (directionSelector.isDown()) {
            spriteBottom = camera.getDown();
            spriteLeft = getSpriteLeft(directionSelector, referencePos);
        } else {
            if (directionSelector.isLeft()) {
                spriteBottom = referencePos.y - getHeight()/2f;
                spriteLeft = camera.getLeft();
            } else { // isRight()
                spriteBottom = referencePos.y - getHeight()/2f;
                spriteLeft = camera.getRight() - getWidth();
            }

            if (spriteBottom < camera.getDown()) {
                spriteBottom = camera.getDown();
            } else if ((spriteBottom + getHeight()) > camera.getUp()) {
                spriteBottom = camera.getUp() - getHeight();
            }
        }
        setPosition(spriteLeft, spriteBottom);

        selectAnimation(directionSelector);
    }

    private float getSpriteLeft(DirectionSelector directionSelector, Vector2 referencePos) {
        float spriteLeft;
        if (directionSelector.isLeft()) {
            spriteLeft = camera.getLeft();
        } else if (directionSelector.isRight()) {
            spriteLeft = camera.getRight() - getWidth();
        } else {
            spriteLeft = referencePos.x - getWidth()/2f;
            if (spriteLeft < camera.getLeft()) {
                spriteLeft = camera.getLeft();
            } else if ((spriteLeft+getWidth()) > camera.getRight()) {
                spriteLeft = camera.getRight() - getWidth();
            }
        }
        return spriteLeft;
    }

    private void selectAnimation(DirectionSelector directionSelector) {
        if (!directionSelector.isLeft() && !directionSelector.isRight()) {
            setAnimation(upAnimation);
        } else if (!directionSelector.isUp() && !directionSelector.isDown()) {
            setAnimation(rightAnimation);
        } else {
            setAnimation(rightUpAnimation);
        }

        flipX = directionSelector.isLeft();
        flipY = directionSelector.isDown();
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            currentAlpha += alphaDelta;
            if (currentAlpha > MAX_ALPHA) {
                currentAlpha = MAX_ALPHA;
                alphaDelta *= -1;
                globalAudio.play(radarSound, .3f);
            } else if (currentAlpha < MIN_ALPHA) {
                currentAlpha = MIN_ALPHA;
                alphaDelta *= -1;
            }

            getAnimation().updateStateTime();
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            alphaModifier.modify(batch, currentAlpha);

            batch.draw(getAnimation().getCurrentKeyFrame().getTexture(),
                    getX(), getY(),
                    getWidth(), getHeight(),
                    0, 0,
                    (int) getWidth(), (int) getHeight(),
                    flipX, flipY);

            alphaModifier.restore(batch);
        }
    }
}
