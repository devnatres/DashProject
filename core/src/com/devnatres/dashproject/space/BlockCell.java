package com.devnatres.dashproject.space;

/**
 * Represents a block cell of a map. <br>
 * A block cell has a height (EBlockHeight) and a DirectionSelect object for different purposes.
 * For example, the DirectionSelect object can be used to indicate the free adjacent cells of the block.
 *     <br>
 * Created by DevNatres on 09/01/2015.
 */
public class BlockCell {
    public enum EBlockHeight {
        HIGH,
        LOW,
        PLAIN //A block (it can't be occupied) without height
    }

    private final DirectionSelector directionSelector;
    private final EBlockHeight blockHeight;

    public BlockCell(EBlockHeight blockHeight) {
        directionSelector = new DirectionSelector();
        this.blockHeight = blockHeight;
    }

    public DirectionSelector getDirectionSelector() {
        return directionSelector;
    }

    public boolean isHeight(EBlockHeight blockHeight) {
        return this.blockHeight == blockHeight;
    }

}
