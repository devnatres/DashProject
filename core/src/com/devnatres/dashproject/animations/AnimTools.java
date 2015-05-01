package com.devnatres.dashproject.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper class for animation creator enums
 * <br>
 * Created by DevNatres on 01/05/2015.
 */
final class AnimTools {
    public static DnaAnimation create(HyperStore hyperStore,
                                      String fileName,
                                      int columns, int rows,
                                      float frameDuration,
                                      Animation.PlayMode playMode) {

        Texture texture = hyperStore.getTexture(fileName);

        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth()/columns,
                texture.getHeight()/rows);

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames.add(tmp[i][j]);
            }
        }

        DnaAnimation animation = new DnaAnimation(frameDuration, frames, playMode);
        return animation;
    }


    private AnimTools() {};
}
