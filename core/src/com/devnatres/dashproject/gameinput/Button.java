package com.devnatres.dashproject.gameinput;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * High level entity that represent button.<br>
 *     <br>
 * Created by DevNatres on 17/01/2015.
 */
public class Button extends Agent {
    private DnaAnimation standbyAnimation;
    private DnaAnimation pushAnimation;
    private final IButtonExecutable executable;
    private final Sound sound;
    private boolean isAutomaticSoundOn = true;
    private boolean isPushed = false;
    private float elapsedTime;

    /**
     * Button constructor's.
     * @param centerX center x coordinate
     * @param centerY center y coordinate
     * @param standbyAnimation mandatory standby animation
     * @param pushAnimation optional push animation, can be null
     * @param sound sound to be played when pushed
     * @param elapsedTime time to wait after pushed (push animation will be displayed if any)
     * @param executable callback. The execute(actionId) method will be called when pushed and after elapsed time
     */
    public Button(float centerX, float centerY,
                  DnaAnimation standbyAnimation,
                  DnaAnimation pushAnimation,
                  Sound sound,
                  int elapsedTime,
                  IButtonExecutable executable) {

        super(standbyAnimation);
        this.standbyAnimation = standbyAnimation;
        setCenter(centerX, centerY);
        this.pushAnimation = pushAnimation;
        this.sound = sound;
        this.elapsedTime = elapsedTime;
        this.executable = executable;
    }

    public void act(float delta, Vector2 touchPoint) {
        super.act(delta);

        if (isPushed) {
            executeCountDown(delta);
        } else {
            if (touchPoint != null && getVolumeRectangle().contains(touchPoint)) {
                isPushed = true;
                if (isAutomaticSoundOn) {
                    GlobalAudio.play(sound);
                }
                if (pushAnimation != null) {
                    setAnimation(pushAnimation);
                }
                executeCountDown(delta);
            }
        }
    }

    private void executeCountDown(float delta) {
        if (elapsedTime <= 0) {
            executable.execute(this);
            isPushed = false;
            setAnimation(standbyAnimation);
        } else {
            elapsedTime -= delta;
        }
    }

    public void setAutomaticSoundOff() {
        isAutomaticSoundOn = false;
    }

    public void playSound() {
        GlobalAudio.play(sound);
    }
}
