package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.tools.Tools;

/**
 * Created by DevNatres on 26/02/2015.
 */
public class Number extends Agent {
    public enum ENumberType {
        INTEGER,
        DECIMAL1
    }

    private static final int PRECISION = 10;
    private static final int INCREMENT_DURATION = 45;

    private float increment;
    private float incrementingValue;
    private float value;

    private ENumberType numberType;
    private final int maxIntegerDigits;
    private int currentDigits;
    private final float maxValue;
    private final TextureRegion[] regions = new TextureRegion[PRECISION];
    private TextureRegion decimalSeparatorRegion;
    private final int decimalImageX;

    private final int digitWidth;
    private final int digitHeight;
    private int decimalSeparatorWidth;

    public Number(DnaAnimation animation, ENumberType numberType) {
        super(animation);
        this.numberType = (numberType == null) ? ENumberType.INTEGER : numberType;

        if (this.numberType == ENumberType.DECIMAL1) {
            maxIntegerDigits = PRECISION - 1;
            maxValue = Tools.integerPow(10, maxIntegerDigits) - 1f + 0.9f;
        } else {
            maxIntegerDigits = PRECISION;
            maxValue = Tools.integerPow(10, maxIntegerDigits) - 1f;
        }

        Texture numberTexture = getAnimation().getCurrentKeyFrame().getTexture();
        digitWidth = (int)(numberTexture.getWidth() / 10.5f);
        digitHeight = numberTexture.getHeight() / getAnimation().getKeyFrames().length;
        decimalSeparatorWidth = digitWidth / 2;
        decimalImageX = digitWidth * 10;

        for (int i = 0; i < PRECISION; i++) {
            regions[i] = new TextureRegion(numberTexture);
        }

        if (this.numberType == ENumberType.DECIMAL1) {
            decimalSeparatorRegion = new TextureRegion(numberTexture);
            decimalSeparatorRegion.setRegion(10 * digitWidth, 0, decimalSeparatorWidth, digitHeight);
        }
    }

    private void updateNumberGraphics() {
        if ((increment > 0 && incrementingValue+increment < value)
                || (increment < 0 && incrementingValue+increment > value)) {
            incrementingValue += increment;
        } else {
            incrementingValue = value;
        }

        getAnimation().updateStateTime();
        int numberFrameY = getAnimation().getCurrentKeyFrameIndex() * digitHeight;
        currentDigits = 0;
        int integerPart = (int)incrementingValue;

        if (numberType == ENumberType.DECIMAL1) {
            decimalSeparatorRegion.setRegion(decimalImageX, numberFrameY, decimalSeparatorWidth, digitHeight);

            float decimalPart = (incrementingValue - integerPart);
            int n = (int)(decimalPart * 10);
            if (n > 9) n = 9; // Is it necessary to assure that n has only one digit?
            updateNumberGraphics_regions(n, numberFrameY);
        }

        updateNumberGraphics_regions(integerPart, numberFrameY);
    }

    private void updateNumberGraphics_regions(int n, int numberFrameY) {
        do {
            int digit = n % 10;
            regions[currentDigits].setRegion(digit * digitWidth, numberFrameY, digitWidth, digitHeight);
            currentDigits++;
            n /= 10;
        } while (n > 0);
    }

    public void setValue(float numberValue) {
        if (numberValue > maxValue) numberValue = maxValue;

        increment = (numberValue - incrementingValue) / INCREMENT_DURATION;
        value = numberValue;
    }

    public void setValueDirectly(float numberValue) {
        if (numberValue > maxValue) numberValue = maxValue;

        increment = 0;
        incrementingValue = numberValue;
        value = numberValue;
    }

    public void sumValue(float plus) {
        setValue(value+plus);
    }

    public float getValue() {
        return value;
    }

    public int getIntValue() {
        return (int)value;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            updateNumberGraphics();

            float x = getX();
            float y = getY();

            int unitIndex = (numberType == ENumberType.DECIMAL1) ? 1: 0;

            for (int i = unitIndex; i < currentDigits; i++) {
                if ((i>unitIndex) && ((i-unitIndex)%3 == 0)) {
                    x -= digitWidth/4;
                }
                batch.draw(regions[i], x, y);
                x -= digitWidth;
            }

            if (numberType == ENumberType.DECIMAL1) {
                x = getX() + digitWidth;
                batch.draw(decimalSeparatorRegion, x, y);
                x += decimalSeparatorWidth;
                batch.draw(regions[0], x, y);
            }
        }
    }

    public int getDigitHeight() {
        return digitHeight;
    }

    public int getDigitWidth() {
        return digitWidth;
    }

    public int getDecimalSeparatorWidth() {
        return decimalSeparatorWidth;
    }
}
