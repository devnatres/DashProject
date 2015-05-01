package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimHero implements IAnimCreator {
    HERO_WALKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mark_standing.png", 3, 1, 10f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    HERO_CROUCHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mark_crouched.png", 1, 1, 10f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
    HERO_ATTACKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mark_attacking.png", 3, 1, 15f, Animation.PlayMode.NORMAL);
        }
    },
    HERO_DASHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mark_dashing.png", 3, 1, 10f, Animation.PlayMode.NORMAL);
        }
    },
    HERO_DYING {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "mark_dying.png", 9, 1, 10f, Animation.PlayMode.NORMAL);
        }
    },

    DASH_HALO_NORMAL {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "dash_halo_normal.png", 2, 1, 10f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },

    DASH_HALO_EXTRA {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "dash_halo_extra.png", 2, 1, 5f, Animation.PlayMode.LOOP_PINGPONG);
        }
    },
}
