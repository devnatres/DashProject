package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.audio.Music;
import com.devnatres.dashproject.dnagdx.GlobalAudio;

/**
 * Auxiliary structure for the audio objects of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenAudio {
    final GlobalAudio globalAudio = GlobalAudio.getInstance();
    final Music gameMusic;
    final Music scoreMusic;
    final Music dieMusic;

    public LevelScreenAudio(LevelScreenSet set) {
        //TODO: less music duration?
        gameMusic = set.localHyperStore.getMusic("music/action.ogg");
        gameMusic.setLooping(true);

        scoreMusic = set.localHyperStore.getMusic("music/final.ogg");
        scoreMusic.setLooping(true);

        dieMusic = set.localHyperStore.getMusic("music/die.ogg");
    }
}
