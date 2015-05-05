package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimPowerUp implements IAnimCreator {
    POWER_UP {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "power_up_anim.png", 8, 1, 5, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    POWER_UP_MESSAGE_TIME {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "power_up_message_time.png", 1, 1, 45, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    POWER_UP_MESSAGE_LIFE {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "power_up_message_life.png", 1, 1, 45, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    POWER_UP_MESSAGE_DASH {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "power_up_message_dash.png", 1, 1, 45, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    POWER_UP_MESSAGE_IMMUNITY {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "power_up_message_immunity.png", 1, 1, 45, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
}
