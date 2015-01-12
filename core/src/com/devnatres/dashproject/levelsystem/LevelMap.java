package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.agents.Foe;
import com.devnatres.dashproject.agents.Horde;
import com.devnatres.dashproject.levelscriptcmd.CreateHordeCmd;
import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelscriptcmd.WaitHordeKilledCmd;
import com.devnatres.dashproject.space.BlockCell;
import com.devnatres.dashproject.space.BlockLayerWithHeight;
import com.devnatres.dashproject.space.BlockMapSlider;
import com.devnatres.dashproject.space.DirectionSelector;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.Tools;

import java.util.HashMap;

import static com.devnatres.dashproject.space.BlockCell.EBlockHeight;

/**
 * Created by DevNatres on 04/12/2014.
 */
public class LevelMap implements Disposable {
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final BlockMapSlider blockMapSlider;
    private final MapProperties tiledMapProperties;
    private final int mapWidth;
    private final int mapHeight;
    private final int tilePixelWidth;
    private final int tilePixelHeight;
    private final int mapPixelWidth;
    private final int mapPixelHeight;

    public LevelMap(String levelName) {
        String levelFileName = "levels/" + levelName + ".tmx";

        tiledMap = new TmxMapLoader().load(levelFileName);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tiledMapProperties = tiledMap.getProperties();
        mapWidth = tiledMapProperties.get("width", Integer.class);
        mapHeight = tiledMapProperties.get("height", Integer.class);
        tilePixelWidth = tiledMapProperties.get("tilewidth", Integer.class);
        tilePixelHeight = tiledMapProperties.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        TiledMapTileLayer blockLayer = (TiledMapTileLayer)tiledMap.getLayers().get("blocks");
        TiledMapTileLayer lowBlockLayer = (TiledMapTileLayer)tiledMap.getLayers().get("lowblocks");

        // Only lowBlockLayer can be null (that layer is not mandatory).
        // Any layer can be empty, i.e., it's not null but it doesn't have blocks.
        blockMapSlider = new BlockMapSlider(
                new BlockLayerWithHeight(blockLayer, EBlockHeight.HIGH),
                new BlockLayerWithHeight(lowBlockLayer, EBlockHeight.LOW));
    }

    public Batch getBatch() {
        return tiledMapRenderer.getSpriteBatch();
    }

    public void paint(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public boolean slide(Rectangle rectangle) {
        return blockMapSlider.slideRectangle(rectangle);
    }

    /**
     * @param rectangle The dimensions and position of the volume
     * @param coverDirection CoverDirection to be updated
     * @param lowCoverDirection CoverDirection of low blocks to be updated
     */
    public void updateCoverDirection(Rectangle rectangle,
                                     DirectionSelector coverDirection,
                                     DirectionSelector lowCoverDirection) {
        final float centerRectangleX = rectangle.getX() + rectangle.getWidth()/2;
        final float centerRectangleY = rectangle.getY() + rectangle.getHeight()/2;
        final float marginX = tilePixelWidth / 2;
        final float marginY = tilePixelHeight / 2;

        coverDirection.clear();
        lowCoverDirection.clear();

        // Left
        float xCheck = rectangle.getX() - marginX;
        float yCheck = centerRectangleY;
        BlockCell blockCell = getBlockAt(xCheck, yCheck);
        if (blockCell != null) {
            if (blockCell.isHeight(EBlockHeight.LOW)) {
                lowCoverDirection.setLeft();
            }
            coverDirection.setLeft();
        }
        if (Debug.DEBUG) Debug.addPoint(xCheck, yCheck, Color.GREEN);

        // Up
        xCheck = centerRectangleX;
        yCheck = (rectangle.getY() + rectangle.getHeight() + marginY);
        blockCell = getBlockAt(xCheck, yCheck);
        if (blockCell != null) {
            if (blockCell.isHeight(EBlockHeight.LOW)) {
                lowCoverDirection.setUp();
            }
            coverDirection.setUp();
        }
        if (Debug.DEBUG) Debug.addPoint(xCheck, yCheck, Color.GREEN);

        // Right
        xCheck = (rectangle.getX() + rectangle.getWidth() + marginX);
        yCheck = centerRectangleY;
        blockCell = getBlockAt(xCheck, yCheck);
        if (blockCell != null) {
            if (blockCell.isHeight(EBlockHeight.LOW)) {
                lowCoverDirection.setRight();
            }
            coverDirection.setRight();
        }
        if (Debug.DEBUG) Debug.addPoint(xCheck, yCheck, Color.GREEN);

        // Down
        xCheck = centerRectangleX;
        yCheck = (rectangle.getY() - marginY);
        blockCell = getBlockAt(xCheck, yCheck);
        if (blockCell != null) {
            if (blockCell.isHeight(EBlockHeight.LOW)) {
                lowCoverDirection.setDown();
            }
            coverDirection.setDown();
        }
        if (Debug.DEBUG) Debug.addPoint(xCheck, yCheck, Color.GREEN);
    }

    private BlockCell getBlockAt(float x, float y) {
        return blockMapSlider.getBlockAt(x, y);
    }

    public int getMapPixelWidth() {
        return mapPixelWidth;
    }

    public int getMapPixelHeight() {
        return mapPixelHeight;
    }

    public int getTilePixelWidth() {
        return tilePixelWidth;
    }

    public int getTilePixelHeight() {
        return tilePixelHeight;
    }

    /**
     *
     * @return number of hordes
     */
    public int extractLevelScript(HyperStore hyperStore, LevelScreen levelScreen) {
        int hordeCount = 0;
        MapLayer mapLayer = tiledMap.getLayers().get("levelscript");
        if (mapLayer == null) {
            return hordeCount;
        }

        LevelScript levelScript = levelScreen.getLevelScript();
        HashMap<Integer, Horde> hordeHashMap = new HashMap<Integer, Horde>();

        MapProperties properties = mapLayer.getProperties();
        int stepNumber = 0;
        while (true) {
            String stepLine = properties.get("step"+stepNumber, String.class);
            if (stepLine == null) {
                break;
            }

            String[] command = stepLine.split(",");

            if (command[0].equals("createhorde")) {
                for (int i = 1; i < command.length; i++) {
                    hordeCount++;
                    int hordeNumber = Integer.parseInt(command[i]);
                    Horde horde = extractHorde(hordeNumber, hyperStore, levelScreen);
                    levelScript.addCmd(new CreateHordeCmd(levelScreen, horde));

                    hordeHashMap.put(hordeNumber, horde);
                }
            } else if (command[0].equals("waithordekilled")) {
                for (int i = 1; i < command.length; i++) {
                    int hordeNumber = Integer.parseInt(command[i]);
                    Horde horde = hordeHashMap.get(hordeNumber);
                    levelScript.addCmd(new WaitHordeKilledCmd(levelScreen, horde));
                }
            } else if (command[0].equals("heroposition")) {
                levelScreen.getHero().setPosition(Integer.parseInt(command[1])*tilePixelWidth,
                        (mapHeight - 1 - Integer.parseInt(command[2]))*tilePixelHeight);
            }

            stepNumber++;
        }

        return hordeCount;
    }

    public Horde extractHorde(int n, HyperStore hyperStore, LevelScreen levelScreen) {
        Horde horde = new Horde(levelScreen);
        MapLayer mapLayer = tiledMap.getLayers().get("horde"+n);
        if (mapLayer == null) {
            return horde;
        }

        MapObjects objects = mapLayer.getObjects();
        for(MapObject object: objects) {
            MapProperties properties = object.getProperties();
            String foeType = properties.get("type", String.class);
            Foe foe;
            if (foeType == null || foeType.equals("robot")) {
                foe = new Foe(hyperStore, levelScreen);
                horde.addLinked(foe);
            } else {
                foe = null;
            }

            assignFoeActions(foe, properties);
        }

        return horde;
    }

    private void assignFoeActions(Foe foe, MapProperties properties) {
        Vector2 originalPos = new Vector2(properties.get("x", Float.class), properties.get("y", Float.class));
        foe.setPosition(originalPos.x, originalPos.y);

        Vector2 sourcePos = new Vector2(originalPos.x, originalPos.y);
        RepeatAction repeatAction = new RepeatAction();
        SequenceAction sequenceAction = new SequenceAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        int stepNumber = 0;
        while (true) {
            String stepLine = properties.get("step"+stepNumber, String.class);
            if (stepLine == null) {
                break;
            }

            String[] command = stepLine.split(",");

            if (command[0].equals("delay")) {
                Action action = new DelayAction(Integer.parseInt(command[1]));
                sequenceAction.addAction(action);
            } else if (command[0].equals("move")) {
                Action action = extractMoveAction(command, 1, sourcePos, originalPos, foe.getSpeed());
                sequenceAction.addAction(action);
            } else if (command[0].equals("delaymove")) {
                Action action = new DelayAction(Integer.parseInt(command[1]));
                sequenceAction.addAction(action);

                action = extractMoveAction(command, 2, sourcePos, originalPos, foe.getSpeed());
                sequenceAction.addAction(action);
            }
            stepNumber++;
        }

        if (sequenceAction.getActions().size > 0) {
            foe.addAction(repeatAction);
        }
    }

    private Action extractMoveAction(String[] command,
                                     int firstParamIndex,
                                     Vector2 sourcePosition,
                                     Vector2 originalPosition,
                                     float speed) {

        Vector2 targetPosition = new Vector2();
        float x;
        float y;
        if (command[firstParamIndex].equals("origin")) {
            x = originalPosition.x;
            y = originalPosition.y;
        } else {
            x = Integer.parseInt(command[firstParamIndex])*tilePixelWidth;
            y = (mapHeight - 1 - Integer.parseInt(command[firstParamIndex+1]))*tilePixelHeight;
        }

        targetPosition.set(x, y);
        return Tools.getMoveToActionNext(sourcePosition, targetPosition, speed);
    }

    public boolean isLineCollision(float x0, float y0, float x1, float y1) {
        int col0 = (int)(x0 / tilePixelWidth);
        int row0 = (int)(y0 / tilePixelHeight);
        int col1 = (int)(x1 / tilePixelWidth);
        int row1 = (int)(y1 / tilePixelHeight);

        return blockMapSlider.isCellLineCollision(col0, row0, col1, row1);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
    }
}
