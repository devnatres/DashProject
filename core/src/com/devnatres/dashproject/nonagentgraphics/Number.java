package com.devnatres.dashproject.nonagentgraphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.tools.Tools;

/**
 * Represents a number that draws itself with a bitmap atlas. <br>
 *     <br>
 * The distribution of the numbers in each frame is: "0123456789."
 * Where each number has the same width as the others, and the decimal separator has half-width.
 * It can exist one row per frame if the numbers are animated.
 *     <br>
 * Created by DevNatres on 26/02/2015.
 */
public class Number {
    public enum ENumberType {
        INTEGER,
        DECIMAL1
    }

    private static final int PRECISION = 9;
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

    private final DnaAnimation animation;
    private final Vector2 unitPosition;

    private float numberScale = 1f;
    private float lastNumberScale = numberScale;

    public Number(DnaAnimation animation, ENumberType numberType, float numberScale) {
        this(animation, numberType);
        this.numberScale = numberScale;
        this.lastNumberScale = numberScale;
    }

    public Number(DnaAnimation animation, ENumberType numberType) {
        this.animation = animation;
        this.numberType = (numberType == null) ? ENumberType.INTEGER : numberType;

        unitPosition = new Vector2();

        Texture numberTexture = animation.getCurrentKeyFrame().getTexture();
        digitWidth = (int)(numberTexture.getWidth() / 10.5f);
        digitHeight = numberTexture.getHeight() / animation.getKeyFrames().length;
        decimalSeparatorWidth = digitWidth / 2;
        decimalImageX = digitWidth * 10;

        if (this.numberType == ENumberType.DECIMAL1) {
            maxIntegerDigits = PRECISION - 1;
            maxValue = Tools.integerPow(10, maxIntegerDigits) - 1f + 0.9f;
            decimalSeparatorRegion = new TextureRegion(numberTexture);
            decimalSeparatorRegion.setRegion(10 * digitWidth, 0, decimalSeparatorWidth, digitHeight);
        } else {
            maxIntegerDigits = PRECISION;
            maxValue = Tools.integerPow(10, maxIntegerDigits) - 1f;
        }

        for (int i = 0; i < PRECISION; i++) {
            regions[i] = new TextureRegion(numberTexture);
        }
    }

    private void updateNumberGraphics() {
        if ((increment > 0 && incrementingValue+increment < value)
                || (increment < 0 && incrementingValue+increment > value)) {
            incrementingValue += increment;
        } else {
            incrementingValue = value;
        }

        animation.updateStateTime();
        int numberFrameY = animation.getCurrentKeyFrameIndex() * digitHeight;
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

    public float getNumberScale() {
        return numberScale;
    }

    public float getUnitX() {
        return unitPosition.x;
    }

    public float getUnitY() {
        return unitPosition.y;
    }

    public void setUnitPosition(float x, float y) {
        unitPosition.set(x, y);
    }

    public void changeNumberScale(float scale) {
        lastNumberScale = numberScale;
        numberScale = scale;
        unitPosition.x += (digitWidth * (1-numberScale));
        unitPosition.y += (digitWidth * (1-numberScale));
    }

    public void restoreNumberScale() {
        unitPosition.x -= (digitWidth * (1-numberScale));
        unitPosition.y -= (digitWidth * (1-numberScale));
        numberScale = lastNumberScale;
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

    public void render(Batch batch) {
        updateNumberGraphics();

        float x = unitPosition.x;
        float y = unitPosition.y;

        float scaledDigitWidth = digitWidth*numberScale;
        float scaledDigitHeight = digitHeight*numberScale;
        float scaledDecimalSeparatorWidth = decimalSeparatorWidth*numberScale;

        int unitIndex = (numberType == ENumberType.DECIMAL1) ? 1: 0;

        for (int i = unitIndex; i < currentDigits; i++) {
            if ((i>unitIndex) && ((i-unitIndex)%3 == 0)) {
                x -= scaledDigitWidth/4;
            }
            batch.draw(regions[i], x, y, scaledDigitWidth, scaledDigitHeight);
            x -= scaledDigitWidth;
        }

        if (numberType == ENumberType.DECIMAL1) {
            x = unitPosition.x + scaledDigitWidth;
            batch.draw(decimalSeparatorRegion, x, y, scaledDecimalSeparatorWidth, scaledDigitHeight);
            x += scaledDecimalSeparatorWidth;
            batch.draw(regions[0], x, y, scaledDigitWidth, scaledDigitHeight);
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
