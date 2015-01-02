package com.devnatres.dashproject.space;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.devnatres.dashproject.gameconstants.EDirection;

/**
 * Created by DevNatres on 07/12/2014.
 */
public class BlockMapSlider {
    private final DirectionSelector[][] slidingMap;
    private final TiledMapTileLayer blockLayer;
    private final int mapWidth;
    private final int mapHeight;
    private final float tilePixelWidth;
    private final float tilePixelHeight;
    private final float mapPixelWidth;
    private final float mapPixelHeight;

    private final Rectangle slidingRectangle;

    public BlockMapSlider(TiledMapTileLayer blockLayer) {
        this.blockLayer = blockLayer;
        mapWidth = blockLayer.getWidth();
        mapHeight = blockLayer.getHeight();
        tilePixelWidth = blockLayer.getTileWidth();
        tilePixelHeight = blockLayer.getTileHeight();
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        slidingRectangle = new Rectangle();

        slidingMap = new DirectionSelector[mapWidth][mapHeight];

        for (int column = 0; column < mapWidth; column++) {
            for (int row = 0; row < mapHeight; row++) {
                if (blockLayer.getCell(column, row) == null) {
                    continue;
                }

                DirectionSelector directionSelector = new DirectionSelector();
                slidingMap[column][row] = directionSelector;

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

        if (checkColumn < 0 || checkColumn >= mapWidth) return;
        if (checkRow < 0 || checkRow >= mapHeight) return;

        if (blockLayer.getCell(checkColumn, checkRow) == null) {
            direction.setDirection(directionSelector);
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
                DirectionSelector directionSelector = slidingMap[column][row];
                if (directionSelector != null) {
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

        boolean validPosition = true;
        // If there are some sliders
        if (countUp != 0 || countRight != 0 || countDown != 0 || countLeft != 0) {
            // Move to the most numerous direction of the extreme cell in that direction
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
                        if (slidingMap[column][row] != null) return false;
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
}
