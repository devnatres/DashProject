package com.devnatres.dashproject.scroll;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a parallax scroll composed of ScrollPane objects. <br>
 *     <br>
 * Created by DevNatres on 06/03/2015.
 */
public class Scroll {
    private final ScrollPlane[] scrollPlanes;
    private final Vector2 zeroVector = new Vector2(0,0);

    /**
     * The planes order specifies the order they will be painted.
     */
    public Scroll(ScrollPlane... scrollPlanes) {
        this.scrollPlanes = scrollPlanes;
    }

    public void render(Batch batch, Vector2 cameraDisplacement) {
        for (int i = 0; i < scrollPlanes.length; i++) {
            scrollPlanes[i].render(batch, cameraDisplacement);
        }
    }

    public void render(Batch batch) {
        render(batch, zeroVector);
    }
}
