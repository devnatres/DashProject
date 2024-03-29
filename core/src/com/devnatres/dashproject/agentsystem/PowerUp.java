package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.tools.Tools;


/**
 * High level class to represent power-ups. It should be created with PowerUpGenerator. <br>
 * <br>
 * Created by DevNatres on 10/02/2015.
 */
class PowerUp extends Agent {
    static final int POWER_UP_RADIO2 = 54 * 54;
    private static final float FLYING_SPEED = 15f;
    private static final int MESSAGE_DURATION = 45;

    static final float EXTRA_TIME = 1.2f;
    static final int EXTRA_LIFE = 3;
    static final int IMMUNITY_DURATION = 300;
    static final int IMMUNITY_DURATION_SHORT = 100;
    static final int EXTRA_DASH_DURATION = 600;
    static final int EXTRA_DASH_DURATION_SHORT = 150;

    private final Hero hero;
    private final EPowerUpType type;
    private final LevelScreen levelScreen;
    private final Agent messageAgent;
    private boolean flying;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Sound catchSound;
    private final Sound jumpSound;

    private boolean jumped;

    PowerUp(LevelScreen levelScreen,
                    HyperStore hyperStore,
                    EPowerUpType type,
                    Vector2 sourceCenter,
                    Vector2 targetCenter) {
        super(type.getAnimation(hyperStore));

        setCenter(sourceCenter);

        this.type = type;
        this.levelScreen = levelScreen;

        hero = levelScreen.getHero();
        catchSound = hyperStore.getSound("sounds/power_up_catch.ogg");
        jumpSound = hyperStore.getSound("sounds/power_up_jump.ogg");

        messageAgent = new Agent(type.getMessage(hyperStore));
        flying = true;

        Vector2 sourcePosition = new Vector2(sourceCenter).add(-getWidth() / 2, -getHeight() / 2);
        Vector2 targetPosition = new Vector2(targetCenter).add(-getWidth() / 2, -getHeight() / 2);
        Action moveAction = Tools.getMoveToAction(sourcePosition, targetPosition, FLYING_SPEED);
        addAction(moveAction);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (flying) {
            if (getActions().size == 0) {
                flying = false;
            }
        } else {
            if (!jumped) {
                jumped = true;
                globalAudio.play(jumpSound, .5f);
            }

            float distance2 = hero.getCenter().dst2(getCenter());
            if (distance2 <= POWER_UP_RADIO2) {
                type.activateEffect(levelScreen);
                levelScreen.setAgentMessage(messageAgent, MESSAGE_DURATION);

                globalAudio.play(catchSound, .5f);
                setVisible(false);
            }
        }
    }
}
