package com.devnatres.dashproject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.DnaShadowedFont;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.sidescreens.MainMenuScreen;
import com.devnatres.dashproject.tools.VectorPool;

/**
 * The game's main loop (specifically, the render() method).
 */
public class DashGame extends Game {
    private static DashGame instance;

    public static DashGame getInstance() {
        if (instance == null) {
            instance = new DashGame();
        }

        return instance;
    }

    /**
     * Assures a new instance, not a preexisting one from other execution. (Android issue.) <br>
     */
    public static DashGame newInstance() {
        instance = new DashGame();
        return instance;
    }

    public static final String TITLE = "Nebular Dash";
    public static final int INITIAL_SCREEN_WIDTH = 480;
    public static final int INITIAL_SCREEN_HEIGHT = 800;
    public static final boolean USE_ACCELEROMETER = false;
    public static final boolean USE_COMPASS = false;

    private Application.ApplicationType appType;
    private SpriteBatch mainBatch;
    private BitmapFont mainWhiteFont;
    private BitmapFont mainYellowFont;
    private DnaShadowedFont mainShadowedFont;
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

    private DashGame() {
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public SpriteBatch getMainBatch() {
        return mainBatch;
    }

    public BitmapFont getMainWhiteFont() {
        return mainWhiteFont;
    }

    public BitmapFont getMainYellowFont() {
        return mainYellowFont;
    }

    public DnaShadowedFont getMainShadowedFont() {
        return mainShadowedFont;
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

        appType = Gdx.app.getType();

        mainBatch = new SpriteBatch();

        mainWhiteFont = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
        mainYellowFont = new BitmapFont(Gdx.files.internal("fonts/yellow.fnt"), false);
        mainShadowedFont = new DnaShadowedFont();

        mainShape = new ShapeRenderer();

        mainCamera = new DnaCamera();
        mainCamera.setToOrtho(false, screenWidth, screenHeight);

        mainInputTranslator = new InputTranslator();

        GlobalAudio.newInstance();

        hyperStore = new HyperStore();
        gameState = new GameState(); // It must be created before any Screen
        this.setScreen(new MainMenuScreen(this));

        initialFrameTime = System.nanoTime();

        if (Debug.DEBUG) Debug.begin(mainCamera);
	}

	@Override
	public void render() {
        //TODO Enable try/catch
        //try {
            super.render(); // Render the Screen set in create()
            if (Debug.DEBUG) Debug.draw();

            timing();

            if (deltaFrameTime < Time.FPS_TIME) {
                // If there is remaining time we sleep the thread to help the system to do other tasks.
                // But we could execute in advance some heavy processes of the game.
                // TODO Remove this try/catch when the external try/catch is enabled
                try {
                    Thread.sleep((long) (Time.FPS_TIME - deltaFrameTime) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check if there is still remaining time. In that case we consume it.
                do {
                  timing();
                } while (deltaFrameTime < Time.FPS_TIME);
            }

            initialFrameTime = currentFrameTime;

            if (Debug.DEBUG) Debug.drawFrames();
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
        mainWhiteFont.dispose();
        mainYellowFont.dispose();
        mainShadowedFont.dispose();
        mainShape.dispose();

        hyperStore.dispose();

        if (Debug.DEBUG) Debug.end();
    }
}
