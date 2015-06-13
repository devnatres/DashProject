package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimButton;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents a game screen for the option menu. <br>
 *     <br>
 * Created by DevNatres on 20/01/2015.
 */
public class OptionScreen implements Screen, IButtonExecutable {
    private static final int DONE_DURATION = 60;

    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final DnaCamera mainCamera;
    private final HyperStore localHyperStore;
    private final GameState gameState;

    private final InputTranslator mainInputTranslator;

    private final Button soundButton;
    private final Button soundSymbolButton;
    private final Button tutorialsButton;
    private final Button backButton;

    private final Agent offSymbol;

    private final Texture tutorialsResettingDone;
    private int doneDuration;

    public OptionScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainCamera = dashGame.getCenteredMainCamera();
        gameState = dashGame.getGameState();

        localHyperStore = new HyperStore();

        mainInputTranslator = dashGame.getClearedMainInputTranslator();

        offSymbol = new Agent(localHyperStore.getTexture("symbols/symbol_off.png"));

        soundButton = new Button(240, 700,
                EAnimButton.BUTTON_OPT_SOUND.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);
        soundButton.setAutomaticSoundOff();
        soundSymbolButton = new Button(240, soundButton.getY()-offSymbol.getHeight()/2,
                EAnimButton.BUTTON_SYMBOL_SOUND.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);
        soundSymbolButton.setAutomaticSoundOff();

        tutorialsButton = new Button(240, 400,
                EAnimButton.BUTTON_OPT_TUTORIAL.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        backButton = new Button(240, 100,
                EAnimButton.BUTTON_OPT_BACK.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        tutorialsResettingDone = localHyperStore.getTexture("message_done.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = mainInputTranslator.getTouchDownPointOnCamera(mainCamera);
        soundButton.act(Time.FRAME, touchDownPointOnCamera);
        soundSymbolButton.act(Time.FRAME, touchDownPointOnCamera);
        tutorialsButton.act(Time.FRAME, touchDownPointOnCamera);
        backButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        soundButton.draw(mainBatch);
        soundSymbolButton.draw(mainBatch);
        if (!gameState.isSoundActivated()) {
            offSymbol.setCenter(soundSymbolButton.getCenter());
            offSymbol.draw(mainBatch);
        }

        tutorialsButton.draw(mainBatch);
        if (doneDuration > 0) {
            doneDuration--;
            mainBatch.draw(tutorialsResettingDone,
                    (dashGame.getScreenWidth()-tutorialsResettingDone.getWidth())/2,
                    tutorialsButton.getY()-tutorialsButton.getHeight());
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
        localHyperStore.dispose();
    }

    @Override
    public void execute(Button button) {
        if (button == backButton) {
            dashGame.setScreen(new MainMenuScreen(dashGame));
        } else if (button == soundButton || button == soundSymbolButton) {
            gameState.activateSound(!gameState.isSoundActivated());
            soundButton.playSound();
        } else if (button == tutorialsButton) {
            gameState.setAllTutorialsUnvisited();
            doneDuration = DONE_DURATION;
        }
    }
}
