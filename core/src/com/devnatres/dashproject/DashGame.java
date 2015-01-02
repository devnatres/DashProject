package com.devnatres.dashproject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.sidescreens.MainMenuScreen;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.VectorPool;

public class DashGame extends Game {
    private int screenWidth;
    private int screenHeight;

    private Application.ApplicationType appType;
    private SpriteBatch mainBatch;
    private BitmapFont mainFont;
    private ShapeRenderer mainShape;
    private OrthographicCamera mainCamera;

    private HyperStore hyperStore;

    private Texture texture;
    private TextureRegion textureRegion;
    private Sprite sprite;

    long initialFrameTime = System.nanoTime();
    long currentFrameTime;
    float deltaFrameTime;

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Application.ApplicationType getAppType() {
        return appType;
    }

    public SpriteBatch getMainBatch() {
        return mainBatch;
    }

    public BitmapFont getMainFont() {
        return mainFont;
    }

    public ShapeRenderer getMainShape() {
        return mainShape;
    }

    public OrthographicCamera getMainCamera() {
        return mainCamera;
    }

    public OrthographicCamera getCenteredMainCamera() {
        mainCamera.setToOrtho(false, screenWidth, screenHeight);
        return mainCamera;
    }

    public HyperStore getHyperStore() {
        return hyperStore;
    }

	@Override
	public void create() {
        VectorPool.initialize();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        appType = Gdx.app.getType();

        mainBatch = new SpriteBatch();
        mainFont = new BitmapFont();
        mainShape = new ShapeRenderer();

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, screenWidth, screenHeight);

        hyperStore = new HyperStore();

        this.setScreen(new MainMenuScreen(this));

        Debug.begin(mainCamera);

        initialFrameTime = System.nanoTime();

	}

	@Override
	public void render() {
        //TODO Enable try
        try {
            super.render(); // Render the Screen set in create()
            if (Debug.DEBUG) Debug.draw();

            timing();

            if (deltaFrameTime < Time.FPS_TIME) {
                // What to do if there is remaining time? Sleep. (But we could do other things.)
                Thread.sleep((long) (Time.FPS_TIME - deltaFrameTime) * 1000);

                // Check if there is still remaining time. In that case we consume it.
                timing();
                while (deltaFrameTime < Time.FPS_TIME) {
                    timing();
                }
            }

            initialFrameTime = currentFrameTime;
        } catch (Exception e) {
            if (Debug.DEBUG) Debug.drawError(e.toString());
            System.out.println(e.getMessage());
        }
    }

    private void timing() {
        currentFrameTime = System.nanoTime();
        deltaFrameTime = (currentFrameTime - initialFrameTime) / Time.NANO_TIME;
    }

    @Override
    public void dispose() {
        super.dispose(); // Call hide() for the current screen (there you should do "dispose()")

        mainBatch.dispose();
        mainFont.dispose();
        mainShape.dispose();

        hyperStore.dispose();

        if (Debug.DEBUG) Debug.end();
    }
}
