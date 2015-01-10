package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.space.DirectionSelector;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.VectorPool;

/**
 * Created by DevNatres on 06/12/2014.
 */
public class Hero extends Agent {
    private static final boolean DEBUG_IMMORTAL = false;

    private static final int DAMAGE_DURATION = 15;
    private static final int MAX_LIFE = 10;
    private static final int STANDARD_ATTACK_DAMAGE = 1;

    private int damageImageDuration;
    private int life = MAX_LIFE;

    private final Sound dashSound;
    private final Sound failDashSound;
    private final Sound hitSound;
    private final Sound comboSound;
    private final Sound deadSound;

    private final DirectionSelector coverDirection;
    private final Rectangle nextArea;

    private final float attackRadio;
    private final float attackRadio2;
    private final Sprite attackHalo; // TODO Only for debugging. Remove.

    private final float dashRadio;
    private final Sprite dashHalo;

    private final float scopeRadio;
    private final float scopeRadio2;

    private final Animation walkingAnimation;
    private final Animation attackingAnimation;
    private float attackingTime;

    private final LevelScreen levelScreen;

    private final HyperStore hyperStore;

    private final Sprite damageImage;

    private final Vector2 nextPositionFromInput = new Vector2();
    private boolean thereIsNextPosFromInput;

    private final Animation deadAnimation;
    private boolean dying;

    static {
        VectorPool.initialize();
    }

    public Hero(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.HERO_WALKING.create(hyperStore));
        //setSize(64, 128);

        this.hyperStore = hyperStore;

        this.levelScreen = levelScreen;

        walkingAnimation = getAnimation();
        attackingAnimation = EAnimations.HERO_ATTACKING.create(hyperStore);

        dashSound = hyperStore.getSound("sounds/dash.ogg");
        failDashSound = hyperStore.getSound("sounds/fail_hit.ogg");
        hitSound = hyperStore.getSound("sounds/hit.ogg");
        comboSound = hyperStore.getSound("sounds/combo.ogg");
        deadSound = hyperStore.getSound("sounds/hit.ogg");

        coverDirection = new DirectionSelector();
        nextArea = new Rectangle(auxArea);

        dashHalo = new Sprite(hyperStore.getTexture("dash_radio.png"));
        dashRadio = dashHalo.getWidth() / 2;

        attackHalo = new Sprite(hyperStore.getTexture("attack_radio.png"));
        attackRadio = attackHalo.getWidth() / 2;
        attackRadio2 = attackRadio * attackRadio;

        scopeRadio = dashRadio + attackRadio;
        scopeRadio2 = scopeRadio * scopeRadio;

        damageImage = new Sprite(hyperStore.getTexture("shoot_damage.png"));

        centerReferences();

        deadAnimation = EAnimations.HERO_DYING.create(hyperStore);
    }

    @Override
    public void positionChanged() {
        super.positionChanged();
        if (dashHalo != null) centerReferences();
    }

    private void centerReferences() {
        dashHalo.setCenter(auxCenter.x, auxCenter.y);
        attackHalo.setCenter(auxCenter.x, auxCenter.y);
        damageImage.setCenter(auxCenter.x, auxCenter.y);
    }

    public void programNextPos(float nextX, float nextY) {
        nextPositionFromInput.set(nextX, nextY);
        thereIsNextPosFromInput = true;
    }

    /**
     * Move the hero to the position previously programmed (solicited) with programNextPos().
     * @return true if the hero was moved
     */
    private boolean setNextPosIfAvailable(float targetCenterX, float targetCenterY) {
        if (Debug.DEBUG) Debug.addPoint(targetCenterX, targetCenterY, Color.WHITE);

        if (!thereIsNextPosFromInput) {
            return false;
        }

        thereIsNextPosFromInput = false;

        Vector2 vTarget = VectorPool.get();
        vTarget.set(targetCenterX, targetCenterY);
        vTarget.sub(auxCenter);
        vTarget.limit(dashRadio);

        targetCenterX = auxCenter.x + vTarget.x;
        targetCenterY = auxCenter.y + vTarget.y;

        if (Debug.DEBUG) Debug.addPoint(targetCenterX, targetCenterY, Color.RED);

        // Normal movement
        float x = targetCenterX - getWidth() / 2;
        float y = targetCenterY - getHeight() / 2;

        nextArea.setPosition(x, y);

        LevelMap map = levelScreen.getMap();
        if (map.slide(nextArea)) {
            if (levelScreen != null) {
                Agent dashShadow = new TransientAgent(EAnimations.HERO_DASHING.create(hyperStore));
                dashShadow.setCenter(auxCenter.x, auxCenter.y);
                levelScreen.register(dashShadow, AgentRegistry.EAgentLayer.TRUNK);
            }

            dashSound.play(.1f);

            nextArea.setPosition(limitPositionX(nextArea.x), limitPositionY(nextArea.y));
            setPosition(nextArea.x, nextArea.y);
            map.updateThisCoverDirection(coverDirection, auxArea);
        } else {
            failDashSound.play();
        }

        VectorPool.recycle(vTarget);

        return true;
    }

    private float limitPositionX(float x) {
        if (x < 0) {
            x = 0;
        } else if (x + getWidth() >= levelScreen.getMap().getMapPixelWidth()) {
            x = levelScreen.getMap().getMapPixelWidth() - getWidth() - 1f;
        }
        return x;
    }

    private float limitPositionY(float y) {
        if (y < 0) {
            y = 0;
        } else if (y + getHeight() >= levelScreen.getMap().getMapPixelHeight()) {
            y = levelScreen.getMap().getMapPixelHeight() - getHeight() - 1f;
        }
        return y;
    }

    @Override
    public void act(float delta) {
        if (dying) {
            addStateTime(delta);
            if (getAnimation().isAnimationFinished(animationStateTime)) {
                setVisible(false);
            }
        } else {
            super.act(delta);
            boolean moved = setNextPosIfAvailable(nextPositionFromInput.x, nextPositionFromInput.y);
            boolean attackReleased = attack();
            if (attackReleased) {
                levelScreen.activateBulletTime(); // Reboot if it was activated before
            } else if (moved) {
                levelScreen.deactivateBulletTime();
            }

            if (attackingTime > 0) {
                attackingTime--;
                if (attackingTime == 0) {
                    setAnimation(walkingAnimation);
                }
            }
        }
    }

    /**
     * Attack any nearby foe.
     * @return true if the attack was done
     */
    private boolean attack() {
        boolean attackReleased = false;
        Horde globalHorde = levelScreen.getGlobalHorde();
        for (int i = 0, n = globalHorde.size(); i < n; i++) {
            Foe foe = globalHorde.getFoe(i);
            if (!foe.isDying() && (auxPosition.dst2(foe.getAuxPosition()) <= attackRadio2)) {

                setAnimation(attackingAnimation);
                attackingTime = attackingAnimation.getAnimationDuration();

                foe.receiveDamage(STANDARD_ATTACK_DAMAGE);

                if (levelScreen.isBulletTime()) {
                    comboSound.play(.1f);
                } else {
                    hitSound.play(.1f);
                }

                attackReleased = true;
            }
        }
        return attackReleased;
    }

    public void receiveDamage() {
        if (!dying) {
            damageImageDuration = DAMAGE_DURATION;
            failDashSound.play();
            if (life > 0) {
                life--;
                if (!DEBUG_IMMORTAL && life <= 0) {
                    die();
                }
            }
        }
    }

    public void die() {
        deadSound.play();
        dying = true;
        setAnimation(deadAnimation);
    }

    public int getLife() {
        return life;
    }

    public boolean isFoeOnScope(Foe foe) {
        return (auxPosition.dst2(foe.getAuxPosition()) <= scopeRadio2);
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isCoverLeft() {
        return coverDirection.isLeft();
    }

    public boolean isCoverUp() {
        return coverDirection.isUp();
    }

    public boolean isCoverRight() {
        return coverDirection.isRight();
    }

    public boolean isCoverDown() {
        return coverDirection.isDown();
    }

    @Override
    public void draw(Batch batch) {
        dashHalo.draw(batch);
        attackHalo.draw(batch);
        super.draw(batch);

        if (damageImageDuration > 0) {
            damageImageDuration--;
            damageImage.draw(batch);
        }
    }

}
