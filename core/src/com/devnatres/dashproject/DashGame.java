package com.devnatres.dashproject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.sidescreens.MainMenuScreen;
import com.devnatres.dashproject.tools.VectorPool;

public class DashGame extends Game {
    public static final String TITLE = "Dash Project";
    public static final int INITIAL_SCREEN_WIDTH = 480;
    public static final int INITIAL_SCREEN_HEIGHT = 800;
    public static final boolean USE_ACCELEROMETER = false;
    public static final boolean USE_COMPASS = false;

    private static int globalScreenWidth = -1;
    private static int globalScreenHeight = -1;

    public static int getGlobalScreenWidth() {
        return globalScreenWidth;
    }

    public static int getGlobalScreenHeight() {
        return globalScreenHeight;
    }

    private Application.ApplicationType appType;
    private SpriteBatch mainBatch;
    private BitmapFont mainFont;
    private ShapeRenderer mainShape;
    private DnaCamera mainCamera;
    private InputTranslator mainInputTranslator;

    private int screenWidth;
    private int screenHeight;

    private HyperStore hyperStore;

    private long initialFrameTime = System.nanoTime();
    private long currentFrameTime;
    private float deltaFrameTime;

    private GameState gameState;

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

    public InputTranslator getClearedMainInputTranslator() {
        mainInputTranslator.clear();
        return mainInputTranslator;
    }

    public DnaCamera getMainCamera() {
        return mainCamera;
    }

    public DnaCamera getCenteredMainCamera() {
        mainCamera.setToOrtho(false, screenWidth, screenHeight);
        return mainCamera;
    }

    public HyperStore getHyperStore() {
        return hyperStore;
    }

    public GameState getGameState() {
        return gameState;
    }

	@Override
	public void create() {
        VectorPool.initialize();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        if (globalScreenWidth == -1) globalScreenWidth = screenWidth;
        if (globalScreenHeight == -1) globalScreenHeight = screenHeight;

        appType = Gdx.app.getType();

        mainBatch = new SpriteBatch();
        mainFont = new BitmapFont();
        mainShape = new ShapeRenderer();

        mainCamera = new DnaCamera();
        mainCamera.setToOrtho(false, screenWidth, screenHeight);

        mainInputTranslator = new InputTranslator();

        hyperStore = new HyperStore();
        gameState = new GameState(); // It must be created before any Screen
        this.setScreen(new MainMenuScreen(this));

        initialFrameTime = System.nanoTime();

        if (Debug.DEBUG) Debug.begin(mainCamera);
	}

	@Override
	public void render() {
        //TODO Enable try
        //try {
            super.render(); // Render the Screen set in create()
            if (Debug.DEBUG) Debug.draw();

            timing();

            if (deltaFrameTime < Time.FPS_TIME) {
                // What to do if there is remaining time? Sleep. (But we could do other things.)
                // TODO Enable Thread.sleep
                //Thread.sleep((long) (Time.FPS_TIME - deltaFrameTime) * 1000);

                // Check if there is still remaining time. In that case we consume it.
                timing();
                while (deltaFrameTime < Time.FPS_TIME) {
                    timing();
                }
            }

            initialFrameTime = currentFrameTime;
        /*} catch (Exception e) {
            if (Debug.DEBUG) Debug.drawError(e.toString());
            System.out.println(e.getMessage());
        }*/
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
