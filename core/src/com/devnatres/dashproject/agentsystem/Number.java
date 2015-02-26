package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.tools.Tools;

/**
 * Created by DevNatres on 26/02/2015.
 */
public class Number extends Agent {
    public enum ENumberType {
        INTEGER {
            @Override
            int getDecimalDigits() {
                return 0;
            }
        },
        DECIMAL1 {
            @Override
            int getDecimalDigits() {
                return 1;
            }
        };
        abstract int getDecimalDigits();
    }

    private static final int PRECISION = 10;

    private float value;
    private ENumberType numberType;
    private final int decimalDigits;
    private final int integerDigits;
    private final float maxValue;
    TextureRegion[] regions = new TextureRegion[PRECISION];

    public Number(DnaAnimation animation, ENumberType numberType) {
        super(animation);
        this.numberType = (numberType == null) ? ENumberType.INTEGER : numberType;

        decimalDigits = Tools.limitInteger(this.numberType.getDecimalDigits(), 0, PRECISION-1);
        integerDigits = Tools.limitInteger(PRECISION - decimalDigits, 1, PRECISION);

        float valuesFromDigits = Tools.integerPow(10, decimalDigits);
        float maxDecimalValue = (valuesFromDigits - 1f) / valuesFromDigits;

        maxValue = Tools.integerPow(10, integerDigits) - 1f + maxDecimalValue;

        for (int i = 0; i < PRECISION; i++) {
            regions[i] = new TextureRegion();
        }

        updateNumberGraphics();
    }

    private void updateNumberGraphics() {
        // TODO
        TextureRegion frameRegion = getAnimation().getCurrentKeyFrame();




    }

    public void setNumberValue(float numberValue) {
        if (value != numberValue) {
            value = numberValue;
            updateNumberGraphics();
        }
    }
}
