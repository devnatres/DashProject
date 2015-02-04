package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    public static Foe buildRobot(HyperStore hyperStore, LevelScreen levelScreen) {
      return new Foe(hyperStore,
              levelScreen,
              EAnimations.FOE_ROBOT_WALKING,
              EAnimations.FOE_ROBOT_WALKING,
              EAnimations.FOE_ROBOT_DEAD,
              "pum.png",
              1,
              1,
              50,
              100);
    }

    private static final int SHAKE_FRAME_DURATION = 4;
    private static final int SHAKE_TOTAL_DURATION = 20;
    private static final int PUM_DURATION = 15;
    private static final int FIRE_WAIT = 30;

    private int life;
    private final int damage;

    private int pumImageDuration;
    private int fireWait = FIRE_WAIT;

    private final Animation basicAnimation;
    private final Animation stunAnimation;
    private final Animation deadAnimation;
    private final Sprite pumImage;

    private boolean dying;

    private final LevelScreen levelScreen;
    private final DnaCamera camera;

    private final int shakeDisplacements[] = new int[]{-1, 1};
    private int shakeIndex;
    private int shakeFrameDuration;
    private int shakeTotalDuration;

    private final Hero hero;
    private final Vector2 heroLastCenterPos = new Vector2();

    private final TransientAgent standardScoreAgent;
    private final TransientAgent comboScoreAgent;
    private final int standardScore;
    private final int comboScore;

    private Horde horde;

    private FoeDamageResult foeDamageResult;

    private final LevelMap map;

    public Foe(HyperStore hyperStore,
               LevelScreen levelScreen,
               EAnimations basicEAnimation,
               EAnimations stunEAnimation,
               EAnimations deadEAnimation,
               String pumTexture,
               int life,
               int damage,
               int standardScore,
               int comboScore) {
        super(basicEAnimation.create(hyperStore));

        basicAnimation = getAnimation();
        stunAnimation = stunEAnimation.create(hyperStore);
        deadAnimation = deadEAnimation.create(hyperStore);
        pumImage = new Sprite(hyperStore.getTexture(pumTexture));

        this.life = life;
        this.damage = damage;

        this.levelScreen = levelScreen;
        map = levelScreen.getMap();
        camera = levelScreen.getCamera();

        hero = levelScreen.getHero();
        heroLastCenterPos.set(hero.getAuxCenter());

        this.standardScore = standardScore;
        this.comboScore = comboScore;
        if (standardScore == 50) {
            standardScoreAgent = new TransientAgent(EAnimations.SCORE_50.create(hyperStore));
        } else {
            // TODO: Other score
            standardScoreAgent = new TransientAgent(EAnimations.SCORE_50.create(hyperStore));
        }
        if (comboScore == 100) {
            comboScoreAgent = new TransientAgent(EAnimations.SCORE_100.create(hyperStore));
        } else {
            // TODO: Other score
            comboScoreAgent = new TransientAgent(EAnimations.SCORE_100.create(hyperStore));
        }

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

    protected void setHorde(Horde horde) {
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
                boolean isHeroVisible = map.isLineCollision(auxCenter.x, auxCenter.y,
                        hero.getAuxCenter().x, hero.getAuxCenter().y);
                if (isHeroVisible) {
                    boolean isHeroMoved = !heroLastCenterPos.equals(hero.getAuxCenter());
                    if (isHeroMoved || fireWait == 0) {
                        shotHero();
                        fireWait = FIRE_WAIT;
                    }
                }
            }
        } else {
            fireWait = FIRE_WAIT; // To avoid firing just when foe enters on camera
        }
    }

    private void shotHero() {
        hero.receiveDamage(damage);
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
                setAnimation(deadAnimation);

                int score;
                Agent scoreAgent;
                if (levelScreen.isBulletTime()) {
                    score = comboScore;
                    scoreAgent = comboScoreAgent;
                    foeDamageResult.setDeadInCombo(true);
                } else {
                    score = standardScore;
                    scoreAgent = standardScoreAgent;
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
