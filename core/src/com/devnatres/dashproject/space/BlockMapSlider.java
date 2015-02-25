package com.devnatres.dashproject.space;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.gameconstants.EDirection;

import static com.devnatres.dashproject.space.BlockCell.*;

/**
 * Represents a "slider" map. <br>
 * This abstract map moves (slides) a rectangle that represents a volume to a nearby free position. <br>
 * It has other helper functions too.<br>
 * <br>
 * Created by DevNatres on 07/12/2014.
 */
public class BlockMapSlider {
    private final BlockCell[][] blockMap;
    private final int mapWidth;
    private final int mapHeight;
    private final int tilePixelWidth;
    private final int tilePixelHeight;
    private final int mapPixelWidth;
    private final int mapPixelHeight;

    private final Rectangle slidingRectangle;

    /**
     * The layer in the first blockLayersWithHeight can not be null.
     * Any layer can be empty, i.e., it's not null but it doesn't have blocks.
     */
    public BlockMapSlider(BlockLayerWithHeight... blockLayersWithHeight) {
        TiledMapTileLayer firstBlockLayer = blockLayersWithHeight[0].getBlockLayer();

        mapWidth = firstBlockLayer.getWidth();
        mapHeight = firstBlockLayer.getHeight();
        tilePixelWidth = (int)firstBlockLayer.getTileWidth();
        tilePixelHeight = (int)firstBlockLayer.getTileHeight();
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        slidingRectangle = new Rectangle();
        blockMap = new BlockCell[mapWidth][mapHeight];

        for (int i = 0; i < blockLayersWithHeight.length; i++) {
            TiledMapTileLayer blockLayer = blockLayersWithHeight[i].getBlockLayer();
            EBlockHeight blockHeight = blockLayersWithHeight[i].getBlockHeight();
            if (blockLayer!=null && blockHeight!=null) {
                addBlockLayer(blockLayer, blockHeight);
            }
        }

        calculateSliders();
    }

    /**
     * Additional block layer must have the same dimensions of the one passed to the constructor
     */
    public void addBlockLayer(TiledMapTileLayer blockLayer, EBlockHeight eBlockHeight) {
        if (mapWidth != blockLayer.getWidth()
            || mapHeight != blockLayer.getHeight()
            || tilePixelWidth != blockLayer.getTileWidth()
            || tilePixelHeight != blockLayer.getTileHeight()) {

            throw new RuntimeException("Layer dimensions don't match with the first one");
        }

        for (int column = 0; column < mapWidth; column++) {
            for (int row = 0; row < mapHeight; row++) {
                if (blockLayer.getCell(column, row) != null) {
                    blockMap[column][row] = new BlockCell(eBlockHeight);
                }
            }
        }
    }

    private void calculateSliders() {
        for (int column = 0; column < mapWidth; column++) {
            for (int row = 0; row < mapHeight; row++) {
                BlockCell blockCell = blockMap[column][row];
                if (blockCell == null) {
                    continue;
                }

                DirectionSelector directionSelector = blockCell.getDirectionSelector();
                setSlidingDirectionIfNoNeighbour(directionSelector, column, row, EDirection.UP);
                setSlidingDirectionIfNoNeighbour(directionSelector, column, row, EDirection.RIGHT);
                setSlidingDirectionIfNoNeighbour(directionSelector, column, row, EDirection.DOWN);
                setSlidingDirectionIfNoNeighbour(directionSelector, column, row, EDirection.LEFT);
            }
        }
    }

    /**
     * Check if there's a block in the specified EDirection from the specified cell (by column and row).
     * If there isn't a block then set that direction in the specified DirectionSelector.
     */
    private void setSlidingDirectionIfNoNeighbour(DirectionSelector directionSelector,
                                                      int column, int row,
                                                      EDirection direction) {
        int checkColumn = column + direction.getXUnit();
        int checkRow = row + direction.getYUnit();

        if (checkColumn>=0 && checkColumn<mapWidth && checkRow>=0 && checkRow<mapHeight) {
            if (blockMap[checkColumn][checkRow] == null) {
                direction.setDirection(directionSelector);
            }
        }
    }

    public boolean slideRectangle(Rectangle rectangle) {
        slidingRectangle.set(rectangle);

        boolean validPosition = calculateSlideRectangle(slidingRectangle);
        if (validPosition) {
            rectangle.set(slidingRectangle);
        } else {
            validPosition = calculateSlideRectangle(slidingRectangle);
            if (validPosition) {
                rectangle.set(slidingRectangle);
            }
        }

        return validPosition;
    }

    private boolean calculateSlideRectangle(Rectangle rectangle) {
        // Determine the grid of tiles overlapping the rectangle
        int minColumn = (int)(rectangle.getX() / tilePixelWidth);
        int maxColumn = (int)((rectangle.getX() + rectangle.getWidth()-1) / tilePixelWidth);
        int minRow = (int)(rectangle.getY() / tilePixelHeight);
        int maxRow = (int)((rectangle.getY() + rectangle.getHeight()-1) / tilePixelHeight);

        if (minColumn < 0) minColumn = 0;
        if (maxColumn >= mapWidth) maxColumn = mapWidth-1;
        if (minRow < 0) minRow = 0;
        if (maxRow >= mapHeight) maxRow = mapHeight-1;

        // Calculate the rectangle center
        float centerX = rectangle.getX() + (rectangle.getWidth() / 2);
        float centerY = rectangle.getY() + (rectangle.getHeight() / 2);
        int centerColumn = (int)(centerX / tilePixelWidth);
        int centerRow = (int)(centerY / tilePixelHeight);

        // Count the sliding directions
        boolean isBlockCollision = false;
        int extremeUp = 0, extremeRight = 0, extremeDown = mapHeight, extremeLeft = mapWidth;
        int countUp = 0, countRight = 0, countDown = 0, countLeft = 0;
        for (int column = minColumn; column <= maxColumn; column++) {
            for (int row = minRow; row <= maxRow; row++) {
                BlockCell blockCell =  blockMap[column][row];
                if (blockCell != null) {
                    DirectionSelector directionSelector = blockCell.getDirectionSelector();
                    isBlockCollision = true;
                    if (directionSelector.isUp() && centerRow >= row) {
                        countUp++;
                        if (row > extremeUp) extremeUp = row;
                    }
                    if (directionSelector.isRight() && centerColumn >= column) {
                        countRight++;
                        if (column > extremeRight) extremeRight = column;
                    }
                    if (directionSelector.isDown() && centerRow <= row) {
                        countDown++;
                        if (row < extremeDown) extremeDown = row;
                    }
                    if (directionSelector.isLeft() && centerColumn <= column) {
                        countLeft++;
                        if (column < extremeLeft) extremeLeft = column;
                    }
                }
            }
        }

        // If there are some sliders...
        boolean validPosition = true;
        if (countUp != 0 || countRight != 0 || countDown != 0 || countLeft != 0) {
            // ...move to the most numerous direction of the extreme cell in that direction
            if (firstMax(countUp, countRight, countDown, countLeft)) {
                float y = (extremeUp + 1) * tilePixelHeight;
                if (y + rectangle.getHeight() > mapPixelHeight) {
                    validPosition = false;
                }
                rectangle.setY(y);
            } else if (firstMax(countRight, countUp, countDown, countLeft)) {
                float x = (extremeRight + 1) * tilePixelWidth;
                if (x + rectangle.getWidth() > mapPixelWidth) {
                    validPosition = false;
                }
                rectangle.setX(x);
            } else if (firstMax(countDown, countUp, countRight, countLeft)) {
                float y = (extremeDown * tilePixelHeight) - rectangle.getHeight();
                if (y < 0) {
                    validPosition = false;
                }
                rectangle.setY(y);
            } else if (firstMax(countLeft, countUp, countRight, countDown)) {
                float x = (extremeLeft * tilePixelWidth) - rectangle.getWidth();
                if (x < 0) {
                    validPosition = false;
                }
                rectangle.setX(x);
            }

            // Check if there is still collision
            if (validPosition) {
                minColumn = (int) (rectangle.getX() / tilePixelWidth);
                maxColumn = (int) ((rectangle.getX() + rectangle.getWidth()-1) / tilePixelWidth);
                minRow = (int) (rectangle.getY() / tilePixelHeight);
                maxRow = (int) ((rectangle.getY() + rectangle.getHeight()-1) / tilePixelHeight);

                if (minColumn < 0) minColumn = 0;
                if (maxColumn >= mapWidth) maxColumn = mapWidth-1;
                if (minRow < 0) minRow = 0;
                if (minRow >= mapHeight) maxRow = mapHeight-1;

                for (int column = minColumn; column <= maxColumn; column++) {
                    for (int row = minRow; row <= maxRow; row++) {
                        if (blockMap[column][row] != null) return false;
                    }
                }
            }
        } else if (isBlockCollision) { // No sliders but there is collision
            validPosition = false;
        }

        return validPosition;
    }

    private boolean firstMax(int first, int a, int b, int c) {
        return first >= a && first >= b && first >= c;
    }

    /**
     * Based on Wikipedia article: http://es.wikipedia.org/wiki/Algoritmo_de_Bresenham
     */
    public boolean isCellLineCollision(int column0, int row0, int column1, int row1) {
        boolean segmentChange = false;
        int column, row, dColumn, dRow, p, incE, incNE, stepColumn, stepRow;
        dColumn = (column1 - column0);
        dRow = (row1 - row0);

        // Decide start and end point
        if (dRow < 0) {
            dRow *= -1;
            stepRow = -1;
        } else {
            stepRow = 1;
        }

        if (dColumn < 0) {
            dColumn *= -1;
            stepColumn = -1;
        } else {
            stepColumn = 1;
        }

        column = column0;
        row = row0;

        if (isCellLineCollision_Collide(column, row, segmentChange, stepColumn, stepRow)) {
            return false;
        }

        // Iterate to the end of the line
        if(dColumn > dRow) {
            p = 2*dRow - dColumn;
            incE = 2*dRow;
            incNE = 2*(dRow-dColumn);
            while (column != column1){
                column = column + stepColumn;
                if (p < 0){
                    p = p + incE;
                    segmentChange = false;
                } else {
                    row = row + stepRow;
                    p = p + incNE;
                    segmentChange = true;
                }
                if (isCellLineCollision_Collide(column, row, segmentChange, stepColumn, stepRow)) {
                    return false;
                }
            }
        } else {
            p = 2*dColumn - dRow;
            incE = 2*dColumn;
            incNE = 2*(dColumn-dRow);
            while (row != row1) {
                row = row + stepRow;
                if (p < 0) {
                    p = p + incE;
                    segmentChange = false;
                } else {
                    column = column + stepColumn;
                    p = p + incNE;
                    segmentChange = true;
                }
                if (isCellLineCollision_Collide(column, row, segmentChange, stepColumn, stepRow)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isCellLineCollision_Collide(int column, int row, boolean segmentChange, int stepColumn, int stepRow) {
        if (Debug.DEBUG) Debug.addTestCell(column, row, segmentChange, stepColumn, stepRow);

        BlockCell blockCell = blockMap[column][row];
        if (blockCell != null && blockCell.isHeight(EBlockHeight.HIGH)) {
            return true;
        } else if (segmentChange) {
            BlockCell antiAliasedBlockCell1 = blockMap[column-stepColumn][row];
            BlockCell antiAliasedBlockCell2 = blockMap[column][row-stepRow];
            if ((antiAliasedBlockCell1 != null && antiAliasedBlockCell1.isHeight(EBlockHeight.HIGH))
                || (antiAliasedBlockCell2 != null && antiAliasedBlockCell2.isHeight(EBlockHeight.HIGH))) {
                return true;
            }
        }

        return false;
    }

    public BlockCell getBlockAt(float x, float y) {
        int column = (int)(x / tilePixelWidth);
        int row = (int)(y / tilePixelHeight);

        return getBlockCell(column, row);
    }

    public BlockCell getBlockCell(int column, int row) {
        if (column < 0) {
            column = 0;
        } else if (column >= mapWidth) {
            column = mapWidth - 1;
        }

        if (row < 0) {
            row = 0;
        } else if (row >= mapHeight) {
            row = mapHeight - 1;
        }
        return blockMap[column][row];
    }
}
