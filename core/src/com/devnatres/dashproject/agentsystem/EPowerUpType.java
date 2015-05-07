package com.devnatres.dashproject.agentsystem;

import com.devnatres.dashproject.animations.EAnimPowerUp;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * PowerUp types with theirs specific behaviours.<br>
 * <br>
 * Created by DevNatres on 21/02/2015.
 */
enum EPowerUpType {
    TIME {
        @Override
        boolean isConvenient(LevelScreen levelScreen) {
            return true;
        }

        @Override
        DnaAnimation getAnimation(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP.create(hyperStore);
        }

        @Override
        void activateEffect(LevelScreen levelScreen) {
            levelScreen.addTime(PowerUp.EXTRA_TIME);
        }

        @Override
        DnaAnimation getMessage(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP_MESSAGE_TIME.create(hyperStore);
        }
    },
    LIFE {
        @Override
        boolean isConvenient(LevelScreen levelScreen) {
            return !levelScreen.getHero().hasMaxLife();
        }

        @Override
        DnaAnimation getAnimation(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP.create(hyperStore);
        }

        @Override
        void activateEffect(LevelScreen levelScreen) {
            levelScreen.getHero().addLife(PowerUp.EXTRA_LIFE);
        }

        @Override
        DnaAnimation getMessage(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP_MESSAGE_LIFE.create(hyperStore);
        }
    },
    DASH {
        @Override
        boolean isConvenient(LevelScreen levelScreen) {
            return !levelScreen.getHero().hasExtraDash();
        }

        @Override
        DnaAnimation getAnimation(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP.create(hyperStore);
        }

        @Override
        void activateEffect(LevelScreen levelScreen) {
            levelScreen.getHero().activateExtraDash(PowerUp.EXTRA_DASH_DURATION);
        }

        @Override
        DnaAnimation getMessage(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP_MESSAGE_DASH.create(hyperStore);
        }
    },
    IMMUNITY {
        @Override
        boolean isConvenient(LevelScreen levelScreen) {
            return !levelScreen.getHero().hasImmunity();
        }

        @Override
        DnaAnimation getAnimation(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP.create(hyperStore);
        }

        @Override
        void activateEffect(LevelScreen levelScreen) {
            levelScreen.getHero().activateImmunity(PowerUp.IMMUNITY_DURATION);
        }

        @Override
        DnaAnimation getMessage(HyperStore hyperStore) {
            return EAnimPowerUp.POWER_UP_MESSAGE_IMMUNITY.create(hyperStore);
        }
    };
    abstract boolean isConvenient(LevelScreen levelScreen);
    abstract DnaAnimation getAnimation(HyperStore hyperStore);
    abstract void activateEffect(LevelScreen levelScreen);
    abstract DnaAnimation getMessage(HyperStore hyperStore);

    static EPowerUpType[] powerUpTypes = EPowerUpType.values();
}