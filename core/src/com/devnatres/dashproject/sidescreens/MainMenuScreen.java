package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Created by DevNatres on 30/11/2014.
 */
public class MainMenuScreen implements Screen, IButtonExecutable {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final BitmapFont mainFont;
    private final DnaCamera mainCamera;
    private final HyperStore hyperStore;

    private final InputTranslator inputTranslator;

    private final Button playButton;
    private final Button optionsButton;
    private final Button creditsButton;
    private final Button exitButton;

    private final Music music;

    public MainMenuScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainFont = dashGame.getMainFont();
        mainCamera = dashGame.getCenteredMainCamera();
        hyperStore = dashGame.getHyperStore();

        inputTranslator = new InputTranslator();

        playButton = new Button(240, 500,
                EAnimation.BUTTON_PLAY_STANDBY.create(hyperStore),
                EAnimation.BUTTON_PLAY_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        optionsButton = new Button(240, 300,
                EAnimation.BUTTON_OPTIONS_STANDBY.create(hyperStore),
                EAnimation.BUTTON_OPTIONS_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        creditsButton = new Button(240, 200,
                EAnimation.BUTTON_CREDITS_STANDBY.create(hyperStore),
                EAnimation.BUTTON_CREDITS_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        exitButton = new Button(240, 100,
                EAnimation.BUTTON_EXIT_STANDBY.create(hyperStore),
                EAnimation.BUTTON_EXIT_PUSHED.create(hyperStore),
                hyperStore.getSound("sounds/fail_hit.ogg"),
                10,
                this);

        music = hyperStore.getMusic("music/menu.ogg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(mainCamera);
        playButton.act(Time.FRAME, touchDownPointOnCamera);
        optionsButton.act(Time.FRAME, touchDownPointOnCamera);
        creditsButton.act(Time.FRAME, touchDownPointOnCamera);
        exitButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainFont.draw(mainBatch, "Dash Project", 50, 750);
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
            GlobalAudio.playOnly(music);
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
