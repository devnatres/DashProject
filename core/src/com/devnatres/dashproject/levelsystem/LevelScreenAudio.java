package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.audio.Music;

/**
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenAudio {
    Music badassMusic;
    Music endOkMusic;

    public LevelScreenAudio(LevelScreen levelScreen) {
        LevelScreenSet set = levelScreen.getSet();

        badassMusic = set.hyperStore.getMusic("music/badass.ogg");
        badassMusic.setLooping(true);
        endOkMusic = set.hyperStore.getMusic("music/end_ok.ogg");
    }
}
