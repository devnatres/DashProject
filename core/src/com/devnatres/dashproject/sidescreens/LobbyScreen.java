package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.GameState;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.levelsystem.LevelCreator;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 14/01/2015.
 */
public class LobbyScreen implements Screen {
    private final DashGame game;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final OrthographicCamera mainCamera;

    private final Texture heroTexture;
    private final Texture background;
    private final HyperStore lobbyHyperStore;

    private final InputTranslator inputTranslator;
    private final GameState gameState;

    public LobbyScreen(DashGame dashGame) {
        this.game = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainFont();
        mainCamera = dashGame.getCenteredMainCamera();

        lobbyHyperStore = new HyperStore();
        heroTexture = lobbyHyperStore.getTexture("mark.png");
        background = lobbyHyperStore.getTexture("lobby_background.png");

        gameState = dashGame.getGameState();

        inputTranslator = new InputTranslator();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        LevelId levelId = gameState.getCurrentLevelId();

        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.draw(heroTexture, 50, 600);
        mainFont.draw(mainBatch, "Total score: ", 200, 700);
        mainFont.draw(mainBatch, "Progress: x.y%", 200, 650);
        mainFont.draw(mainBatch, "Select level: " + levelId.getLevelName(), 200, 300);
        mainBatch.end();

        if (inputTranslator.isTouchDown()) {
            game.setScreen(LevelCreator.createLevel(game, levelId));
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
