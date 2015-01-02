package com.devnatres.dashproject.agents;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 09/12/2014.
 */
public class Foe extends Agent {
    private static final int SHAKE_FRAME_DURATION = 4;

    Animation foeDeadAnimation;
    boolean dying;

    private final LevelScreen levelScreen;

    private final int shakeDisplacements[] = new int[]{-1, 1};
    private int shakeIndex;
    private int shakeFrameDuration;

    public Foe(HyperStore hyperStore, LevelScreen levelScreen) {
        super(EAnimations.FOE_ROBOT_WALKING.create(hyperStore));

        this.levelScreen = levelScreen;

        foeDeadAnimation = EAnimations.FOE_ROBOT_DEAD.create(hyperStore);

    }


    @Override
    public void act(float delta) {
        if (dying) {
            addStateTime(delta);
            if (getAnimation().isAnimationFinished(animationStateTime)) {
                setVisible(false);
            }
        } else {
            if (!isBulletTime(levelScreen)) {
                super.act(delta);
            }
        }
    }

    private boolean isBulletTime(LevelScreen levelScreen) {
        return levelScreen != null && levelScreen.isBulletTime();
    }

    public boolean isDying() {
        return dying;
    }

    /*public void actBulletTime(float delta) {
        if (dying) {
            act(delta);
        }
    }*/

    public void receiveDamage() {
        if (!dying) {
            dying = true;
            setAnimation(foeDeadAnimation);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDying()) {
            float originalX = getX();
            float originalY = getY();

            if (shakeFrameDuration < SHAKE_FRAME_DURATION) {
                shakeFrameDuration++;
            } else {
                shakeFrameDuration = 0;
                shakeIndex = (shakeIndex < shakeDisplacements.length - 1) ? shakeIndex + 1 : 0;
            }
            setPosition(originalX + shakeDisplacements[shakeIndex], originalY);
            super.draw(batch, parentAlpha);
            setPosition(originalX, originalY);
        } else {
            super.draw(batch, parentAlpha);
        }
    }

}
