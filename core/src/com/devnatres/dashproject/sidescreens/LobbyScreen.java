package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.GameState;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.levelsystem.LevelCreator;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 14/01/2015.
 */
public class LobbyScreen implements Screen, IButtonExecutable {
    private static int ARROW_BUTTON_X = 420;

    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;

    private final Texture heroTexture;
    private final Texture background;
    private final HyperStore lobbyHyperStore;

    private final InputTranslator inputTranslator;
    private final GameState gameState;

    private final HyperStore hyperStore;
    private final Button goButton;
    private final Button backButton;
    private final Button upButton;
    private final Button up2Button;
    private final Button downButton;
    private final Button down2Button;

    private LevelId currentLevelId;

    public LobbyScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainFont();
        mainCamera = dashGame.getCenteredMainCamera();
        hyperStore = dashGame.getHyperStore();

        lobbyHyperStore = new HyperStore();
        heroTexture = lobbyHyperStore.getTexture("mark.png");
        background = lobbyHyperStore.getTexture("lobby_background.png");

        gameState = dashGame.getGameState();

        inputTranslator = new InputTranslator();

        goButton = new Button(380, 64,
                EAnimations.BUTTON_GO_STANDBY.create(hyperStore),
                EAnimations.BUTTON_GO_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        backButton = new Button(100, 64,
                EAnimations.BUTTON_BACK_STANDBY.create(hyperStore),
                EAnimations.BUTTON_BACK_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        up2Button = new Button(ARROW_BUTTON_X, 510,
                EAnimations.BUTTON_ARROW_UP2.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        upButton = new Button(ARROW_BUTTON_X, 430,
                EAnimations.BUTTON_ARROW_UP.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        downButton = new Button(ARROW_BUTTON_X, 300,
                EAnimations.BUTTON_ARROW_DOWN.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        down2Button = new Button(ARROW_BUTTON_X, 210,
                EAnimations.BUTTON_ARROW_DOWN2.create(hyperStore),
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

        currentLevelId = gameState.getCurrentLevelId();

        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(mainCamera);
        goButton.act(Time.FRAME, touchDownPointOnCamera);
        backButton.act(Time.FRAME, touchDownPointOnCamera);
        upButton.act(Time.FRAME, touchDownPointOnCamera);
        up2Button.act(Time.FRAME, touchDownPointOnCamera);
        downButton.act(Time.FRAME, touchDownPointOnCamera);
        down2Button.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.draw(heroTexture, 50, 600);
        mainFont.draw(mainBatch, "Total score: ", 200, 700);
        mainFont.draw(mainBatch, "Progress: x.y%", 200, 650);
        mainFont.draw(mainBatch, "Select level: " + currentLevelId.getLevelName(), 200, 300);
        mainFont.draw(mainBatch, "Record: " + gameState.getCurrentLevelScore(), 200, 250);
        goButton.draw(mainBatch);
        backButton.draw(mainBatch);
        up2Button.draw(mainBatch);
        upButton.draw(mainBatch);
        downButton.draw(mainBatch);
        down2Button.draw(mainBatch);
        mainBatch.end();

        /*if (inputTranslator.isTouchDown()) {
            dashGame.setScreen(LevelCreator.createLevel(dashGame, currentLevelId));
            dispose();
        }*/
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

    @Override
    public void execute(Button button) {
        if (button == goButton) {
            dashGame.setScreen(LevelCreator.createLevel(dashGame, currentLevelId));
            dispose();
        } else if (button == backButton) {
            dashGame.setScreen(new MainMenuScreen(dashGame));
        } else if (button == up2Button) {
            gameState.displaceCurrentLevel(2);
        } else if (button == upButton) {
            gameState.displaceCurrentLevel(1);
        } else if (button == downButton) {
            gameState.displaceCurrentLevel(-1);
        } else if (button == down2Button) {
            gameState.displaceCurrentLevel(-2);
        }

    }
}
