package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * High level entity that represent a foe.<br>
 * <br>
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    public static Foe buildRobot(LevelScreen levelScreen, HyperStore hyperStore) {
      return new Foe(levelScreen,
              hyperStore,
              EAnimation.FOE_ROBOT_WALKING,
              EAnimation.FOE_ROBOT_WALKING,
              EAnimation.FOE_ROBOT_DYING,
              "pum.png",
              1,
              1,
              50,
              100);
    }

    public static Foe buildTank(LevelScreen levelScreen, HyperStore hyperStore) {
        return new Foe(levelScreen,
                hyperStore,
                EAnimation.FOE_TANK_WALKING,
                EAnimation.FOE_TANK_STUNNING,
                EAnimation.FOE_TANK_DYING,
                "pum.png",
                2,
                2,
                75,
                150);
    }

    private static final int ONE_SHAKE_DURATION = 4;
    private static final int TOTAL_SHAKE_DURATION = 40;
    private static final int PUM_DURATION = 15;
    private static final int FIRE_WAIT = 30;

    private boolean dying;
    private int life;
    private final int initialLife;
    private final int damage;
    private int fireWait = FIRE_WAIT;

    private final Animation basicAnimation;
    private final Animation stunAnimation;
    private final Animation deadAnimation;
    private final Sprite pumImage;
    private int pumImageDuration;
    private final Shake shake = new Shake(TOTAL_SHAKE_DURATION, ONE_SHAKE_DURATION);

    private final LevelScreen levelScreen;
    private final LevelMap map;
    private final DnaCamera camera;

    private final Hero hero;
    private final Vector2 heroLastCenter = new Vector2();

    private Horde horde;
    private FoeDamageResult foeDamageResult;

    private final TransientAgent standardScoreAgent;
    private final int standardScore;
    private final TransientAgent comboScoreAgent;
    private final int comboScore;

    public Foe(LevelScreen levelScreen,
               HyperStore hyperStore,
               EAnimation basicEAnimation,
               EAnimation stunEAnimation,
               EAnimation deadEAnimation,
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
        this.initialLife = life;
        this.damage = damage;

        this.levelScreen = levelScreen;
        map = levelScreen.getMap();
        camera = levelScreen.getCamera();

        hero = levelScreen.getHero();
        heroLastCenter.set(hero.getCenter());

        this.standardScore = standardScore;
        this.comboScore = comboScore;
        if (standardScore == 50) {
            standardScoreAgent = new TransientUpAgent(EAnimation.SCORE_50.create(hyperStore));
        } else {
            standardScoreAgent = new TransientUpAgent(EAnimation.SCORE_75.create(hyperStore));
        }
        if (comboScore == 100) {
            comboScoreAgent = new TransientUpAgent(EAnimation.SCORE_100.create(hyperStore));
        } else {
            comboScoreAgent = new TransientUpAgent(EAnimation.SCORE_150.create(hyperStore));
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
                if (getAnimation() == stunAnimation) {
                    setAnimation(basicAnimation);
                    recoverLife();
                }
                super.act(delta);
                shotIfHeroInSight();
            }
            heroLastCenter.set(hero.getCenter());
        }
    }

    protected void setHorde(Horde horde) {
        this.horde = horde;
    }

    private void shotIfHeroInSight() {
        if (hero.isDying()) return;

        if (camera.isOnCamera(this)) {
            if (fireWait > 0) {
                fireWait--;
            }
            if (isHeroUncovered()) {
                boolean isHeroInSight = map.isLineCollision(getVolumeX(), getVolumeY(),
                        hero.getCenterX(), hero.getCenterY());
                if (isHeroInSight) {
                    boolean isHeroMoved = !heroLastCenter.equals(hero.getCenter());
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
        Vector2 heroCenter = hero.getCenter();
        boolean isHeroCovered = false;
        Vector2 center = getCenter();

        if ((center.x < heroCenter.x && hero.isCoverLeft())
                || (center.x > heroCenter.x && hero.isCoverRight())
                || (center.y < heroCenter.y && hero.isCoverDown())
                || (center.y > heroCenter.y && hero.isCoverUp())) {
            isHeroCovered = true;
        }
        return !isHeroCovered;
    }

    public boolean isDying() {
        return dying;
    }

    public void receiveDamage(int damagePoints) {
        if (dying) return;

        foeDamageResult.clear();
        if (life > 0) life -= damagePoints;

        if (life <= 0) {
            if (horde != null) horde.countKilledFoe();

            dying = true;
            setAnimation(deadAnimation);
            receiveDamage_score();
            levelScreen.removeComboLivingFoe(this);
        } else {
            setAnimation(stunAnimation);
        }

        levelScreen.addComboFoe(this);
        levelScreen.activateBulletTime();
    }

    private void receiveDamage_score() {
        boolean comboAttack = levelScreen.isBulletTime()
                && levelScreen.thereAreComboLivingFoesThatContains(this)
                && !levelScreen.isFirstComboFoe(this);

        int score;
        Agent scoreAgent;
        if (comboAttack) {
            score = comboScore;
            scoreAgent = comboScoreAgent;
            foeDamageResult.setDeadInCombo(true);
        } else {
            score = standardScore;
            scoreAgent = standardScoreAgent;
            foeDamageResult.setDeadInCombo(false);
        }
        foeDamageResult.setScore(score);
        levelScreen.processFoeDamageResult(this, foeDamageResult);
        horde.addFoeDamageResult(foeDamageResult);

        scoreAgent.setCenter(getCenter());
        levelScreen.register(scoreAgent, AgentRegistry.EAgentLayer.SCORE);
    }

    public void recoverLife() {
        life = initialLife;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDying() && shake.canStillShake()) {
            float xBeforeShake = getX();
            float yBeforeShake = getY();
            shake.shake();
            setPosition(xBeforeShake + shake.getDisplacement(), yBeforeShake);
            super.draw(batch, parentAlpha);
            setPosition(xBeforeShake, yBeforeShake);
        } else {
            super.draw(batch, parentAlpha);
            if (pumImageDuration > 0) {
                pumImageDuration--;
                pumImage.setCenter(getCenterX(), getCenterY());
                pumImage.draw(batch);
            }
        }
    }
}
