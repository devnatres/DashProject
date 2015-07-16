package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.DashGame;

/**
 * Created by DevNatres on 15/05/2015.
 */
public class DnaShadowedFont implements Disposable {
    private static int DISPLACEMENT = 3;

    private final BitmapFont foreFont;
    private final BitmapFont backFont;
    private final int centerX;

    public DnaShadowedFont() {
        this(false);
    }

    public DnaShadowedFont(boolean yellow) {
        String foreFontResource = yellow ? "fonts/yellow.fnt" : "fonts/white.fnt";

        foreFont = new BitmapFont(Gdx.files.internal(foreFontResource), false);
        backFont = new BitmapFont(Gdx.files.internal("fonts/black.fnt"), false);

        centerX = DashGame.SCREEN_WIDTH / 2;
    }

    public void draw(Batch preparedBatch, String string, int x, int y) {
        final float displacement = DISPLACEMENT * foreFont.getScaleX();

        backFont.draw(preparedBatch, string, x + displacement, y - displacement);
        foreFont.draw(preparedBatch, string, x, y);
    }

    public void drawCenteredInX(Batch preparedBatch, String string, int yCenter) {
        final int xOrigin = centerX - getTextWidth(string)/2;
        final int yOrigin = yCenter + (int)(foreFont.getLineHeight()/2f);
        final float displacement = DISPLACEMENT * foreFont.getScaleX();

        backFont.draw(preparedBatch, string, xOrigin + displacement, yOrigin - displacement);
        foreFont.draw(preparedBatch, string, xOrigin, yOrigin);
    }

    public void setScale(float scaleXY) {
        foreFont.setScale(scaleXY);
        backFont.setScale(scaleXY);
    }

    public int getTextWidth(String string) {
        return (int) foreFont.getBounds(string).width;
    }

    @Override
    public void dispose() {
        foreFont.dispose();
        backFont.dispose();
    }
}
