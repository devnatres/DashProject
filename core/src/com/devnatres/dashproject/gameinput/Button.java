package com.devnatres.dashproject.gameinput;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DnaAnimation;
import com.devnatres.dashproject.GlobalAudio;
import com.devnatres.dashproject.agents.Agent;

/**
 * Created by DevNatres on 17/01/2015.
 */
public class Button extends Agent {
    private float centerX;
    private float centerY;
    private DnaAnimation standbyAnimation;
    private DnaAnimation pushAnimation;
    private final IButtonExecutable executable;
    private final Sound sound;
    private boolean isAutomaticSoundOn = true;
    private boolean isPushed = false;
    private float elapsedTime;

    /**
     *
     * @param centerX center x coordinate
     * @param centerY center y coordinate
     * @param standbyAnimation mandatory standby animation
     * @param pushAnimation optional push animation, can be null
     * @param sound sound to be played when pushed
     * @param elapsedTime time to wait after pushed (push animation will be displayed if any)
     * @param executable executable object to call its execute(actionId) method when pushed and after elapsed time
     */
    public Button(float centerX, float centerY,
                  DnaAnimation standbyAnimation,
                  DnaAnimation pushAnimation,
                  Sound sound,
                  int elapsedTime,
                  IButtonExecutable executable) {

        super(standbyAnimation);
        this.standbyAnimation = standbyAnimation;

        this.centerX = centerX;
        this.centerY = centerY;
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
            if (touchPoint != null && auxArea.contains(touchPoint)) {
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

    public void setAutomaticSoundOn() {
        isAutomaticSoundOn = true;
    }

    public void setAutomaticSoundOff() {
        isAutomaticSoundOn = false;
    }

    public void playSound() {
        GlobalAudio.play(sound);
    }
}
