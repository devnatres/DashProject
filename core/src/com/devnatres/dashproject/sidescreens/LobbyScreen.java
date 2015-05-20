package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
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
        TROPHY_COUNT_A,
        TROPHY_COUNT_B,
        TROPHY_COUNT_C,
        LEVEL_TROPHY_A,
        LEVEL_TROPHY_B,
        LEVEL_TROPHY_C,
        LEVEL_NAME,
        LEVEL_RECORD,
        LEVEL_LAST,
        ;
    }

    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainWhiteFont;
    private final BitmapFont mainYellowFont;
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
    private final Button upPlusButton;
    private final Button downButton;
    private final Button downPlusButton;

    private LevelId currentLevelId;
    private EExposition eExposition;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final HashMap<ETexts, String> texts = new HashMap<ETexts, String>();

    private final Texture trophy_a;
    private final Texture trophy_b;
    private final Texture trophy_c;
    private final Texture trophy_shape;
    private final Texture trophy_light;

    private final Texture new_level;

    public LobbyScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainWhiteFont = dashGame.getMainWhiteFont();
        mainYellowFont = dashGame.getMainYellowFont();
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

        upPlusButton = new Button(ARROW_BUTTON_X, 510,
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

        downPlusButton = new Button(ARROW_BUTTON_X, 210,
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

        new_level = localHyperStore.getTexture("new_level.png");
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
        upPlusButton.act(Time.FRAME, touchDownPointOnCamera);
        downButton.act(Time.FRAME, touchDownPointOnCamera);
        downPlusButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.draw(heroTexture, 50, 600);

        int headX = 240;
        mainWhiteFont.drawMultiLine(mainBatch, "Record Score", headX, 780, 0, HAlignment.CENTER);
        mainWhiteFont.drawMultiLine(mainBatch, texts.get(ETexts.TOTAL_SCORE), headX, 750, 0, HAlignment.CENTER);

        mainWhiteFont.drawMultiLine(mainBatch, "Levels", headX, 700, 0, HAlignment.CENTER);
        mainWhiteFont.drawMultiLine(mainBatch, texts.get(ETexts.PROGRESS), headX, 670, 0, HAlignment.CENTER);

        final int trophyYIcr = 3;
        int trophyWidth = trophy_shape.getWidth();
        int trophyX = 240 - trophyWidth/2;
        int trophyY = 620;
        int trophyXTab = 110;
        paintTrophy(gameState.getCurrentLevelTrophyA(), trophyX-trophyXTab, trophyY, false);
        mainWhiteFont.draw(mainBatch, texts.get(ETexts.TROPHY_COUNT_A), trophyX - trophyXTab + trophyWidth, trophyY + trophyYIcr);
        paintTrophy(gameState.getCurrentLevelTrophyB(), trophyX , trophyY, false);
        mainWhiteFont.draw(mainBatch, texts.get(ETexts.TROPHY_COUNT_B), trophyX + trophyWidth, trophyY + trophyYIcr);
        paintTrophy(gameState.getCurrentLevelTrophyC(), trophyX+trophyXTab, trophyY, false);
        mainWhiteFont.draw(mainBatch, texts.get(ETexts.TROPHY_COUNT_C), trophyX + trophyXTab + trophyWidth, trophyY + trophyYIcr);

        int titleX = 50;
        int titleY = 550;
        if (gameState.getLevelIndex() == gameState.getCompletedLevels()) {
            mainBatch.draw(new_level, titleX - new_level.getWidth(), titleY - new_level.getHeight()/2);
        }
        mainYellowFont.draw(mainBatch, texts.get(ETexts.LEVEL_NAME), titleX, titleY);

        int trophyXMargin = 20;
        int levelX = 150;
        int levelY = 500;
        drawTextRight(texts.get(ETexts.LEVEL_TROPHY_A), levelX, levelY);
        paintTrophy(gameState.getCurrentLevelTrophyA(), levelX+trophyXMargin, levelY, false);
        levelY -= 40;
        drawTextRight(texts.get(ETexts.LEVEL_TROPHY_B), levelX, levelY);
        paintTrophy(gameState.getCurrentLevelTrophyB(), levelX+trophyXMargin, levelY, false);
        levelY -= 40;
        drawTextRight(texts.get(ETexts.LEVEL_TROPHY_C), levelX, levelY);
        paintTrophy(gameState.getCurrentLevelTrophyC(), levelX+trophyXMargin, levelY, false);

        int scoreX1 = 50;
        int scoreX2 = 250;
        int scoreY = 300;
        mainWhiteFont.draw(mainBatch, "Record:", scoreX1, scoreY);
        drawTextRight(texts.get(ETexts.LEVEL_RECORD), scoreX2, scoreY);
        paintTrophy(gameState.getCurrentLevelBestTotalScore(), scoreX2+trophyXMargin, scoreY, true);

        scoreY -= 50;
        mainWhiteFont.draw(mainBatch, "Last:", scoreX1, scoreY);
        drawTextRight(texts.get(ETexts.LEVEL_LAST), scoreX2, scoreY);
        paintTrophy(gameState.getCurrentLevelLastTotalScore(), scoreX2+trophyXMargin, scoreY, true);

        goButton.draw(mainBatch);
        backButton.draw(mainBatch);
        if (eExposition != EExposition.NONE) tutorialButton.draw(mainBatch);
        upPlusButton.draw(mainBatch);
        upButton.draw(mainBatch);
        downButton.draw(mainBatch);
        downPlusButton.draw(mainBatch);
        mainBatch.end();
    }

    private void drawTextRight(String string, float x, float y) {
        mainWhiteFont.drawMultiLine(mainBatch, string, x, y, 0, HAlignment.RIGHT);
    }

    private void paintTrophy(int score, int refX, int refY, boolean light) {
        final int trophyYIcr = 9;
        final Texture trophyTexture = getTrophyTexture(score);
        if (light && trophyTexture != trophy_shape) {
            mainBatch.draw(trophy_light, refX - trophy_light.getWidth()/4,
                    refY - trophyTexture.getHeight() + trophyYIcr - trophy_light.getHeight()/4);
        }
        mainBatch.draw(trophyTexture, refX, refY - trophyTexture.getHeight() + trophyYIcr);
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
        } else if (button == upPlusButton) {
            gameState.displaceCurrentLevel(2);
            updateCurrentLevel();
        } else if (button == upButton) {
            gameState.displaceCurrentLevel(1);
            updateCurrentLevel();
        } else if (button == downButton) {
            gameState.displaceCurrentLevel(-1);
            updateCurrentLevel();
        } else if (button == downPlusButton) {
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

        texts.clear();
        texts.put(ETexts.TOTAL_SCORE, format(gameState.getTotalBestScore()));
        texts.put(ETexts.PROGRESS, gameState.getCompletedLevels() + "/" + gameState.getMaxLevels());

        texts.put(ETexts.TROPHY_COUNT_A, " x " + format(gameState.getTrophyACount()));
        texts.put(ETexts.TROPHY_COUNT_B, " x " + format(gameState.getTrophyBCount()));
        texts.put(ETexts.TROPHY_COUNT_C, " x " + format(gameState.getTrophyCCount()));

        texts.put(ETexts.LEVEL_NAME, "" + currentLevelId.getLevelName());

        texts.put(ETexts.LEVEL_TROPHY_A, format(gameState.getCurrentLevelTrophyA()));
        texts.put(ETexts.LEVEL_TROPHY_B, format(gameState.getCurrentLevelTrophyB()));
        texts.put(ETexts.LEVEL_TROPHY_C, format(gameState.getCurrentLevelTrophyC()));

        texts.put(ETexts.LEVEL_RECORD, format(gameState.getCurrentLevelBestTotalScore()));

        texts.put(ETexts.LEVEL_LAST, format(gameState.getCurrentLevelLastTotalScore()));
    }

    private String format(int score) {
        return String.format("%,d", score);
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
