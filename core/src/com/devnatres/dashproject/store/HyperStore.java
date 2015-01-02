package com.devnatres.dashproject.store;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by DevNatres on 06/12/2014.
 */
public class HyperStore implements Disposable {
    private final Store<Texture> textureStore;
    private final Store<Sound> soundStore;
    private final Store<Music> musicStore;

    public Texture getTexture(String resourceFileName) {
        return textureStore.getResource(resourceFileName);
    }

    public Sound getSound(String resourceFileName) {
        return soundStore.getResource(resourceFileName);
    }

    public Music getMusic(String resourceFileName) {
        return musicStore.getResource(resourceFileName);
    }

    public HyperStore() {
        textureStore = new Store<Texture>(new Store.Generable<Texture>() {
            @Override
            public Texture generate(String resourceFileName) {
                return new Texture(Gdx.files.internal(resourceFileName));
            }
        });

        soundStore = new Store<Sound>(new Store.Generable<Sound>() {
            @Override
            public Sound generate(String resourceFileName) {
                return Gdx.audio.newSound(Gdx.files.internal(resourceFileName));
            }
        });

        musicStore = new Store<Music>(new Store.Generable<Music>() {
            @Override
            public Music generate(String resourceFileName) {
                return Gdx.audio.newMusic(Gdx.files.internal(resourceFileName));
            }
        });


    }

    @Override
    public void dispose() {
        textureStore.dispose();
        soundStore.dispose();
        musicStore.dispose();
    }
}
