package com.devnatres.dashproject.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.dnagdx.DnaCamera;

import java.util.ArrayList;

/**
 * Debug supporting class. <br>
 *     <br>
 * Created by DevNatres on 08/12/2014.
 */
abstract public class Debug {
    public static final boolean DEBUG = false;
    private static final boolean DEBUG_FRAMES = DEBUG && true;
    private static final boolean DEBUG_POINTS = DEBUG && false;
    private static final boolean DEBUG_RECTANGLES = DEBUG && false;
    private static final boolean DEBUG_COLLISIONS = DEBUG && false;

    public static final boolean IMMORTAL = false;

    private static ArrayList<Vector2> points;
    private static ArrayList<Color> pointColors;

    private static ArrayList<Rectangle> rectangles;
    private static ArrayList<Color> rectangleColors;

    private static ShapeRenderer shape;
    private static Batch batch;
    private static DnaCamera gameCamera;
    private static DnaCamera debugCamera;
    private static boolean initialized;
    private static BitmapFont font;

    private static Array<DebugCell> testCells;

    private static int count;

    public static void begin(DnaCamera gameCamera) {
        if (!DEBUG) return;

        points = new ArrayList<Vector2>();
        pointColors = new ArrayList<Color>();
        rectangles = new ArrayList<Rectangle>();
        rectangleColors = new ArrayList<Color>();
        testCells = new Array<DebugCell>();

        Debug.gameCamera = gameCamera;

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        DashGame dashGame = DashGame.getInstance();
        debugCamera = new DnaCamera();

        font = new BitmapFont();
        font.setColor(Color.GREEN);

        count = 0;
        DebugFPS.start();

        initialized = true;
    }

    public static void end() {
        if (!DEBUG) return;

        font.dispose();
        shape.dispose();
        batch.dispose();

        initialized = false;
    }

    public static void drawFrames() {
        if (!DEBUG_FRAMES) return;

        DebugFPS.update();

        debugCamera.update();
        batch.setProjectionMatrix(debugCamera.combined);

        batch.begin();
        font.draw(batch,"ALPHA v0.8 - FPS: " + DebugFPS.measuredFPS
                + "   AVG(" + DebugFPS.measuredFpsList.length + "s): " + DebugFPS.avgFps
                + "   MIN(" + DebugFPS.measuredFpsList.length + "s): " + DebugFPS.minFps
                + "   MAX(" + DebugFPS.measuredFpsList.length + "s): " + DebugFPS.maxFps
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
