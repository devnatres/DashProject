package com.devnatres.dashproject.resourcestore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.resourcestore.Store.EGlobalResourceType;

import static com.badlogic.gdx.graphics.Texture.*;

/**
 * Represents a collection of different media stores (see Store class). <br>
 * Music and sound are obtained from GlobalAudio class.<br>
 * <br>
 * Created by DevNatres on 06/12/2014.
 */
public class HyperStore implements Disposable {
    private final Store<Texture> textureStore;
    private final Store<Sound> soundStore;
    private final Store<Music> musicStore;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();

    public Texture getTexture(String resourceFileName) {
        return textureStore.getResource(resourceFileName);
    }

    /**
     * @return sound from GlobalAudio class
     */
    public Sound getSound(String resourceFileName) {
        return soundStore.getResource(resourceFileName);
    }

    /**
     * @return music from GlobalAudio class
     */
    public Music getMusic(String resourceFileName) {
        return musicStore.getResource(resourceFileName);
    }

    public HyperStore() {
        textureStore = new Store<Texture>(new Store.Generable<Texture>() {
            @Override
            public Texture generate(String resourceFileName) {
                Texture texture = new Texture(Gdx.files.internal(resourceFileName));
                texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                return texture;
            }
        });

        soundStore = new Store<Sound>(new Store.Generable<Sound>() {
            @Override
            public Sound generate(String resourceFileName) {
                return globalAudio.newSound(resourceFileName);
            }
        });

        musicStore = new Store<Music>(new Store.Generable<Music>() {
            @Override
            public Music generate(String resourceFileName) {
                return globalAudio.newMusic(resourceFileName);
            }
        });


    }

    /**
     * Music and sound are disposed with GlobalAudio class.
     * Other kind of resources are disposed as usual.
     */
    @Override
    public void dispose() {
        textureStore.dispose();
        soundStore.dispose(EGlobalResourceType.GLOBAL_SOUND);
        musicStore.dispose(EGlobalResourceType.GLOBAL_MUSIC);
    }
}
