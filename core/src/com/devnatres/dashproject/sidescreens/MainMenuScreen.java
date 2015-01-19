package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 30/11/2014.
 */
public class MainMenuScreen implements Screen, IExecutable {
    private static int PLAY_BUTTON_ACTION = 0;

    private final DashGame game;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;
    private final HyperStore hyperStore;

    private final InputTranslator inputTranslator;

    private final Button playButton;

    public MainMenuScreen(DashGame game) {
        this.game = game;
        mainBatch = game.getMainBatch();
        mainFont = game.getMainFont();
        mainCamera = game.getCenteredMainCamera();
        hyperStore = game.getHyperStore();

        inputTranslator = new InputTranslator();

        playButton = new Button(240, 500,
                EAnimations.BUTTON_PLAY_STANDBY.create(hyperStore),
                EAnimations.BUTTON_PLAY_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this,
                PLAY_BUTTON_ACTION);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(mainCamera);
        playButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainFont.draw(mainBatch, "Dash Project", 50, 750);
        playButton.draw(mainBatch);
//        mainFont.draw(mainBatch, "Play", 200, 500);
//        mainFont.draw(mainBatch, "Options", 200, 450);
//        mainFont.draw(mainBatch, "Credits", 200, 400);
//        mainFont.draw(mainBatch, "Exit", 200, 350);
        mainBatch.end();
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

    @Override
    public void execute(int actionId) {
        if (actionId == PLAY_BUTTON_ACTION) {
            game.setScreen(new LobbyScreen(game));
        }
    }
}
