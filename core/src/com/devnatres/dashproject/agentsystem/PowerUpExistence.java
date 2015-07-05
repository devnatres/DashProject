package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper to manage the life of a power-up and its alpha.
 * <br>
 * Created by DevNatres on 08/05/2015.
 */
class PowerUpExistence {
    private static final int FLICK_DURATION = 5;

    private int duration;
    private int durationShort;
    private boolean show;
    private final Sound powerUpEndingSound;
    private final GlobalAudio globalAudio = GlobalAudio.getInstance();

    public PowerUpExistence(HyperStore hyperStore) {
        powerUpEndingSound = hyperStore.getSound("sounds/power_up_ending.ogg");
    }

    /**
     * Return true if the power up is still alive, false otherwise.
     */
    public boolean update() {
        if (duration > 0) {
            duration--;
            if (duration == 0) {
                show = false;
                return false;
            } else if (duration == durationShort) {
                globalAudio.play(powerUpEndingSound, .5f);
            } else if ((duration < durationShort) && ((duration%FLICK_DURATION) == 0)) {
                show = !show;
            }
        }
        return true;
    }

    public void initialize(int duration, int durationShort, boolean initialShowValue) {
        this.duration = duration;
        this.durationShort = durationShort;
        show = initialShowValue;
    }

    public int getRemainingDuration() {
        return duration;
    }

    public boolean mustShow() {
        return show;
    }
}
