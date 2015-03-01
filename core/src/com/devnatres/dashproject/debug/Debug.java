package com.devnatres.dashproject.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.Time;

import java.util.ArrayList;

/**
 * Debug supporting class. <br>
 *     <br>
 * Created by DevNatres on 08/12/2014.
 */
public class Debug {
    public static final boolean DEBUG = true;
    private static final boolean DEBUG_FRAMES = DEBUG && true;
    private static final boolean DEBUG_POINTS = DEBUG && false;
    private static final boolean DEBUG_RECTANGLES = DEBUG && false;
    private static final boolean DEBUG_COLLISIONS = DEBUG && false;

    public static final boolean IMMORTAL = true;

    private static ArrayList<Vector2> points;
    private static ArrayList<Color> pointColors;

    private static ArrayList<Rectangle> rectangles;
    private static ArrayList<Color> rectangleColors;

    private static ShapeRenderer shape;
    private static Batch batch;
    private static Camera gameCamera;
    private static OrthographicCamera debugCamera;
    private static boolean initialized;
    private static BitmapFont font;

    private static Array<DebugCell> testCells;

    private static int count;

    private static int lastFrameCount;
    private static int currentFrameCount;
    private static float initialFrameTime;
    private static float currentFrameTime;
    private static float[] lastFrameCounts = new float[5]; // Last seconds to consider for average fps.
    private static int lastFrameCountsIndex;
    private static float lastAvgFps;

    public static void begin(Camera gameCamera) {
        if (!DEBUG) return;

        points = new ArrayList<Vector2>();
        pointColors = new ArrayList<Color>();
        rectangles = new ArrayList<Rectangle>();
        rectangleColors = new ArrayList<Color>();
        testCells = new Array<DebugCell>();

        Debug.gameCamera = gameCamera;

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, DashGame.getGlobalScreenWidth(), DashGame.getGlobalScreenHeight());

        font = new BitmapFont();
        font.setColor(Color.RED);

        initialized = true;

        initialFrameTime = System.nanoTime();
    }

    public static void end() {
        if (!DEBUG) return;

        font.dispose();
        shape.dispose();
        batch.dispose();
    }

    public static void drawFrames() {
        if (!DEBUG_FRAMES) return;

        currentFrameCount++;
        currentFrameTime = System.nanoTime();
        float deltaFrameTime = (currentFrameTime - initialFrameTime) / Time.NANO_TIME;
        if (deltaFrameTime >= 1f) {
            initialFrameTime = currentFrameTime;
            lastFrameCount = currentFrameCount;
            currentFrameCount = 0;

            lastFrameCounts[lastFrameCountsIndex] = lastFrameCount;
            lastFrameCountsIndex++;
            if (lastFrameCountsIndex == lastFrameCounts.length) lastFrameCountsIndex = 0;

            lastAvgFps = 0;
            for (int i = 0; i < lastFrameCounts.length; i++) {
                lastAvgFps += lastFrameCounts[i];
            }
            lastAvgFps /= lastFrameCounts.length;
            lastAvgFps = (int)(lastAvgFps * 10f) / 10f;
        }

        batch.begin();
        font.draw(batch,"FPS: " + lastFrameCount
                + "   AVG(" + lastFrameCounts.length + "): " + lastAvgFps
                , 10, 15);
        batch.end();
    }

    public static void addPoint(float x, float y, Color color) {
        if (!DEBUG_POINTS) return;

        points.add(new Vector2(x, y));
        pointColors.add(color);
    }

    public static void addRectangleRef(Rectangle rectangle, Color color) {
        if (!DEBUG_RECTANGLES) return;

        if (!rectangles.contains(rectangle)) {
            rectangles.add(rectangle);
            rectangleColors.add(color);
        }
    }

    public static void removeRectangleRef() {
        if (!DEBUG_RECTANGLES) return;

        rectangles.clear();
        rectangleColors.clear();
    }

    public static void addTestCell(int column, int row, boolean segmentChange, int stepColumn, int stepRow) {
        if (!DEBUG_COLLISIONS) return;

        testCells.add(new DebugCell(column, row, 0));
        if (segmentChange) {
            testCells.add(new DebugCell(column - stepColumn, row, 1));
            testCells.add(new DebugCell(column, row - stepRow, 1));
        }
    }

    public static void drawError(String errorMessage) {
        if (!(initialized && DEBUG)) return;

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
        if (!(initialized && DEBUG)) return;

        if (DEBUG_POINTS) drawPoints(gameCamera);
        if (DEBUG_RECTANGLES) drawRectangles(gameCamera);
        if (DEBUG_COLLISIONS) drawCollisions(gameCamera);
    }

    private static void drawPoints(Camera gameCamera) {
        shape.setProjectionMatrix(gameCamera.combined);

        for (int i = 0, n = points.size(); i < n; i++) {
            Vector2 vector = points.get(i);
            Color color = pointColors.get(i);

            shape.setColor(color);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.circle(vector.x, vector.y, 5);
            shape.end();
        }
    }

    private static void drawRectangles(Camera gameCamera) {
        shape.setProjectionMatrix(gameCamera.combined);

        for (int i = 0, n = rectangles.size(); i < n; i++) {
            Rectangle rectangle = rectangles.get(i);
            Color color = rectangleColors.get(i);

            shape.setColor(color);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shape.end();
        }
    }

    private static void drawCollisions(Camera gameCamera) {
        shape.setProjectionMatrix(gameCamera.combined);

       for (int i = 0; i < testCells.size; i++) {
            DebugCell debugCell = testCells.get(i);
            if (debugCell.getType() == 0) {
                shape.setColor(255, 255, 255, .7f);
            } else {
                shape.setColor(128, 0, 128, .7f);
            }
           shape.begin(ShapeRenderer.ShapeType.Filled);
           shape.circle((debugCell.getColumn() * DebugCell.CELL_PIXEL_WIDTH) + DebugCell.CELL_PIXEL_WIDTH / 2,
                   (debugCell.getRow() * DebugCell.CELL_PIXEL_HEIGHT) + DebugCell.CELL_PIXEL_HEIGHT / 2,
                   DebugCell.CELL_PIXEL_WIDTH / 2);
            shape.end();
        }

        testCells.clear();
    }

    public static void addCount() {
        if (!DEBUG) return;
        count++;
    }

    public static int getCount() {
        return count;
    }

    public static void resetCount() {
        count = 0;
    }

    public static void doNothing() {}

    private Debug() {};
}
