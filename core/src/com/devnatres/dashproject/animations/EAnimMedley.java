package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimMedley implements IAnimCreator {
    RADAR_RIGHT {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "hub/radar_right.png", 1, 1, 8, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    RADAR_UP {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "hub/radar_up.png", 1, 1, 8, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    RADAR_RIGHT_UP {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "hub/radar_rightup.png", 1, 1, 8, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    NUMBERS_GOLD {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "numbers/numbers_gold.png", 1, 2, 10, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    NUMBERS_SILVER {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "numbers/numbers_silver.png", 1, 2, 10, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    DAMAGE_SOFT_FLASHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "fx/damage_soft_flashing.png", 2, 1, 5, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    DAMAGE_HARD_FLASHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "fx/damage_hard_flashing.png", 2, 1, 5, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    TROPHY_LIGHTING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "trophies/trophy_lighting.png", 4, 1, 7, Animation.PlayMode.NORMAL);
        }
    },
}
