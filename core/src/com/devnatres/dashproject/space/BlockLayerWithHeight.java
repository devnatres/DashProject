package com.devnatres.dashproject.space;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static com.devnatres.dashproject.space.BlockCell.*;

/**
 * Represents a pair (block layer, block height). <br>
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
