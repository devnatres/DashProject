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

import java.util.HashMap;

/**
 * Represents a game screen for the "lobby room",
 * where the player selects the level to play and can see other information. <br>
 *     <br>
 * Created by DevNatres on 14/01/2015.
 */
public class LobbyScreen implements Screen, IButtonExecutable {
    private static final int ARROW_BUTTON_X = 420;

    private enum ETexts {
        TOTAL_SCORE,
        PROGRESS,
        TROPHY_TOTAL_COUNT,
        LEVEL_TROPHIES,
        LEVEL_NAME,
        LEVEL_RECORD,
        LEVEL_LAST,
        ;
    }

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
    private final HashMap<ETexts, String> textMap = new HashMap<ETexts, String>();

    private final Texture trophy_a;
    private final Texture trophy_b;
    private final Texture trophy_c;
    private final Texture trophy_shape;
    private final Texture trophy_light;

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

        trophy_a = localHyperStore.getTexture("trophies/trophy_a.png");
        trophy_b = localHyperStore.getTexture("trophies/trophy_b.png");
        trophy_c = localHyperStore.getTexture("trophies/trophy_c.png");
        trophy_shape = localHyperStore.getTexture("trophies/trophy_shape.png");
        trophy_light = localHyperStore.getTexture("trophies/trophy_light.png");
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

        int headColumn1 = 120;
        mainFont.draw(mainBatch, textMap.get(ETexts.TOTAL_SCORE), headColumn1, 750);
        mainFont.draw(mainBatch, textMap.get(ETexts.PROGRESS), headColumn1, 700);
        mainFont.draw(mainBatch, textMap.get(ETexts.TROPHY_TOTAL_COUNT), headColumn1, 650);

        int bodyColumn1 = 50;
        int bodyColumn2 = 190;
        int bodyColumn3 = 270;

        mainFont.draw(mainBatch, textMap.get(ETexts.LEVEL_NAME), bodyColumn1, 550);
        mainFont.draw(mainBatch, textMap.get(ETexts.LEVEL_TROPHIES), bodyColumn1, 500);

        int levelDataY = 400;
        mainFont.draw(mainBatch, "Record:", bodyColumn1, levelDataY);
        mainFont.draw(mainBatch, textMap.get(ETexts.LEVEL_RECORD), bodyColumn2, levelDataY);
        paintTrophy(gameState.getCurrentLevelBestTotalScore(), bodyColumn3, levelDataY);

        levelDataY -= 50;
        mainFont.draw(mainBatch, "Last:", bodyColumn1, levelDataY);
        mainFont.draw(mainBatch, textMap.get(ETexts.LEVEL_LAST), bodyColumn2, levelDataY);
        paintTrophy(gameState.getCurrentLevelLastTotalScore(), bodyColumn3, levelDataY);

        goButton.draw(mainBatch);
        backButton.draw(mainBatch);
        if (eExposition != EExposition.NONE) tutorialButton.draw(mainBatch);
        up2Button.draw(mainBatch);
        upButton.draw(mainBatch);
        downButton.draw(mainBatch);
        down2Button.draw(mainBatch);
        mainBatch.end();
    }

    private void paintTrophy(int score, int refX, int refY) {
        final int trophyIcr = 9;
        final Texture trophyTexture = getTrophyTexture(score);
        if (trophyTexture != trophy_shape) {
            mainBatch.draw(trophy_light, refX - trophy_light.getWidth()/4,
                    refY - trophyTexture.getHeight() + trophyIcr - trophy_light.getHeight()/4);
        }
        mainBatch.draw(trophyTexture, refX, refY - trophyTexture.getHeight() + trophyIcr);
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

        textMap.clear();
        textMap.put(ETexts.TOTAL_SCORE, "Total score: " + String.format("%,d",gameState.getTotalBestScore()));
        textMap.put(ETexts.PROGRESS, "Progress: " + gameState.getCompletedLevels() + "/" + gameState.getMaxLevels());

        textMap.put(ETexts.TROPHY_TOTAL_COUNT,
                "  " + String.format("%,d",gameState.getTrophyACount())
                + "  " + String.format("%,d", gameState.getTrophyBCount())
                + "  " + String.format("%,d", gameState.getTrophyCCount()));

        String newLevel = (gameState.getLevelIndex() == gameState.getCompletedLevels()) ? " (NEW!)" : "";
        textMap.put(ETexts.LEVEL_NAME, "" + currentLevelId.getLevelName() + newLevel);

        textMap.put(ETexts.LEVEL_TROPHIES,
                "  " + String.format("%,d",gameState.getCurrentLevelTrophyA())
                        + "  " + String.format("%,d", gameState.getCurrentLevelTrophyB())
                        + "  " + String.format("%,d", gameState.getCurrentLevelTrophyC()));

        textMap.put(ETexts.LEVEL_RECORD, String.format("%,d", gameState.getCurrentLevelBestTotalScore()));

        textMap.put(ETexts.LEVEL_LAST, String.format("%,d", gameState.getCurrentLevelLastTotalScore()));
    }

    Texture getTrophyTexture(int score) {
        if (score >= gameState.getCurrentLevelTrophyA()) {
            return trophy_a;
        } else if (score >= gameState.getCurrentLevelTrophyB()) {
            return trophy_b;
        } else if (score >= gameState.getCurrentLevelTrophyC()) {
            return trophy_c;
        } else {
            return trophy_shape;
        }
    }
}
