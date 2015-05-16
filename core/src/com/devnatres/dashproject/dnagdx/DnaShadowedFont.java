package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by DevNatres on 15/05/2015.
 */
public class DnaShadowedFont {
    private final BitmapFont whiteFont;
    private final BitmapFont blackFont;

    public DnaShadowedFont() {
        whiteFont = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
        blackFont = new BitmapFont(Gdx.files.internal("fonts/black.fnt"), false);
    }

    public void draw(Batch preparedBatch, String string, int x, int y) {
        blackFont.draw(preparedBatch, string, x+2, y-2);
        whiteFont.draw(preparedBatch, string, x, y);
    }

    public int getTextWidth(String string) {
        return (int)whiteFont.getBounds(string).width;
    }
}
