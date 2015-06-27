package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.levelsystem.LevelCreator;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.sidescreens.LobbyScreen;

/**
 * Represents a screen to render an Exposition object. <br>
 * When the exposition is finished,
 * if a levelId has been specified then the corresponding level will be created. <br>
 *     <br>
 * Created by DevNatres on 17/02/2015.
 */
public class ExpositionScreen implements Screen {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final DnaCamera mainCamera;

    private final Exposition exposition;
    private final LevelId levelId;

    private final HyperStore localHyperStore;

    public ExpositionScreen(DashGame dashGame, EExposition eExposition, LevelId levelId) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainCamera = dashGame.getCenteredMainCamera();

        localHyperStore = new HyperStore();
        this.exposition = eExposition.createExposition(dashGame, localHyperStore);
        this.levelId = levelId;
    }

    @Override
    public void render(float delta) {
        if (exposition == null || exposition.isFinished()) {
            if (levelId == null) {
                dashGame.setScreen(new LobbyScreen(dashGame));
            } else {
                dashGame.setScreen(LevelCreator.createLevel(dashGame, levelId));
            }
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        mainBatch.begin();
        exposition.render(mainBatch);
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
        if (exposition != null) exposition.dispose();
        localHyperStore.dispose();
    }
}
