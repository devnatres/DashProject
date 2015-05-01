package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public enum EAnimButton implements IAnimCreator {
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

    BUTTON_TUTORIAL_STANDBY {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_tutorial_standby.png");
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
    BUTTON_OPT_TUTORIAL {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_opt_tutorial.png");
        }
    },
    BUTTON_OPT_BACK {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_opt_back.png");
        }
    },

    BUTTON_MENU_RESUME {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_resume.png");
        }
    },
    BUTTON_MENU_RESET {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_reset.png");
        }
    },
    BUTTON_MENU_MENU {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_menu.png");
        }
    },
    BUTTON_MENU_EXIT {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_exit.png");
        }
    },
    BUTTON_MENU_SOUND {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_sound.png");
        }
    },
    BUTTON_MENU_YES {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_yes.png");
        }
    },
    BUTTON_MENU_NO {
        public DnaAnimation create(HyperStore hyperStore) {
            return createButtonAnimation(hyperStore, "buttons/button_menu_no.png");
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

    public static DnaAnimation createButtonAnimation(HyperStore hyperStore, String fileName) {
        return AnimTools.create(hyperStore, fileName, 3, 1, 5, Animation.PlayMode.LOOP_PINGPONG);
    }
}
