package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.animations.EAnimButton;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.exposition.EExposition;
import com.devnatres.dashproject.exposition.ExpositionScreen;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.levelsystem.LevelCreator;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents a game screen for the "lobby room",
 * where the player selects the level to play and can see other information. <br>
 *     <br>
 * Created by DevNatres on 14/01/2015.
 */
public class LobbyScreen implements Screen, IButtonExecutable {
    private static final int ARROW_BUTTON_X = 420;

    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;

    private final Texture heroTexture;
    private final Texture background;
    private final HyperStore localHyperStore;

    private final InputTranslator mainInputTranslator;
    private final GameState gameState;

    private final Button goButton;
    private final Button backButton;
    private final Button tutorialButton;
    private final Button upButton;
    private final Button up2Button;
    private final Button downButton;
    private final Button down2Button;

    private LevelId currentLevelId;
    private EExposition eExposition;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();

    public LobbyScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainWhiteFont();
        mainCamera = dashGame.getCenteredMainCamera();

        localHyperStore = new HyperStore();
        heroTexture = localHyperStore.getTexture("mark.png");
        background = localHyperStore.getTexture("lobby_background.png");

        gameState = dashGame.getGameState();
        updateCurrentLevel();

        mainInputTranslator = dashGame.getClearedMainInputTranslator();

        goButton = new Button(380, 64,
                EAnimButton.BUTTON_GO_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_GO_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        backButton = new Button(100, 64,
                EAnimButton.BUTTON_BACK_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_BACK_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        tutorialButton = new Button(240, 64,
                EAnimButton.BUTTON_TUTORIAL_STANDBY.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        up2Button = new Button(ARROW_BUTTON_X, 510,
                EAnimButton.BUTTON_ARROW_UP2.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        upButton = new Button(ARROW_BUTTON_X, 430,
                EAnimButton.BUTTON_ARROW_UP.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        downButton = new Button(ARROW_BUTTON_X, 300,
                EAnimButton.BUTTON_ARROW_DOWN.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        down2Button = new Button(ARROW_BUTTON_X, 210,
                EAnimButton.BUTTON_ARROW_DOWN2.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = mainInputTranslator.getTouchDownPointOnCamera(mainCamera);
        goButton.act(Time.FRAME, touchDownPointOnCamera);
        backButton.act(Time.FRAME, touchDownPointOnCamera);
        if (eExposition != EExposition.NONE) tutorialButton.act(Time.FRAME, touchDownPointOnCamera);
        upButton.act(Time.FRAME, touchDownPointOnCamera);
        up2Button.act(Time.FRAME, touchDownPointOnCamera);
        downButton.act(Time.FRAME, touchDownPointOnCamera);
        down2Button.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.draw(heroTexture, 50, 600);

        mainFont.draw(mainBatch, "Total score: " + gameState.getTotalBestScore(), 200, 750);
        mainFont.draw(mainBatch, "Progress: " + gameState.getCompletedLevels() + "/" + gameState.getMaxLevels(), 200, 700);
        mainFont.draw(mainBatch, "A: " + gameState.getTrophyACount()
                + "  B: " + gameState.getTrophyBCount()
                + "  C: " + gameState.getTrophyCCount()
                , 200, 650);

        int levelDataX1 = 20;
        int levelDataX2 = 170;
        int levelDataX3 = 270;
        String newLevel = "";
        if (gameState.getLevelIndex() == gameState.getCompletedLevels()) {
            newLevel = " (NEW!)";
        }
        mainFont.draw(mainBatch, "Select level: " + currentLevelId.getLevelName() + newLevel, levelDataX1, 565);
        mainFont.draw(mainBatch, "A: " + gameState.getCurrentLevelTrophyA()
                        + "  B: " + gameState.getCurrentLevelTrophyB()
                        + "  C: " + gameState.getCurrentLevelTrophyC()
                , levelDataX1, 530);

        int levelDataY = 490;
        mainFont.draw(mainBatch, "Last", levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "Records", levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Action: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastActionScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestActionScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Time: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastTimeScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestTimeScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Life: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastLifeScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestLifeScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Max.Chain: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastChainScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestChainScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Full Chain: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastFullChainScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestFullChainScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "TOTAL: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastTotalScore(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestTotalScore(), levelDataX3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Trophy: ", levelDataX1, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelLastTrophy(), levelDataX2, levelDataY);
        mainFont.draw(mainBatch, "" + gameState.getCurrentLevelBestTrophy(), levelDataX3, levelDataY);

        goButton.draw(mainBatch);
        backButton.draw(mainBatch);
        if (eExposition != EExposition.NONE) tutorialButton.draw(mainBatch);
        up2Button.draw(mainBatch);
        upButton.draw(mainBatch);
        downButton.draw(mainBatch);
        down2Button.draw(mainBatch);
        mainBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        globalAudio.stopMusic();
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
        if (button == goButton) {
            if (eExposition == EExposition.NONE || gameState.isTutorialVisited(currentLevelId)) {
                dashGame.setScreen(LevelCreator.createLevel(dashGame, currentLevelId));
            } else {
                dashGame.setScreen(new ExpositionScreen(dashGame, eExposition, currentLevelId));
                gameState.setTutorialVisited(currentLevelId);
            }
        } else if (button == backButton) {
            dashGame.setScreen(new MainMenuScreen(dashGame));
        } else if (button == up2Button) {
            gameState.displaceCurrentLevel(2);
            updateCurrentLevel();
        } else if (button == upButton) {
            gameState.displaceCurrentLevel(1);
            updateCurrentLevel();
        } else if (button == downButton) {
            gameState.displaceCurrentLevel(-1);
            updateCurrentLevel();
        } else if (button == down2Button) {
            gameState.displaceCurrentLevel(-2);
            updateCurrentLevel();
        } else if (button == tutorialButton) {
            dashGame.setScreen(new ExpositionScreen(dashGame, eExposition, null));
            gameState.setTutorialVisited(currentLevelId);
        }
    }

    private void updateCurrentLevel() {
        currentLevelId = gameState.getCurrentLevelId();
        eExposition = currentLevelId.getETutorial();
    }
}
