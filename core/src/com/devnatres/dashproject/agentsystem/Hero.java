package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.agentsystem.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.space.DirectionSelector;
import com.devnatres.dashproject.tools.VectorPool;

/**
 * High level entity that represents the hero.<br>
 * <br>
 * Created by DevNatres on 06/12/2014.
 */
public class Hero extends Agent {
    private static final int DAMAGE_DURATION = 15;
    private static final int MAX_LIFE = 5;
    private static final int STANDARD_ATTACK_DAMAGE = 1;
    private static final float ATTACK_RADIO = 64;
    private static final float ATTACK_RADIO2 = ATTACK_RADIO * ATTACK_RADIO;

    private boolean dying;
    private int life = MAX_LIFE;
    private int immunityDuration;
    private int extraDashDuration;

    private final DirectionSelector coverDirection;
    private final DirectionSelector lowCoverDirection;
    private final Rectangle nextVolumeRect;
    private final Vector2 nextPositionFromInput = new Vector2();
    private boolean thereIsNextPosFromInput;
    private Array<Foe> attackedFoes = new Array();

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Sound dashSound;
    private final Sound failDashSound;
    private final Sound hitSound;
    private final Sound comboSound;
    private final Sound deadSound;
    private final Sprite dashHalo_normal;
    private final Sprite dashHalo_extra;
    private float dashRadio;
    private Sprite dashHalo;
    private final float scopeRadio;
    private final float scopeRadio2;
    private final Sprite damageImage;
    private int damageImageDuration;
    private final DnaAnimation walkingAnimation;
    private final DnaAnimation crouchingAnimation;
    private final DnaAnimation deadAnimation;
    private final DnaAnimation attackingAnimation;
    private float attackingTime;

    private final LevelScreen levelScreen;
    private final LevelMap map;
    private final HyperStore hyperStore;

    static {
        VectorPool.initialize();
    }

    public Hero(LevelScreen levelScreen, HyperStore hyperStore) {
        super(EAnimation.HERO_WALKING.create(hyperStore));

        this.hyperStore = hyperStore;
        this.levelScreen = levelScreen;

        walkingAnimation = getAnimation();
        crouchingAnimation = EAnimation.HERO_CROUCHING.create(hyperStore);
        attackingAnimation = EAnimation.HERO_ATTACKING.create(hyperStore);
        deadAnimation = EAnimation.HERO_DYING.create(hyperStore);

        dashSound = hyperStore.getSound("sounds/dash.ogg");
        failDashSound = hyperStore.getSound("sounds/fail_hit.ogg");
        hitSound = hyperStore.getSound("sounds/hit.ogg");
        comboSound = hyperStore.getSound("sounds/combo.ogg");
        deadSound = hyperStore.getSound("sounds/hit.ogg");

        coverDirection = new DirectionSelector();
        lowCoverDirection = new DirectionSelector();
        nextVolumeRect = new Rectangle(getVolumeRectangle());

        dashHalo_normal = new Sprite(hyperStore.getTexture("dash_radio.png"));
        dashHalo_extra = new Sprite(hyperStore.getTexture("dash_radio_extra.png"));
        setDashHalo(dashHalo_normal);

        scopeRadio = dashRadio + ATTACK_RADIO;
        scopeRadio2 = scopeRadio * scopeRadio;

        damageImage = new Sprite(hyperStore.getTexture("shoot_damage.png"));

        centerAccessories();

        map = levelScreen.getMap();
        map.updateTheseCoverDirections(getVolumeRectangle(), coverDirection, lowCoverDirection);
    }

    @Override
    public void positionChanged() {
        super.positionChanged();
        if (dashHalo != null) centerAccessories();
        map.updateTheseCoverDirections(getVolumeRectangle(), coverDirection, lowCoverDirection);
    }

    private void centerAccessories() {
        dashHalo.setCenter(getCenterX(), getCenterY());
        damageImage.setCenter(getCenterX(), getCenterY());
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
        if (!thereIsNextPosFromInput) return false;

        thereIsNextPosFromInput = false;

        Vector2 vTarget = VectorPool.get();
        vTarget.set(targetCenterX, targetCenterY);
        vTarget.sub(getCenter());
        vTarget.limit(dashRadio);
        targetCenterX = getCenterX() + vTarget.x;
        targetCenterY = getCenterY() + vTarget.y;

        float x = targetCenterX - getVolumeWidth()/2;
        float y = targetCenterY - getVolumeHeight()/2;
        nextVolumeRect.set(x, y, getVolumeWidth(), getVolumeHeight());

        if (map.slide(nextVolumeRect)) {
            Agent dashShadow = new TransientAgent(EAnimation.HERO_DASHING.create(hyperStore));
            dashShadow.setCenter(getCenterX(), getCenterY());
            levelScreen.register(dashShadow, EAgentLayer.TRUNK);

            nextVolumeRect.setPosition(limitPositionX(nextVolumeRect.x), limitPositionY(nextVolumeRect.y));
            setCenter(nextVolumeRect.x + nextVolumeRect.width / 2, nextVolumeRect.y + nextVolumeRect.height / 2);

            globalAudio.play(dashSound, .1f);
        } else {
            globalAudio.play(failDashSound);
        }

        VectorPool.recycle(vTarget);
        return true;
    }

    private float limitPositionX(float x) {
        if (x < 0) {
            x = 0;
        } else if (x + getVolumeWidth() >= map.getMapPixelWidth()) {
            x = map.getMapPixelWidth() - getVolumeWidth() - 1f;
        }
        return x;
    }

    private float limitPositionY(float y) {
        if (y < 0) {
            y = 0;
        } else if (y + getVolumeHeight() >= map.getMapPixelHeight()) {
            y = map.getMapPixelHeight() - getVolumeHeight() - 1f;
        }
        return y;
    }

    @Override
    public void act(float delta) {
        if (dying) {
            getAnimation().updateStateTime();
            if (getAnimation().isAnimationFinished()) setVisible(false);
        } else {
            super.act(delta);
            boolean moved = setNextPosIfAvailable(nextPositionFromInput.x, nextPositionFromInput.y);
            if (moved) {
                Array<Foe> attackedFoes = attack();
                if (attackedFoes.size == 0) levelScreen.deactivateBulletTime();
            }

            if (attackingTime > 0) {
                attackingTime--;
                if (attackingTime == 0) assureNormalAnimation();
            } else {
                assureNormalAnimation();
            }

            if (immunityDuration > 0) immunityDuration--;

            if (extraDashDuration > 0) {
                extraDashDuration--;
                if (extraDashDuration == 0) {
                    setDashHalo(dashHalo_normal);
                    centerAccessories();
                }
            }
        }
    }

    private void assureNormalAnimation() {
        if (lowCoverDirection.hasDirection()) {
            if (getAnimation() != crouchingAnimation) {
                setAnimation(crouchingAnimation);
            }
        } else if (getAnimation() != walkingAnimation) {
            setAnimation(walkingAnimation);
        }
    }

    /**
     * Attack any nearby foe.
     * @return the attacked foes
     */
    private Array<Foe> attack() {
        attackedFoes.clear();
        Horde globalHorde = levelScreen.getGlobalHorde();
        for (int i = 0, n = globalHorde.size(); i < n; i++) {
            Foe foe = globalHorde.getFoe(i);
            if (!foe.isDying() && isFoeOnAttackRadio(foe) && isFoeInSight(foe)) {
                setAnimation(attackingAnimation);
                attackingTime = attackingAnimation.getAnimationDuration();
                foe.receiveDamage(STANDARD_ATTACK_DAMAGE);

                if (levelScreen.isBulletTime()) {
                    globalAudio.play(comboSound, .1f);
                } else {
                    globalAudio.play(hitSound, .1f);
                }

                attackedFoes.add(foe);
            }
        }

        levelScreen.restoreNotAttackedFoesAccordingToComboLivingFoes(attackedFoes);

        return attackedFoes;
    }

    private boolean isFoeOnAttackRadio(Foe foe) {
        return getVolumePosition().dst2(foe.getPosition()) <= ATTACK_RADIO2;
    }

    private boolean isFoeInSight(Foe foe) {
        final float heroLowX = getVolumeX();
        final float heroHighX = getVolumeX() + getVolumeWidth() - 1;
        final float heroLowY = getVolumeY();
        final float heroHighY = getVolumeY() + getVolumeHeight() - 1;

        final float foeLowX = foe.getVolumeX();
        final float foeHighX = foe.getVolumeX() + foe.getVolumeWidth() - 1;
        final float foeLowY = foe.getVolumeY();
        final float foeHighY = foe.getVolumeY() + foe.getVolumeHeight() - 1;

        return map.isLineCollision(heroLowX, heroLowY, foeLowX, foeLowY)
                || map.isLineCollision(heroHighX, heroHighY, foeHighX, foeHighY);
    }

    public void receiveDamage(int damage) {
        if (!dying && immunityDuration == 0) {
            damageImageDuration = DAMAGE_DURATION;
            globalAudio.play(failDashSound);
            if (life > 0) {
                life -= damage;
                if (life < 0) life = 0;

                if (Debug.IMMORTAL) return;

                if (life == 0) die();
            }
        }
    }

    public void die() {
        if (!dying) {
            globalAudio.play(deadSound);
            dying = true;
            setAnimation(deadAnimation);
        }
    }

    public int getLife() {
        return life;
    }

    public void addLife(int extraLife) {
        life += extraLife;
        if (life > MAX_LIFE) life = MAX_LIFE;
    }

    public boolean hasMaxLife() {
        return life == MAX_LIFE;
    }

    public void activateExtraDash(int duration) {
        extraDashDuration = duration;
        setDashHalo(dashHalo_extra);
        centerAccessories();
    }

    private void setDashHalo(Sprite newDashHalo) {
        dashHalo = newDashHalo;
        dashRadio = dashHalo.getWidth() / 2;
    }

    public boolean hasExtraDash() {
        return extraDashDuration > 0;
    }

    public void addImmunity(int duration) {
        immunityDuration = duration;
    }

    public boolean hasImmunity() {
        return immunityDuration > 0;
    }

    public boolean isFoeOnScope(Foe foe) {
        return (getVolumePosition().dst2(foe.getPosition()) <= scopeRadio2);
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

        if (immunityDuration > 0) {
            Color color = batch.getColor();
            float alpha = color.a;
            color.a = .5f;
            batch.setColor(color);

            super.draw(batch);

            color.a = alpha;
            batch.setColor(color);
        } else {
            super.draw(batch);
        }

        if (damageImageDuration > 0) {
            damageImageDuration--;
            damageImage.draw(batch);
        }
    }

}
