package com.devnatres.dashproject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.devnatres.dashproject.sidescreens.SplashScreen;
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
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;
    public static final boolean USE_ACCELEROMETER = false;
    public static final boolean USE_COMPASS = false;
    private static final int DOUBLE_BACK_KEY_LAPSE = Time.getIntFPS(1.25f);

    private Application.ApplicationType appType;
    private SpriteBatch mainBatch;
    private BitmapFont mainWhiteFont;
    private BitmapFont mainYellowFont;
    private DnaShadowedFont mainShadowedFont;
    private DnaShadowedFont mainShadowedYellowFont;
    private ShapeRenderer mainShape;
    private DnaCamera mainCamera;
    private DnaCamera systemCamera;
    private InputTranslator mainInputTranslator;
    private int doubleBackKeyLapse;
    private Texture pressAgainTexture;

    private HyperStore hyperStore;

    private long initialFrameTime = System.nanoTime();
    private long currentFrameTime;
    private float deltaFrameTime;

    private GameState gameState;

    private DashGame() {
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
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

    public DnaShadowedFont getMainShadowedYellowFont() {
        return mainShadowedYellowFont;
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
        mainCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
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

        appType = Gdx.app.getType();

        mainBatch = new SpriteBatch();

        mainWhiteFont = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
        mainYellowFont = new BitmapFont(Gdx.files.internal("fonts/yellow.fnt"), false);
        mainShadowedFont = new DnaShadowedFont();
        mainShadowedYellowFont = new DnaShadowedFont(true);

        mainShape = new ShapeRenderer();

        mainCamera = new DnaCamera();
        systemCamera = new DnaCamera();

        pressAgainTexture = new Texture(Gdx.files.internal("messages/press_again.png"));

        mainInputTranslator = new InputTranslator();

        GlobalAudio.newInstance();

        hyperStore = new HyperStore();
        gameState = new GameState(); // It must be created before any Screen

        Gdx.input.setCatchBackKey(true);

        this.setScreen(new SplashScreen(this));

        initialFrameTime = System.nanoTime();

        if (Debug.DEBUG) Debug.begin(mainCamera);
	}

	@Override
	public void render() {
        //TODO Enable try/catch
        //try {
            super.render(); // Render the Screen set in create()
            if (Debug.DEBUG) Debug.draw();

            backKeyManagement();

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

    private void backKeyManagement() {
        if (doubleBackKeyLapse > 0) {
            doubleBackKeyLapse--;
            systemCamera.update();
            mainBatch.setProjectionMatrix(systemCamera.combined);
            mainBatch.begin();
            mainBatch.draw(pressAgainTexture, SCREEN_WIDTH/2 - pressAgainTexture.getWidth()/2, 120);
            mainBatch.end();
        }

        if (mainInputTranslator.isResetRequested()) {
            resetPushed();
        }
    }

    public void resetPushed() {
        if (doubleBackKeyLapse > 0) {
            Gdx.app.exit();
        } else {
            doubleBackKeyLapse = DOUBLE_BACK_KEY_LAPSE;
        }
    }

    @Override
    public void dispose() {
        super.dispose(); // Call hide() for the current screen (there you should do "dispose()")

        mainBatch.dispose();
        mainWhiteFont.dispose();
        mainYellowFont.dispose();
        mainShadowedFont.dispose();
        mainShadowedYellowFont.dispose();
        mainShape.dispose();

        pressAgainTexture.dispose();

        hyperStore.dispose();

        if (Debug.DEBUG) Debug.end();
    }
}
