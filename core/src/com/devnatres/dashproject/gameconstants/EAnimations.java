package com.devnatres.dashproject.gameconstants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by David on 15/12/2014.
 */
public enum EAnimations {
    HERO_WALKING {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_standing.png", 3, 1, 10f, PlayMode.LOOP_PINGPONG);
        }
    },
    HERO_ATTACKING {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_attacking.png", 2, 1, 15f, PlayMode.NORMAL);
        }
    },
    HERO_DASHING {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_dashing.png", 3, 1, 10f, PlayMode.NORMAL);
        }
    },
    HERO_DYING {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_dying.png", 3, 1, 15f, PlayMode.NORMAL);
        }
    },

    FOE_ROBOT_WALKING {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "foe_robot_walking.png", 2, 1, 10f, PlayMode.LOOP_PINGPONG);
        }
    },

    FOE_ROBOT_DEAD {
        public Animation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "foe_robot_cutting.png", 2, 1, 30f, PlayMode.NORMAL);
        }
    }
    ;
    abstract public Animation create(HyperStore hyperStore);

    static private Animation createAnimation(HyperStore hyperStore,
                                      String fileName,
                                      int columns, int rows,
                                      float frameDuration,
                                      PlayMode playMode) {

        Texture texture = hyperStore.getTexture(fileName);

        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth()/columns,
                texture.getHeight()/rows);

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames.add(tmp[i][j]);
            }
        }

        Animation animation = new Animation(frameDuration, frames, playMode);
        return animation;
    }
}
