package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.levelsystem.LevelCreator;

/**
 * Created by DevNatres on 30/11/2014.
 */
public class MainMenuScreen implements Screen {
    final DashGame game;
    final SpriteBatch mainBatch;
    final BitmapFont mainFont;
    final OrthographicCamera mainCamera;

    public MainMenuScreen(DashGame game) {
        this.game = game;
        mainBatch = game.getMainBatch();
        mainFont = game.getMainFont();
        mainCamera = game.getCenteredMainCamera();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainCamera.update(); // It isn't necessary if we don't change properties like position but it's a good practice
        mainBatch.setProjectionMatrix(mainCamera.combined); // Use the coordinate system specified by the camera

        mainBatch.begin();
        mainFont.draw(mainBatch, "Dash Project", 100, 150);
        mainFont.draw(mainBatch, "Tap anywhere to begin!", 100, 100);
        mainBatch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(LevelCreator.createLevel(game, "level0001"));
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
    }
}
