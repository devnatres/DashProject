package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.agentsystem.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.agentsystem.Foe;
import com.devnatres.dashproject.agentsystem.Horde;
import com.devnatres.dashproject.agentsystem.Mine;
import com.devnatres.dashproject.dnagdx.DnaOrthogonalTiledMapRenderer;
import com.devnatres.dashproject.gameconstants.EScroll;
import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelscriptcmd.RegisterHordeCmd;
import com.devnatres.dashproject.levelscriptcmd.WaitHordeKilledCmd;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.space.BlockCell;
import com.devnatres.dashproject.space.BlockLayerWithHeight;
import com.devnatres.dashproject.space.BlockMapSlider;
import com.devnatres.dashproject.space.BlockMapSlider.EBlockDecision;
import com.devnatres.dashproject.space.DirectionSelector;
import com.devnatres.dashproject.tools.Tools;

import java.util.HashMap;

import static com.devnatres.dashproject.space.BlockCell.EBlockHeight;

/**
 * Represents a map of the game. <br>
 * In some way its an extension of LibGdx's OrthogonalTiledMapRenderer.<br>
 *     <br>
 * Created by DevNatres on 04/12/2014.
 */
public class LevelMap implements Disposable {
    private final TiledMap tiledMap;
    private final DnaOrthogonalTiledMapRenderer tiledMapRenderer;
    private final BlockMapSlider blockMapSlider;
    private final MapProperties tiledMapProperties;
    private final int mapWidth;
    private final int mapHeight;
    private final int tilePixelWidth;
    private final int tilePixelHeight;
    private final int mapPixelWidth;
    private final int mapPixelHeight;

    public LevelMap(String levelName, Batch batch) {
        String levelFileName = "maps/" + levelName + ".tmx";

        tiledMap = new TmxMapLoader().load(levelFileName);
        if (batch == null) {
            tiledMapRenderer = new DnaOrthogonalTiledMapRenderer(tiledMap);
        } else {
            tiledMapRenderer = new DnaOrthogonalTiledMapRenderer(tiledMap, batch);
        }

        tiledMapProperties = tiledMap.getProperties();
        mapWidth = tiledMapProperties.get("width", Integer.class);
        mapHeight = tiledMapProperties.get("height", Integer.class);
        tilePixelWidth = tiledMapProperties.get("tilewidth", Integer.class);
        tilePixelHeight = tiledMapProperties.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        TiledMapTileLayer blockLayer = (TiledMapTileLayer)tiledMap.getLayers().get("blocks");
        TiledMapTileLayer lowBlockLayer = (TiledMapTileLayer)tiledMap.getLayers().get("lowblocks");
        TiledMapTileLayer groundLayer = (TiledMapTileLayer)tiledMap.getLayers().get("ground");


        blockMapSlider = new BlockMapSlider(EBlockDecision.BLOCK_WHEN_TILE,
                new BlockLayerWithHeight(blockLayer, EBlockHeight.HIGH),
                new BlockLayerWithHeight(lowBlockLayer, EBlockHeight.LOW));

        // In the ground layer, the holes are the blocks.
        blockMapSlider.addBlockLayerWithHeight(EBlockDecision.BLOCK_WHEN_HOLE,
                new BlockLayerWithHeight(groundLayer, EBlockHeight.PLAIN));

        blockMapSlider.compile();
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
     * Given a rectangle that represents a volume,
     * this method indicates if the rectangle has adjacent obstacles,
     * setting that in the specified Direction objects.
     *
     * @param rectangle The dimensions and position of the volumeRect
     * @param coverDirection CoverDirection to be updated
     * @param lowCoverDirection CoverDirection of low blocks to be updated
     */
    public void updateTheseCoverDirections(Rectangle rectangle,
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
        if (isBlockWithSomeHeight(blockCell)) {
            if (blockCell.isHeight(EBlockHeight.LOW)) lowCoverDirection.setLeft();
            coverDirection.setLeft();
        }

        // Up
        xCheck = centerRectangleX;
        yCheck = (rectangle.getY() + rectangle.getHeight() + marginY);
        blockCell = getBlockAt(xCheck, yCheck);
        if (isBlockWithSomeHeight(blockCell)) {
            if (blockCell.isHeight(EBlockHeight.LOW)) lowCoverDirection.setUp();
            coverDirection.setUp();
        }

        // Right
        xCheck = (rectangle.getX() + rectangle.getWidth() + marginX);
        yCheck = centerRectangleY;
        blockCell = getBlockAt(xCheck, yCheck);
        if (isBlockWithSomeHeight(blockCell)) {
            if (blockCell.isHeight(EBlockHeight.LOW)) lowCoverDirection.setRight();
            coverDirection.setRight();
        }

        // Down
        xCheck = centerRectangleX;
        yCheck = (rectangle.getY() - marginY);
        blockCell = getBlockAt(xCheck, yCheck);
        if (isBlockWithSomeHeight(blockCell)) {
            if (blockCell.isHeight(EBlockHeight.LOW)) lowCoverDirection.setDown();
            coverDirection.setDown();
        }
    }

    private boolean isBlockWithSomeHeight(BlockCell blockCell) {
        return blockCell != null && !blockCell.isHeight(EBlockHeight.PLAIN);
    }

    private BlockCell getBlockAt(float x, float y) {
        return blockMapSlider.getBlockAt(x, y);
    }

    public boolean isBlockCell(int column, int row) {
        return blockMapSlider.getBlockCell(column, row) != null;
    }

    public int getMapPixelWidth() {
        return mapPixelWidth;
    }

    public int getMapPixelHeight() {
        return mapPixelHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }


    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * Extracts the script from the level map, as well as foes and other agents. <br>
     *     <br>
     * @return the number of hordes found.
     */
    public int extractLevelScript(LevelScreen levelScreen, HyperStore hyperStore, String scriptName) {
        int hordeCount = 0;
        MapLayer mapLayer = tiledMap.getLayers().get(scriptName);
        if (mapLayer == null) return hordeCount;

        extractMines(levelScreen, hyperStore);
        LevelScript levelScript = levelScreen.getLevelScript();
        HashMap<Integer, Horde> hordeHashMap = new HashMap<Integer, Horde>();
        MapProperties properties = mapLayer.getProperties();
        int stepNumber = 0;
        while (true) {
            String stepLine = properties.get("step"+stepNumber, String.class);
            if (stepLine == null) break;

            String[] command = stepLine.split(",");
            if (command[0].equals("createhorde")) {
                int n = extractLevelScript_createHorde(levelScreen, hyperStore, command, levelScript, hordeHashMap);
                hordeCount += n;
            } else if (command[0].equals("waithordekilled")) {
                extractLevelScript_waitHordeKilled(levelScreen, command, levelScript, hordeHashMap);
            } else if (command[0].equals("heroposition")) {
                int heroX = Integer.parseInt(command[1])*tilePixelWidth;
                int heroY = (mapHeight-1-Integer.parseInt(command[2])) * tilePixelHeight;
                levelScreen.getHero().setPosition(heroX, heroY);
            } else if (command[0].equals("backscroll")) {
                EScroll eScroll = EScroll.valueOf(command[1]);
                levelScreen.setBackScroll(eScroll);
            } else if (command[0].equals("forescroll")) {
                EScroll eScroll = EScroll.valueOf(command[1]);
                levelScreen.setForeScroll(eScroll);
            }
            stepNumber++;
        }
        return hordeCount;
    }

    private int extractLevelScript_createHorde(LevelScreen levelScreen,
                                               HyperStore hyperStore,
                                               String[] command,
                                               LevelScript levelScript,
                                               HashMap<Integer, Horde> hordeHashMap) {

        for (int i = 1; i < command.length; i++) {
            int hordeNumber = Integer.parseInt(command[i]);
            Horde horde = extractHorde(levelScreen, hyperStore, hordeNumber);
            levelScript.addCmd(new RegisterHordeCmd(levelScreen, horde));

            hordeHashMap.put(hordeNumber, horde);
        }
        return command.length - 1;
    }

    private void extractLevelScript_waitHordeKilled(LevelScreen levelScreen,
                                                    String[] command,
                                                    LevelScript levelScript,
                                                    HashMap<Integer, Horde> hordeHashMap) {
        for (int i = 1; i < command.length; i++) {
            int hordeNumber = Integer.parseInt(command[i]);
            Horde horde = hordeHashMap.get(hordeNumber);
            levelScript.addCmd(new WaitHordeKilledCmd(levelScreen, horde));
        }
    }

    public void extractMines(LevelScreen levelScreen, HyperStore hyperStore) {
        MapLayer mapLayer = tiledMap.getLayers().get("mines");
        if (mapLayer == null) return;

        MapObjects objects = mapLayer.getObjects();
        for(MapObject object: objects) {
            MapProperties properties = object.getProperties();
            Mine mine = new Mine(levelScreen, hyperStore);
            Vector2 position = new Vector2(properties.get("x", Float.class), properties.get("y", Float.class));
            mine.setPosition(position.x, position.y);
            levelScreen.register(mine, EAgentLayer.FLOOR);
        }
    }

    public Horde extractHorde(LevelScreen levelScreen, HyperStore hyperStore, int n) {
        Horde horde = new Horde(levelScreen);
        MapLayer mapLayer = tiledMap.getLayers().get("horde"+n);
        if (mapLayer == null) return horde;

        MapObjects objects = mapLayer.getObjects();
        for(MapObject object: objects) {
            MapProperties properties = object.getProperties();
            String foeType = properties.get("type", String.class);
            Foe foe;
            if (foeType == null || foeType.equals("robot")) {
                foe = Foe.buildRobot(levelScreen, hyperStore);
            } else {
                foe = Foe.buildTank(levelScreen, hyperStore);
            }
            horde.addLinked(foe);

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
            if (stepLine == null) break;

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

        if (sequenceAction.getActions().size > 0) foe.addAction(repeatAction);
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

    public int getColumn(float x) {
        return (int)(x / tilePixelWidth);
    }

    public int getRow(float y) {
        return (int)(y / tilePixelHeight);
    }

    public void setThisCellCenter(int column, int row, Vector2 center) {
        center.set((column*tilePixelWidth) + tilePixelWidth/2, (row*tilePixelHeight) + tilePixelHeight/2);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
    }
}
