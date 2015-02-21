package com.devnatres.dashproject.debug;

/**
 * Represents a cell for debugging purposes.<br>
 *     <br>
 * Created by DevNatres on 08/01/2015.
 */
public class DebugCell {
    public static final int CELL_PIXEL_WIDTH = 32;
    public static final int CELL_PIXEL_HEIGHT = 32;

    private final int column;
    private final int row;
    private final int type;

    public DebugCell(int column, int row, int type) {
        this.column = column;
        this.row = row;
        this.type = type;
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
