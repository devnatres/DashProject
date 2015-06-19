package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.tools.Tools;

/**
 * Created by DevNatres on 19/06/2015.
 */
public class SplashScreen implements Screen {
    private static final int FADE_DURATION = (int)Time.FPS;
    private static final int SPLASH_DURATION = (int)Time.FPS * 3 + FADE_DURATION * 2;
    private static final float FADE_ICR = 1f/FADE_DURATION;

    private final DashGame dashGame;
    private final HyperStore localHyperStore;
    private final SpriteBatch mainBatch;
    private final DnaCamera mainCamera;
    private final InputTranslator mainInputTranslator;
    private final Texture background;

    private int splashDuration = SPLASH_DURATION;
    private float fadeValue;

    public SplashScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        localHyperStore = new HyperStore();
        mainBatch = dashGame.getMainBatch();
        mainCamera = dashGame.getCenteredMainCamera();
        mainInputTranslator = dashGame.getClearedMainInputTranslator();
        background = localHyperStore.getTexture("splash.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        mainBatch.begin();

        if (splashDuration > (SPLASH_DURATION - FADE_DURATION)) {
            mainBatch.setColor(1f, 1f, 1f, fadeValue);
            fadeValue = Tools.limitFloat(fadeValue + FADE_ICR, 0f, 1f);
        } else if (splashDuration < FADE_DURATION) {
            mainBatch.setColor(1f, 1f, 1f, fadeValue);
            fadeValue = Tools.limitFloat(fadeValue - FADE_ICR, 0f, 1f);
        } else {
            mainBatch.setColor(1f, 1f, 1f, 1f);
        }

        if (fadeValue > FADE_ICR) {
            mainBatch.draw(background, 0, 0);
        }

        mainBatch.end();

        if (mainInputTranslator.isTouchDown()) {
            splashDuration = 0;
        } else if (splashDuration > 0) {
            splashDuration--;
        }

        if (splashDuration == 0) {
            mainBatch.setColor(1f, 1f, 1f, 1f);
            dashGame.setScreen(new MainMenuScreen(dashGame));
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
        localHyperStore.dispose();
    }
}
