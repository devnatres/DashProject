package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by DevNatres on 03/04/2015.
 */
public class AlphaModifier {
    private Color color;
    private float oldAlpha;

    public void modify(Batch batch, float newAlpha) {
        color = batch.getColor();
        oldAlpha = color.a;
        color.a = newAlpha;
        batch.setColor(color);
    }

    public void restore(Batch batch) {
        color.a = oldAlpha;
        batch.setColor(color);
    }
}
