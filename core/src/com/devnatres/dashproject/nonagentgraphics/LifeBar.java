package com.devnatres.dashproject.nonagentgraphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 01/05/2015.
 */
public class LifeBar {
    private static final int X_POSITION = 300;
    private static final int Y_MARGIN = 30;
    private static final int INTERNAL_MARGIN = 1;

    private final Sprite lifePointImage;
    private final Vector2 position;

    public LifeBar(HyperStore hyperStore) {
        lifePointImage = new Sprite(hyperStore.getTexture("life_point.png"));
        position = new Vector2(X_POSITION, DashGame.getInstance().getScreenHeight() - Y_MARGIN);
    }

    public void paint(Batch batch, int life) {
        float lifeWidth = lifePointImage.getWidth() + INTERNAL_MARGIN;
        for (int i = 1; i <= life; i++) {
            lifePointImage.setPosition(position.x + (i*lifeWidth), position.y);
            lifePointImage.draw(batch);
        }
    }
}
