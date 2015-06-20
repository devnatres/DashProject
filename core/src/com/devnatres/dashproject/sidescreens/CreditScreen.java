package com.devnatres.dashproject.sidescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.animations.EAnimButton;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents a game screen for credits. <br>
 *     <br>
 * Created by DevNatres on 20/01/2015.
 */
public class CreditScreen implements Screen, IButtonExecutable {
    private final DashGame dashGame;
    private final SpriteBatch mainBatch;
    private final DnaCamera mainCamera;

    private final InputTranslator mainInputTranslator;

    private final HyperStore localHyperStore;

    private final Texture background;
    private final Button backButton;

    public CreditScreen(DashGame dashGame) {
        this.dashGame = dashGame;
        mainBatch = dashGame.getMainBatch();
        mainCamera = dashGame.getCenteredMainCamera();

        localHyperStore = new HyperStore();
        background = localHyperStore.getTexture("screens/credits.png");
        backButton = new Button(240, 60,
                EAnimButton.BUTTON_OPT_BACK.create(localHyperStore),
                null,
                localHyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        mainInputTranslator = dashGame.getClearedMainInputTranslator();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.update();
        mainBatch.setProjectionMatrix(mainCamera.combined);

        Vector2 touchDownPointOnCamera = mainInputTranslator.getTouchDownPointOnCamera(mainCamera);
        backButton.act(Time.FRAME, touchDownPointOnCamera);

        mainBatch.begin();
        mainBatch.draw(background, 0, 0);
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
        }
    }
}
