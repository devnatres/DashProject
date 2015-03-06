package com.devnatres.dashproject.scroll;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.tools.Tools;

/**
 * Represents a plane scroll. <br>
 * To be used with Scroll.
 *     <br>
 * Created by DevNatres on 06/03/2015.
 */
public class ScrollPlane {
    private final Vector2 position;
    private final Vector2 speed;
    private final float deepFactor;
    private final TextureRegion textureRegion;
    private final Vector2 effectiveVelocity;
    private final int width;
    private final int height;

    /**
     *
     * @param texture The texture will be set to a TextureWrap.Repeat wrap.
     * @param position The position of the texture. It never changes.
     * @param speed The constant displacement of the texture if we want to simulate automatic movement.
     * @param deepFactor Between -1 (foreground) and 1 (background). The closer to 0, the farther.
     */
    public ScrollPlane(Texture texture, Vector2 position, Vector2 speed, float deepFactor) {
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        textureRegion = new TextureRegion(texture);
        width = texture.getWidth();
        height = texture.getHeight();

        this.position = position;
        this.speed = speed;
        this.deepFactor = Tools.limitFloat(deepFactor, -1, 1);
        this.effectiveVelocity = new Vector2();
    }

    public void render(Batch batch, Vector2 cameraDisplacement) {
        batch.draw(textureRegion, position.x, position.y);
        effectiveVelocity.set(speed);
        effectiveVelocity.add(cameraDisplacement.x/width * deepFactor, -cameraDisplacement.y/height * deepFactor);
        textureRegion.scroll(effectiveVelocity.x, effectiveVelocity.y);
    }
}
