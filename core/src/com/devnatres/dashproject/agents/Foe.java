package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    private static final int SHAKE_FRAME_DURATION = 4;
    private static final int SHAKE_TOTAL_DURATION = 20;

    Animation foeDeadAnimation;
    boolean dying;

    private final LevelScreen levelScreen;
    private final OrthographicCamera camera;
    private final float halfCameraWidth;
    private final float halfCameraHeight;

    private final int shakeDisplacements[] = new int[]{-1, 1};
    private int shakeIndex;
    private int shakeFrameDuration;
    private int shakeTotalDuration;

    public Foe(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.FOE_ROBOT_WALKING.create(hyperStore));

        this.levelScreen = levelScreen;
        camera = levelScreen.getCamera();
        halfCameraWidth = camera.viewportWidth/2f;
        halfCameraHeight = camera.viewportHeight/2f;

        foeDeadAnimation = EAnimations.FOE_ROBOT_DEAD.create(hyperStore);

    }


    @Override
    public void act(float delta) {
        if (dying) {
            addStateTime(delta);
            if (getAnimation().isAnimationFinished(animationStateTime)) {
                setVisible(false);
            }
        } else {
            if (!isBulletTime(levelScreen)) {
                super.act(delta);
            }
        }
    }

    private boolean isBulletTime(LevelScreen levelScreen) {
        return levelScreen != null && levelScreen.isBulletTime();
    }

    public boolean isDying() {
        return dying;
    }

    public void receiveDamage() {
        if (!dying) {
            dying = true;
            setAnimation(foeDeadAnimation);
        }
    }

    public boolean isOnCamera() {
        final float cameraLeft = camera.position.x - halfCameraWidth;
        final float cameraRight = camera.position.x + halfCameraWidth;
        final float cameraDown = camera.position.y - halfCameraHeight;
        final float cameraUp = camera.position.y + halfCameraHeight ;

        return (auxCenter.x >= cameraLeft)
                && (auxCenter.x <= cameraRight)
                && (auxCenter.y >= cameraDown)
                && (auxCenter.y <= cameraUp);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDying() && shakeTotalDuration < SHAKE_TOTAL_DURATION) {
            float originalX = getX();
            float originalY = getY();

            if (shakeFrameDuration < SHAKE_FRAME_DURATION) {
                shakeFrameDuration++;
            } else {
                shakeFrameDuration = 0;
                shakeIndex = (shakeIndex < shakeDisplacements.length - 1) ? shakeIndex + 1 : 0;
            }
            shakeTotalDuration++;
            setPosition(originalX + shakeDisplacements[shakeIndex], originalY);
            super.draw(batch, parentAlpha);
            setPosition(originalX, originalY);
        } else {
            super.draw(batch, parentAlpha);
        }
    }

}
