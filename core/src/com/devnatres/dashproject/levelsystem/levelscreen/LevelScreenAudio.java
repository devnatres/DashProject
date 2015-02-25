package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.audio.Music;

/**
 * Auxiliary structure for the audio objects of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenAudio {
    Music badassMusic;
    Music endOkMusic;

    public LevelScreenAudio(LevelScreenSet set) {
        badassMusic = set.hyperStore.getMusic("music/badass.ogg");
        badassMusic.setLooping(true);
        endOkMusic = set.hyperStore.getMusic("music/end_ok.ogg");
    }
}
