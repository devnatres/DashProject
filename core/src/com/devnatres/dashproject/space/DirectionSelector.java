package com.devnatres.dashproject.space;

/**
 * Created by DevNatres on 07/12/2014.
 */
public class DirectionSelector {

    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int RIGHT = 4;
    private static final int DOWN = 8;

    private int coverDirection;

    public void clear() {
        coverDirection = 0;
    }

    public boolean isLeft() {
        return isDirection(LEFT);
    }

    public boolean isUp() {
        return isDirection(UP);
    }

    public boolean isRight() {
        return isDirection(RIGHT);
    }

    public boolean isDown() {
        return isDirection(DOWN);
    }

    public void setLeft() {
        coverDirection |= LEFT;
    }

    public void setUp() {
        coverDirection |= UP;
    }

    public void setDown() {
        coverDirection |= DOWN;
    }

    public void setRight() {
        coverDirection |= RIGHT;
    }

    private boolean isDirection(int coverMask) {
        return (coverDirection & coverMask) == coverMask;
    }
}
