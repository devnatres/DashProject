package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.animations.EAnimButton;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents a game screen for the main menu. <br>
 *     <br>
 * Created by DevNatres on 30/11/2014.
 */
public class MainMenuScreen implements Screen, IButtonExecutable {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;
    private final HyperStore mainHyperStore;
    private final HyperStore localHyperStore;

    private final InputTranslator mainInputTranslator;

    private final Texture background;
    private final Button playButton;
    private final Button optionsButton;
    private final Button creditsButton;
    private final Button exitButton;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Music music;

    public MainMenuScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainWhiteFont();
        mainCamera = dashGame.getCenteredMainCamera();
        mainInputTranslator = dashGame.getClearedMainInputTranslator();
        mainHyperStore = dashGame.getHyperStore();
        localHyperStore = new HyperStore();

        background = localHyperStore.getTexture("menu_background.png");

        playButton = new Button(240, 450,
                EAnimButton.BUTTON_PLAY_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_PLAY_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        optionsButton = new Button(240, 300,
                EAnimButton.BUTTON_OPTIONS_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_OPTIONS_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        creditsButton = new Button(240, 200,
                EAnimButton.BUTTON_CREDITS_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_CREDITS_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        exitButton = new Button(240, 100,
                EAnimButton.BUTTON_EXIT_STANDBY.create(localHyperStore),
                EAnimButton.BUTTON_EXIT_PUSHED.create(localHyperStore),
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        music = mainHyperStore.getMusic("music/surfing_menus.ogg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = mainInputTranslator.getTouchDownPointOnCamera(mainCamera);
        playButton.act(Time.FRAME, touchDownPointOnCamera);
        optionsButton.act(Time.FRAME, touchDownPointOnCamera);
        creditsButton.act(Time.FRAME, touchDownPointOnCamera);
        exitButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        playButton.draw(mainBatch);
        optionsButton.draw(mainBatch);
        creditsButton.draw(mainBatch);
        exitButton.draw(mainBatch);
        mainBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if (!music.isPlaying()) {
            music.setLooping(true);
            globalAudio.playOnly(music);
        }
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
        if (button == playButton) {
            dashGame.setScreen(new LobbyScreen(dashGame));
        } else if (button == optionsButton) {
            dashGame.setScreen(new OptionScreen(dashGame));
        } else if (button == creditsButton) {
            dashGame.setScreen(new CreditScreen(dashGame));
        } else if (button == exitButton) {
            Gdx.app.exit();
        }
    }
}
