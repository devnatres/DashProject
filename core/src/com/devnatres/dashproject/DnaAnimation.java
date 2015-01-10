package com.devnatres.dashproject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.gameconstants.Time;

/**
 * Created by DevNatres on 07/01/2015.
 */
public class DnaAnimation extends Animation {
    private float currentStateTime;

    public DnaAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public DnaAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public DnaAnimation(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    public float updateCurrentStateTime() {
        if (currentStateTime >= Time.MAX_TIME_FLOAT) {
            currentStateTime = 0;
        } else {
            currentStateTime++;
        }

        return currentStateTime;
    }

    public TextureRegion getCurrentKeyFrame() {
        return getKeyFrame(currentStateTime);
    }
}
