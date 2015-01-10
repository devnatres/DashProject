package com.devnatres.dashproject.space;

/**
 * Created by DevNatres on 09/01/2015.
 */
public class BlockCell {
    public enum EBlockHeight {
        HIGH,
        LOW
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
