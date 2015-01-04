package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.Debug;
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
    private static final int HEAL_DURATION = 30;

    private int damageImageDuration;
    private int healDuration = HEAL_DURATION * 2;
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

    private final Animation walkingAnimation;
    private final Animation attackingAnimation;
    private float attackingTime;

    private final LevelScreen levelScreen;

    private final HyperStore hyperStore;

    private final Sprite damageImage;
    private final Sprite healPointImage;

    private final Vector2 nextPositionFromInput = new Vector2();
    private boolean thereIsNextPositionFromInput;

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

        damageImage = new Sprite(hyperStore.getTexture("shoot_damage.png"));
        healPointImage = new Sprite(hyperStore.getTexture("heal_point.png"));

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

    public void programNextPosition(float nextX, float nextY) {
        nextPositionFromInput.set(nextX, nextY);
        thereIsNextPositionFromInput = true;
    }

    private void setNextPositionIfAvailable(float targetCenterX, float targetCenterY) {
        if (Debug.DEBUG) Debug.addPoint(targetCenterX, targetCenterY, Color.WHITE);

        if (!thereIsNextPositionFromInput) {
            return;
        }

        thereIsNextPositionFromInput = false;

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
                float dashShadowX = getX() - (dashShadow.getWidth() - auxArea.width)/2;
                float dashShadowY = getY() - (dashShadow.getHeight() - auxArea.height)/2;
                dashShadow.setPosition(dashShadowX, dashShadowY);
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
            setNextPositionIfAvailable(nextPositionFromInput.x, nextPositionFromInput.y);
            tryAttack();

            if (attackingTime > 0) {
                attackingTime--;
                if (attackingTime == 0) {
                    setAnimation(walkingAnimation);
                }
            }
        }
    }

    private void tryAttack() {
        if (levelScreen == null) {
            return;
        }

        Horde totalHorde = levelScreen.getTotalHorde();
        for (int i = 0, n = totalHorde.size(); i < n; i++) {
            Foe foe = totalHorde.getFoe(i);
            if (foe.isVisible() && !foe.isDying() && (auxPosition.dst2(foe.getAuxPosition()) <= attackRadio2)) {

                setAnimation(attackingAnimation);
                attackingTime = attackingAnimation.getAnimationDuration();

                foe.receiveDamage();

                if (levelScreen.isBulletTime()) {
                    comboSound.play(.1f);
                } else {
                    hitSound.play(.1f);
                }

                levelScreen.activateBulletTime();
            }
        }
    }

    public void receiveDamage() {
        if (!dying) {
            damageImageDuration = DAMAGE_DURATION;
            failDashSound.play();
            if (life > 0) {
                life--;
                if (life == 0) {
                    deadSound.play();
                    dying = true;
                    setAnimation(deadAnimation);
                }
            }
            healDuration = HEAL_DURATION;
        }
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

        if (healDuration > 0) {
            healDuration--;
            float lifeWidth = healPointImage.getWidth() + 2;
            float lifeX = auxCenter.x -(life*lifeWidth/2);
            for (int i = 1; i <= life; i++) {
                healPointImage.setPosition(lifeX + (i-1)*lifeWidth,
                        getY() + getHeight() + 5);
                healPointImage.draw(batch);
            }
        }
    }

}
