package com.devnatres.dashproject.space;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static com.devnatres.dashproject.space.BlockCell.*;

/**
 * Represents a pair (block layer, block height). <br>
 * It's not responsibility of this class to interpret the block layer, i.e.,
 * if a tile is a block or not; it depends on the user's interpretation.
 *     <br>
 * Created by DevNatres on 09/01/2015.
 */
public class BlockLayerWithHeight {
    private final TiledMapTileLayer blockLayer;
    private final EBlockHeight blockHeight;

    public BlockLayerWithHeight(TiledMapTileLayer blockLayer, EBlockHeight blockHeight) {
        this.blockLayer = blockLayer;
        this.blockHeight = blockHeight;
    }

    public TiledMapTileLayer getBlockLayer() {
        return blockLayer;
    }

    public EBlockHeight getBlockHeight() {
        return blockHeight;
    }

}
