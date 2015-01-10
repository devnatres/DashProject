package com.devnatres.dashproject.debug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by David on 08/01/2015.
 */
public class DebugCell {
    public static final int CELL_PIXEL_WIDTH = 32;
    public static final int CELL_PIXEL_HEIGHT = 32;

    private final int column;
    private final int row;
    private final int type;

    private final ShapeRenderer shape;


    public DebugCell(int column, int row, int type) {
        this.column = column;
        this.row = row;
        this.type = type;

        shape = new ShapeRenderer();
    }

    public int getType() {
        return type;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
