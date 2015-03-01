package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Auxiliary structure for the set (tools) of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenSet {
    final DashGame dashGame;
    float screenWidth;
    float screenHeight;
    final SpriteBatch mainBatch;
    final ShapeRenderer mainShape;
    final BitmapFont mainFont;
    final DnaCamera mainCamera;
    final DnaCamera fixedCamera;
    final HyperStore hyperStore;
    final InputTranslator mainInputTranslator;
    boolean skipCameraAssistant;

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

    public void drawCentered(Texture texture) {
        mainBatch.setProjectionMatrix(fixedCamera.combined);
        mainBatch.begin();
        mainBatch.draw(texture,
                (screenWidth - texture.getWidth()) / 2,
                (screenHeight - texture.getHeight()) / 2);
        mainBatch.end();
    }

    public void drawCentered(Agent agent) {
        agent.setCenter(screenWidth / 2, screenHeight/2);
        mainBatch.setProjectionMatrix(fixedCamera.combined);
        mainBatch.begin();
        agent.draw(mainBatch);
        mainBatch.end();
    }
}
