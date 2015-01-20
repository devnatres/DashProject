package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.GameState;
import com.devnatres.dashproject.agents.Agent;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 20/01/2015.
 */
public class OptionScreen implements Screen, IButtonExecutable {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;
    private final HyperStore hyperStore;
    private final GameState gameState;

    private final InputTranslator inputTranslator;

    private final Button soundButton;
    private final Button cameraButton;
    private final Button backButton;

    private Agent soundSymbol;
    private Agent cameraSymbol;
    private Agent offSymbol;

    private boolean cameraActivated = true;

    public OptionScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainFont();
        mainCamera = dashGame.getCenteredMainCamera();
        hyperStore = dashGame.getHyperStore();
        gameState = dashGame.getGameState();

        inputTranslator = new InputTranslator();

        soundSymbol = new Agent(hyperStore.getTexture("symbols/symbol_sound.png"));
        soundSymbol.setCenter(240, 630);
        cameraSymbol = new Agent(hyperStore.getTexture("symbols/symbol_camera.png"));
        cameraSymbol.setCenter(240, 430);
        offSymbol = new Agent(hyperStore.getTexture("symbols/symbol_off.png"));


        soundButton = new Button(240, 700,
                EAnimations.BUTTON_OPT_SOUND.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        cameraButton = new Button(240, 500,
                EAnimations.BUTTON_OPT_CAMERA.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        backButton = new Button(240, 100,
                EAnimations.BUTTON_OPT_BACK.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(mainCamera);
        soundButton.act(Time.FRAME, touchDownPointOnCamera);
        cameraButton.act(Time.FRAME, touchDownPointOnCamera);
        backButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainFont.draw(mainBatch, "Options", 50, 750);
        soundButton.draw(mainBatch);
        soundSymbol.draw(mainBatch);
        if (!gameState.isSoundActivated()) {
            offSymbol.setCenter(soundSymbol.getAuxCenter());
            offSymbol.draw(mainBatch);
        }
        cameraButton.draw(mainBatch);
        cameraSymbol.draw(mainBatch);
        if (!cameraActivated) {
            offSymbol.setCenter(cameraSymbol.getAuxCenter());
            offSymbol.draw(mainBatch);
        }
        backButton.draw(mainBatch);
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
    public void execute(Button button) {
        if (button == backButton) {
            dashGame.setScreen(new MainMenuScreen(dashGame));
        } else if (button == soundButton) {
            gameState.activateSound(!gameState.isSoundActivated());
        } else if (button == cameraButton) {
            cameraActivated = !cameraActivated;
        }
    }
}
