package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimFoe implements IAnimCreator {
    FOE_ROBOT_WALKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "foe_robot_walking.png", 2, 1, 10f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    FOE_ROBOT_DYING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "foe_robot_cutting.png", 2, 1, 30f, Animation.PlayMode.NORMAL);
        }
    },

    FOE_TANK_WALKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "foe_tank_walking.png", 2, 1, 15f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    FOE_TANK_DYING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "foe_tank_cutting.png", 2, 1, 30f, Animation.PlayMode.NORMAL);
        }
    },
    FOE_TANK_STUNNING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "foe_tank_stunning.png", 2, 1, 5f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    MINE {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mine.png", 3, 1, 10, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    MINE_EXPLOSION {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mine_explosion.png", 3, 1, 10, Animation.PlayMode.NORMAL);
        }
    },
}
