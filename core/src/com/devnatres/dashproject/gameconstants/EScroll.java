package com.devnatres.dashproject.gameconstants;

import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.scroll.Scroll;
import com.devnatres.dashproject.scroll.ScrollPlane;

/**
 * Prefabricated scrolls.<br>
 *     <br>
 * Created by DevNatres on 06/03/2015.
 */
public enum EScroll {
    SC00 {
        @Override
        public Scroll create(HyperStore hyperStore) {
            return null;
        }
    },
    SC01 {
        @Override
        public Scroll create(HyperStore hyperStore) {
            ScrollPlane groundScroll = new ScrollPlane(hyperStore.getTexture("bg_land.png"),
                    new Vector2(0f, 0f),
                    new Vector2(0f, -0.001f),
                    .1f);

            ScrollPlane cloudScroll = new ScrollPlane(hyperStore.getTexture("bg_clouds.png"),
                    new Vector2(0f, 0f),
                    new Vector2(0f, -0.005f),
                    .3f);

            return new Scroll(groundScroll, cloudScroll);
        }
    },
    ;
    abstract public Scroll create(HyperStore hyperStore);
}

