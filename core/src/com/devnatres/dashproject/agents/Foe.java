package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    private static final int SHAKE_FRAME_DURATION = 4;
    private static final int SHAKE_TOTAL_DURATION = 20;
    private static final int PUM_DURATION = 15;
    private static final int FIRE_WAIT = 30;

    private int pumImageDuration;
    private int fireWait = FIRE_WAIT;

    private final Animation foeDeadAnimation;
    private boolean dying;

    private final LevelScreen levelScreen;
    private final OrthographicCamera camera;
    private final float halfCameraWidth;
    private final float halfCameraHeight;

    private final int shakeDisplacements[] = new int[]{-1, 1};
    private int shakeIndex;
    private int shakeFrameDuration;
    private int shakeTotalDuration;

    private final Hero hero;
    private final Vector2 heroLastCenterPos = new Vector2();

    private final Sprite pumImage;

    public Foe(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.FOE_ROBOT_WALKING.create(hyperStore));

        this.levelScreen = levelScreen;
        camera = levelScreen.getCamera();
        halfCameraWidth = camera.viewportWidth/2f;
        halfCameraHeight = camera.viewportHeight/2f;

        foeDeadAnimation = EAnimations.FOE_ROBOT_DEAD.create(hyperStore);
        hero = levelScreen.getHero();
        heroLastCenterPos.set(hero.getAuxCenter());

        pumImage = new Sprite(hyperStore.getTexture("pum.png"));
    }


    @Override
    public void act(float delta) {
        if (dying) {
            addStateTime(delta);
            if (getAnimation().isAnimationFinished(animationStateTime)) {
                setVisible(false);
            }
        } else {
            if (!levelScreen.isBulletTime()) {
                super.act(delta);
                shotHeroIfInSight();
            }
            heroLastCenterPos.set(hero.getAuxCenter());
        }
    }

    private void shotHeroIfInSight() {
        if (hero.isDying()) {
            return;
        }

        if (isOnCamera()) {
            if (fireWait > 0) {
                fireWait--;
            }
            if (isHeroUncovered()) {
                boolean isHeroMoved = !heroLastCenterPos.equals(hero.getAuxCenter());
                if (isHeroMoved || fireWait == 0) {
                    shotHero();
                    fireWait = FIRE_WAIT;
                }
            }
        } else {
            fireWait = FIRE_WAIT; // To avoid firing just when foe enters on camera
        }
    }

    private void shotHero() {
        hero.receiveDamage();
        pumImageDuration = PUM_DURATION;
        fireWait = FIRE_WAIT;
    }

    private boolean isHeroUncovered() {
        Vector2 heroCenterPos = hero.getAuxCenter();
        boolean isHeroCovered = false;

        if ((auxCenter.x < heroCenterPos.x && hero.isCoverLeft())
                || (auxCenter.x > heroCenterPos.x && hero.isCoverRight())
                || (auxCenter.y < heroCenterPos.y && hero.isCoverDown())
                || (auxCenter.y > heroCenterPos.y && hero.isCoverUp())) {
            isHeroCovered = true;
        }
        return !isHeroCovered;
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
            if (pumImageDuration > 0) {
                pumImageDuration--;
                pumImage.setCenter(auxCenter.x, auxCenter.y);
                pumImage.draw(batch);
            }
        }
    }

}
