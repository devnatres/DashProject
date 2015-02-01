package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.DnaCamera;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 20/01/2015.
 */
public class CreditScreen implements Screen {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final DnaCamera mainCamera;

    private final InputTranslator inputTranslator;

    private final HyperStore creditHyperStore;

    private Texture background;

    public CreditScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainCamera = dashGame.getCenteredMainCamera();

        creditHyperStore = new HyperStore();
        background = creditHyperStore.getTexture("credits.png");

        inputTranslator = new InputTranslator();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
        mainBatch.end();

        if (inputTranslator.isTouchDown()) {
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
        creditHyperStore.dispose();
    }

}
