package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    private static final int STANDARD_SCORE = 50;
    private static final int STANDARD_COMBO_FACTOR = 2;

    private static final int DEFAULT_HEAL_POINTS = 1;
    private static final int SHAKE_FRAME_DURATION = 4;
    private static final int SHAKE_TOTAL_DURATION = 20;
    private static final int PUM_DURATION = 15;
    private static final int FIRE_WAIT = 30;

    private int life = DEFAULT_HEAL_POINTS;
    private int pumImageDuration;
    private int fireWait = FIRE_WAIT;

    private final Animation foeDeadAnimation;
    private boolean dying;

    private final LevelScreen levelScreen;
    private final DnaCamera camera;

    private final int shakeDisplacements[] = new int[]{-1, 1};
    private int shakeIndex;
    private int shakeFrameDuration;
    private int shakeTotalDuration;

    private final Hero hero;
    private final Vector2 heroLastCenterPos = new Vector2();

    private final Sprite pumImage;

    private final TransientAgent score50;
    private final TransientAgent score100;

    private Horde horde;

    private FoeDamageResult foeDamageResult;

    public Foe(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.FOE_ROBOT_WALKING.create(hyperStore));

        this.levelScreen = levelScreen;
        camera = levelScreen.getCamera();

        foeDeadAnimation = EAnimations.FOE_ROBOT_DEAD.create(hyperStore);
        hero = levelScreen.getHero();
        heroLastCenterPos.set(hero.getAuxCenter());

        pumImage = new Sprite(hyperStore.getTexture("pum.png"));

        score50 = new TransientAgent(EAnimations.SCORE_50.create(hyperStore));
        score100 = new TransientAgent(EAnimations.SCORE_100.create(hyperStore));

        foeDamageResult = new FoeDamageResult();
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

    void setHorde(Horde horde) {
        this.horde = horde;
    }

    private void shotHeroIfInSight() {
        if (hero.isDying()) {
            return;
        }

        if (camera.isOnCamera(this)) {
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

    public Horde getHorde() {
        return horde;
    }

    public void receiveDamage(int damagePoints) {
        foeDamageResult.clear();

        if (!dying) {
            if (life > 0) {
                life -= damagePoints;
            }
            if (life <= 0) {
                if (horde != null) {
                    horde.countKilledFoe(this);
                }
                dying = true;
                setAnimation(foeDeadAnimation);

                int score;
                Agent scoreAgent;
                if (levelScreen.isBulletTime()) {
                    score = STANDARD_SCORE * STANDARD_COMBO_FACTOR;
                    scoreAgent = score100;
                    foeDamageResult.setDeadInCombo(true);
                } else {
                    score = STANDARD_SCORE;
                    scoreAgent = score50;
                    foeDamageResult.setDeadInCombo(false);
                }
                foeDamageResult.setScore(score);
                scoreAgent.setCenter(auxCenter.x, auxCenter.y);
                levelScreen.register(scoreAgent, AgentRegistry.EAgentLayer.SCORE);

                levelScreen.processFoeDamageResult(foeDamageResult);
                horde.processFoeDamageResult(foeDamageResult);
            }
        }
    }

/*    public boolean isOnCamera() {
        final float cameraLeft = camera.position.x - halfCameraWidth;
        final float cameraRight = camera.position.x + halfCameraWidth;
        final float cameraDown = camera.position.y - halfCameraHeight;
        final float cameraUp = camera.position.y + halfCameraHeight ;

        return (auxCenter.x >= cameraLeft)
                && (auxCenter.x <= cameraRight)
                && (auxCenter.y >= cameraDown)
                && (auxCenter.y <= cameraUp);
    }*/

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
