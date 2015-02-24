package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenSet {
    DashGame dashGame;
    float screenWidth;
    float screenHeight;
    SpriteBatch mainBatch;
    ShapeRenderer mainShape;
    BitmapFont mainFont;
    DnaCamera mainCamera;
    DnaCamera fixedCamera;
    HyperStore hyperStore;
    InputTranslator mainInputTranslator;

    public LevelScreenSet(LevelScreen levelScreen, DashGame game) {
        this.dashGame = game;
        screenWidth = game.getScreenWidth();
        screenHeight = game.getScreenHeight();
        mainBatch = game.getMainBatch();
        mainShape = game.getMainShape();
        mainShape.setColor(Color.WHITE);
        mainFont = game.getMainFont();
        mainCamera = game.getMainCamera();
        mainCamera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
        hyperStore = game.getHyperStore();
        mainInputTranslator = game.getClearedMainInputTranslator();
        mainInputTranslator.clear();
        fixedCamera = new DnaCamera();
        fixedCamera.setToOrtho(false, game.getScreenWidth(), game.getScreenHeight());
    }
}
