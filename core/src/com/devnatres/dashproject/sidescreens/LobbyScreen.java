package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.levelsystem.LevelCreator;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 14/01/2015.
 */
public class LobbyScreen implements Screen {
    final DashGame game;
    final SpriteBatch mainBatch;
    final BitmapFont mainFont;
    final OrthographicCamera mainCamera;

    final Texture heroTexture;
    final Texture background;
    final HyperStore lobbyHyperStore;

    int currentLevelNumber;

    final InputTranslator inputTranslator;

    public LobbyScreen(DashGame game) {
        this.game = game;
        mainBatch = game.getMainBatch();
        mainFont = game.getMainFont();
        mainCamera = game.getCenteredMainCamera();

        lobbyHyperStore = new HyperStore();
        heroTexture = lobbyHyperStore.getTexture("mark.png");
        background = lobbyHyperStore.getTexture("lobby_background.png");

        currentLevelNumber = 1;

        inputTranslator = new InputTranslator();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainCamera.update(); // It isn't necessary if we don't change properties like position but it's a good practice
        mainBatch.setProjectionMatrix(mainCamera.combined); // Use the coordinate system specified by the camera

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.draw(heroTexture, 50, 600);
        mainFont.draw(mainBatch, "Total score: ", 200, 700);
        mainFont.draw(mainBatch, "Progress: x.y%", 200, 650);
        mainFont.draw(mainBatch, "Select level: " + currentLevelNumber, 200, 300);
        mainBatch.end();

        if (inputTranslator.isTouchDown()) {
            final String levelString = "level" + String.format("%04d", currentLevelNumber);
            game.setScreen(LevelCreator.createLevel(game, levelString));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        lobbyHyperStore.dispose();
    }
}
