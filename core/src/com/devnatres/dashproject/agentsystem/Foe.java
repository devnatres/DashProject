package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.animations.EAnimFoe;
import com.devnatres.dashproject.animations.EAnimScore;
import com.devnatres.dashproject.animations.IAnimCreator;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
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
              EAnimFoe.FOE_ROBOT_WALKING,
              EAnimFoe.FOE_ROBOT_WALKING,
              EAnimFoe.FOE_ROBOT_DYING,
              "fx/pum.png",
              1,
              1,
              50,
              100);
    }

    public static Foe buildTank(LevelScreen levelScreen, HyperStore hyperStore) {
        return new Foe(levelScreen,
                hyperStore,
                EAnimFoe.FOE_TANK_WALKING,
                EAnimFoe.FOE_TANK_STUNNING,
                EAnimFoe.FOE_TANK_DYING,
                "fx/pum.png",
                2,
                1,
                75,
                150);
    }

    private static final int ONE_SHAKE_DURATION = 4;
    private static final int TOTAL_SHAKE_DURATION = 40;
    private static final int PUM_DURATION = 15;
    private static final int FIRE_WAIT = 30;
    private static final int PUM_IMAGE_Y_ADJUSTMENT = -3;

    private boolean dying;
    private int life;
    private final int initialLife;
    private final int damage;
    private int fireWait = FIRE_WAIT;

    private final DnaAnimation basicAnimation;
    private final DnaAnimation stunAnimation;
    private final DnaAnimation deadAnimation;
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

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Sound countSound;

    public Foe(LevelScreen levelScreen,
               HyperStore hyperStore,
               IAnimCreator basicAnimCreator,
               IAnimCreator stunAnimCreator,
               IAnimCreator deadAnimCreator,
               String pumTexture,
               int life,
               int damage,
               int standardScore,
               int comboScore) {

        super(basicAnimCreator.create(hyperStore));

        basicAnimation = getAnimation();
        stunAnimation = stunAnimCreator.create(hyperStore);
        deadAnimation = deadAnimCreator.create(hyperStore);
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
            standardScoreAgent = new TransientUpAgent(EAnimScore.SCORE_50.create(hyperStore));
        } else {
            standardScoreAgent = new TransientUpAgent(EAnimScore.SCORE_75.create(hyperStore));
        }
        if (comboScore == 100) {
            comboScoreAgent = new TransientUpAgent(EAnimScore.SCORE_100.create(hyperStore));
        } else {
            comboScoreAgent = new TransientUpAgent(EAnimScore.SCORE_150.create(hyperStore));
        }

        foeDamageResult = new FoeDamageResult();

        countSound = hyperStore.getSound("sounds/count.ogg");
    }


    @Override
    public void act(float delta) {
        if (dying) {
            getAnimation().updateStateTime();
            if (getAnimation().isAnimationFinished()) {
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
            } else {
                getAnimation().updateStateTime();
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
        final Vector2 heroCenter = hero.getCenter();
        boolean isHeroCovered = false;
        final Vector2 center = getCenter();

        final float heroLeftCover = heroCenter.x - map.getCollisionMarginX();
        final float heroRightCover = heroCenter.x + map.getCollisionMarginX();
        final float heroUpCover = heroCenter.y + map.getCollisionMarginY();
        final float heroDownCover = heroCenter.y - map.getCollisionMarginY();

        if ((center.x < heroLeftCover && hero.isCoverLeft())
                || (center.x > heroRightCover && hero.isCoverRight())
                || (center.y < heroDownCover && hero.isCoverDown())
                || (center.y > heroUpCover && hero.isCoverUp())) {
            isHeroCovered = true;
        }
        return !isHeroCovered;
    }

    public boolean isDying() {
        return dying;
    }

    public boolean receiveDamage(int damagePoints) {
        if (dying) return false;

        foeDamageResult.clear();
        if (life > 0) life -= damagePoints;

        if (life <= 0) {
            globalAudio.play(countSound, .5f);

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

        return isComboAttack();
    }

    private void receiveDamage_score() {
        int score;
        Agent scoreAgent;
        if (isComboAttack()) {
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

    private boolean isComboAttack() {
        return levelScreen.isBulletTime()
                && levelScreen.thereAreComboLivingFoesThatContains(this)
                && !levelScreen.isFirstComboFoe(this);
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
                pumImage.setCenter(getCenterX(), getCenterY() + PUM_IMAGE_Y_ADJUSTMENT);
                pumImage.draw(batch);
            }
        }
    }
}
