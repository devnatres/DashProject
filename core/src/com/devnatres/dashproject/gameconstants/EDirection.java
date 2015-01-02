package com.devnatres.dashproject.gameconstants;

import com.devnatres.dashproject.space.DirectionSelector;

/**
* Created by DevNatres on 07/12/2014.
*/
public enum EDirection {
    UP {
        @Override
        public int getXUnit() {
            return 0;
        }

        @Override
        public int getYUnit() {
            return 1;
        }

        @Override
        public void setDirection(DirectionSelector directionSelector) {
            directionSelector.setUp();
        }
    },
    RIGHT {
        @Override
        public int getXUnit() {
            return 1;
        }

        @Override
        public int getYUnit() {
            return 0;
        }

        @Override
        public void setDirection(DirectionSelector directionSelector) {
            directionSelector.setRight();
        }
    },
    DOWN {
        @Override
        public int getXUnit() {
            return 0;
        }

        @Override
        public int getYUnit() {
            return -1;
        }

        @Override
        public void setDirection(DirectionSelector directionSelector) {
            directionSelector.setDown();
        }
    },
    LEFT {
        @Override
        public int getXUnit() {
            return -1;
        }

        @Override
        public int getYUnit() {
            return 0;
        }

        @Override
        public void setDirection(DirectionSelector directionSelector) {
            directionSelector.setLeft();
        }
    };

    abstract public int getXUnit();
    abstract public int getYUnit();
    abstract public void setDirection(DirectionSelector directionSelector);
}
