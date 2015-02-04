package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.GlobalAudio;
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
    private final DirectionSelector lowCoverDirection;
    private final Rectangle nextArea;

    private final float attackRadio;
    private final float attackRadio2;
    private final Sprite attackHalo; // TODO Only for debugging. Remove.

    private final float dashRadio;
    private final Sprite dashHalo;

    private final float scopeRadio;
    private final float scopeRadio2;

    private final Animation walkingAnimation;
    private final Animation crouchingAnimation;
    private final Animation attackingAnimation;
    private float attackingTime;

    private final LevelScreen levelScreen;

    private final HyperStore hyperStore;

    private final Sprite damageImage;

    private final Vector2 nextPositionFromInput = new Vector2();
    private boolean thereIsNextPosFromInput;

    private final Animation deadAnimation;
    private boolean dying;

    private final LevelMap map;

    static {
        VectorPool.initialize();
    }

    public Hero(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.HERO_WALKING.create(hyperStore));
        //setSize(64, 128);

        this.hyperStore = hyperStore;

        this.levelScreen = levelScreen;

        walkingAnimation = getAnimation();
        crouchingAnimation = EAnimations.HERO_CROUCHING.create(hyperStore);
        attackingAnimation = EAnimations.HERO_ATTACKING.create(hyperStore);

        dashSound = hyperStore.getSound("sounds/dash.ogg");
        failDashSound = hyperStore.getSound("sounds/fail_hit.ogg");
        hitSound = hyperStore.getSound("sounds/hit.ogg");
        comboSound = hyperStore.getSound("sounds/combo.ogg");
        deadSound = hyperStore.getSound("sounds/hit.ogg");

        coverDirection = new DirectionSelector();
        lowCoverDirection = new DirectionSelector();
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

        map = levelScreen.getMap();
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

            //dashSound.play(.1f);
            GlobalAudio.play(dashSound, .1f);

            nextArea.setPosition(limitPositionX(nextArea.x), limitPositionY(nextArea.y));
            setPosition(nextArea.x, nextArea.y);
            map.updateCoverDirection(auxArea, coverDirection, lowCoverDirection);
        } else {
            //failDashSound.play();
            GlobalAudio.play(failDashSound);
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
            if (!attackReleased && moved) {
                levelScreen.deactivateBulletTime();
            }

            if (attackingTime > 0) {
                attackingTime--;
                if (attackingTime == 0) {
                    assureNormalAnimation();
                }
            } else {
                assureNormalAnimation();
            }
        }
    }

    private void assureNormalAnimation() {
        Animation animation = getAnimation();
        if (lowCoverDirection.hasDirection()) {
            if (animation != crouchingAnimation) {
                setAnimation(crouchingAnimation);
            }
        } else if (animation != walkingAnimation) {
            setAnimation(walkingAnimation);
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
            if (!foe.isDying() && isFoeOnAttackRadio(foe) && isFoeInSight(foe)) {
                setAnimation(attackingAnimation);
                attackingTime = attackingAnimation.getAnimationDuration();

                foe.receiveDamage(STANDARD_ATTACK_DAMAGE);

                if (levelScreen.isBulletTime()) {
                    //comboSound.play(.1f);
                    GlobalAudio.play(comboSound, .1f);
                } else {
                    //hitSound.play(.1f);
                    GlobalAudio.play(hitSound, .1f);
                }

                levelScreen.activateBulletTime(); // Reboot if it was activated before
                attackReleased = true;
            }
        }
        return attackReleased;
    }

    private boolean isFoeOnAttackRadio(Foe foe) {
        return auxPosition.dst2(foe.getAuxPosition()) <= attackRadio2;
    }

    private boolean isFoeInSight(Foe foe) {
        final float heroLowX = auxArea.x;
        final float heroHighX = auxArea.x + auxArea.width - 1;
        final float heroLowY = auxArea.y;
        final float heroHighY = auxArea.y + auxArea.height - 1;

        final float foeLowX = foe.getX();
        final float foeHighX = foe.getAuxPosition().x + foe.getWidth() - 1;
        final float foeLowY = foe.getY();
        final float foeHighY = foe.getY() + foe.getHeight() - 1;

        return map.isLineCollision(heroLowX, heroLowY, foeLowX, foeLowY)
                || map.isLineCollision(heroHighX, heroHighY, foeHighX, foeHighY);
    }

    public void receiveDamage(int damage) {
        if (!dying) {
            damageImageDuration = DAMAGE_DURATION;
            //failDashSound.play();
            GlobalAudio.play(failDashSound);
            if (life > 0) {
                life -= damage;
                if (!Debug.IMMORTAL && life <= 0) {
                    die();
                }
            }
        }
    }

    public void die() {
        //deadSound.play();
        GlobalAudio.play(deadSound);
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
