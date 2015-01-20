package com.devnatres.dashproject.gameconstants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DnaAnimation;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 15/12/2014.
 */
public enum EAnimations {
    HERO_WALKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_standing.png", 3, 1, 10f, PlayMode.LOOP_PINGPONG);
        }
    },
    HERO_CROUCHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_crouched.png", 1, 1, 10f, PlayMode.LOOP_PINGPONG);
        }
    },
    HERO_ATTACKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_attacking.png", 2, 1, 15f, PlayMode.NORMAL);
        }
    },
    HERO_DASHING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_dashing.png", 3, 1, 10f, PlayMode.NORMAL);
        }
    },
    HERO_DYING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "mark_dying.png", 3, 1, 15f, PlayMode.NORMAL);
        }
    },

    FOE_ROBOT_WALKING {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "foe_robot_walking.png", 2, 1, 10f, PlayMode.LOOP_PINGPONG);
        }
    },

    FOE_ROBOT_DEAD {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "foe_robot_cutting.png", 2, 1, 30f, PlayMode.NORMAL);
        }
    },

    SCORE_50 {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "score/score_50.png", 1, 1, SCORE_DURATION, PlayMode.NORMAL);
        }
    },

    SCORE_100 {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "score/score_100.png", 1, 1, SCORE_DURATION, PlayMode.NORMAL);
        }
    },

    RADAR_INDICATOR {
        public DnaAnimation create(HyperStore hyperStore) {
            return createAnimation(hyperStore, "radar_indicator.png", 4, 1, 5, PlayMode.LOOP_PINGPONG);
        }
    },

    BUTTON_PLAY_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_play_standby.png");
        }
    },
    BUTTON_PLAY_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_play_pushed.png");
        }
    },

    BUTTON_OPTIONS_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_options_standby.png");
        }
    },
    BUTTON_OPTIONS_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_options_pushed.png");
        }
    },

    BUTTON_CREDITS_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_credits_standby.png");
        }
    },
    BUTTON_CREDITS_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_credits_pushed.png");
        }
    },

    BUTTON_EXIT_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_exit_standby.png");
        }
    },
    BUTTON_EXIT_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_exit_pushed.png");
        }
    },

    BUTTON_GO_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_go_standby.png");
        }
    },
    BUTTON_GO_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_go_pushed.png");
        }
    },

    BUTTON_BACK_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_back_standby.png");
        }
    },
    BUTTON_BACK_PUSHED {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_back_pushed.png");
        }
    },

    BUTTON_OPT_SOUND {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_opt_sound.png");
        }
    },
    BUTTON_OPT_CAMERA {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_opt_camera.png");
        }
    },
    BUTTON_OPT_BACK {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_opt_back.png");
        }
    },

    BUTTON_ARROW_UP {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_arrow_up.png");
        }
    },
    BUTTON_ARROW_UP2 {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_arrow_up2.png");
        }
    },
    BUTTON_ARROW_DOWN {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_arrow_down.png");
        }
    },
    BUTTON_ARROW_DOWN2 {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_arrow_down2.png");
        }
    },
    ;
    private static int SCORE_DURATION = 90;

    public static DnaAnimation createButtonAnimation(HyperStore hyperStore, String fileName) {
        return createAnimation(hyperStore, fileName, 3, 1, 5, PlayMode.LOOP_PINGPONG);
    }

    abstract public DnaAnimation create(HyperStore hyperStore);

    static private DnaAnimation createAnimation(HyperStore hyperStore,
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

        DnaAnimation animation = new DnaAnimation(frameDuration, frames, playMode);
        return animation;
    }
}
