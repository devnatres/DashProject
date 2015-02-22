package com.devnatres.dashproject.gameconstants;

/**
 * Class for time constants. <br>
 *     <br>
 * Created by DevNatres on 16/12/2014.
 */
public final class Time {
    public static final float FPS = 60f;
    public static final float FPS_TIME = 1 / FPS;
    public static final float NANO_TIME = 1.0e9f; // 1x(10^9)
    public static final float FRAME = 1f; // delta value
    public static final float FAST_CAMERA_SPEED = 15f;
    public static final float MEDIUM_CAMERA_SPEED = 10f;
    public static final float BULLET_TIME = 40f;
    public static final float MAX_TIME_FLOAT = Float.MAX_VALUE - FRAME - 1f; // -1f to avoid round errors

    private Time() {}
}
