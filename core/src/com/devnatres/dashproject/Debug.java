package com.devnatres.dashproject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.gameconstants.Parameters;

import java.util.ArrayList;

/**
 * Created by DevNatres on 08/12/2014.
 */
public class Debug {
    public static final boolean DEBUG = true;
    private static final boolean DEBUG_POINTS = DEBUG && false;

    private static ArrayList<Vector2> points = new ArrayList<Vector2>();
    private static ArrayList<Color> pointColors = new ArrayList<Color>();

    private static ShapeRenderer shape;
    private static Batch batch;
    private static Camera gameCamera;
    private static OrthographicCamera debugCamera;
    private static boolean initialized;
    private static BitmapFont font;


    public static void begin(Camera gameCamera) {
        Debug.gameCamera = gameCamera;

        batch = new SpriteBatch();
        shape = new  ShapeRenderer();

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Parameters.INITIAL_SCREEN_WIDTH, Parameters.INITIAL_SCREEN_HEIGHT);

        font = new BitmapFont();
        font.setColor(Color.RED);

        initialized = true;
    }

    public static void end() {
        font.dispose();
        shape.dispose();
        batch.dispose();
    }

    private static boolean isDebugging(boolean flag) {
        return initialized && flag;
    }

    public static void addPoint(float x, float y, Color color) {
        points.add(new Vector2(x, y));
        pointColors.add(color);
    }

    public static void drawError(String errorMessage) {
        shape.setProjectionMatrix(debugCamera.combined);
        shape.setColor(Color.BLACK);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.box(20, 10, 0, 440, 50, 0);
        shape.end();

        batch.setProjectionMatrix(debugCamera.combined);

        batch.begin();
        font.draw(batch, errorMessage, 50, 50);
        batch.end();
    }

    public static void draw() {
        if (!isDebugging(DEBUG)) return;

        if (isDebugging(DEBUG_POINTS)) drawPoints(gameCamera);
    }

    private static void drawPoints(Camera game) {
        shape.setProjectionMatrix(game.combined);

        for (int i = 0, n = points.size(); i < n; i++) {
            Vector2 vector = points.get(i);
            Color color = pointColors.get(i);

            shape.setColor(color);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.circle(vector.x, vector.y, 5);
            shape.end();
        }
    }

    private Debug() {};
}
