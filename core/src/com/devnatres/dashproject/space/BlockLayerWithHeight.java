package com.devnatres.dashproject.space;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static com.devnatres.dashproject.space.BlockCell.*;

/**
 * Created by David on 09/01/2015.
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
