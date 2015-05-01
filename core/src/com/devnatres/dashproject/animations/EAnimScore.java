package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimScore implements IAnimCreator {
    SCORE_50 {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "score/score_50.png", 1, 1, SCORE_DURATION, Animation.PlayMode.NORMAL);
        }
    },
    SCORE_75 {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "score/score_75.png", 1, 1, SCORE_DURATION, Animation.PlayMode.NORMAL);
        }
    },
    SCORE_100 {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "score/score_100.png", 1, 1, SCORE_DURATION, Animation.PlayMode.NORMAL);
        }
    },
    SCORE_150 {
        public DnaAnimation create(HyperStore hyperStore) {
            return AnimTools.create(hyperStore, "score/score_150.png", 1, 1, SCORE_DURATION, Animation.PlayMode.NORMAL);
        }
    },
    ;
    private static int SCORE_DURATION = 90;
}
