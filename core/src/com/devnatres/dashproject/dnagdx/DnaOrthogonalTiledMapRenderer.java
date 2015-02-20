package com.devnatres.dashproject.dnagdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

/**
 * This class doesn't open neither end the batch when it is passed to the constructor.
 * Created by DevNatres on 20/02/2015.
 */
public class DnaOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {

    public DnaOrthogonalTiledMapRenderer(TiledMap map) {
        super(map);
    }

    public DnaOrthogonalTiledMapRenderer(TiledMap map, Batch batch) {
        super(map, batch);
    }

    public DnaOrthogonalTiledMapRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public DnaOrthogonalTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    @Override
    protected void beginRender () {
        AnimatedTiledMapTile.updateAnimationBaseTime();
        if (ownsSpriteBatch) {
            spriteBatch.begin();
        }
    }

    @Override
    protected void endRender () {
        if (ownsSpriteBatch) {
            spriteBatch.end();
        }
    }
}
