package com.devnatres.dashproject.resourcestore;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.dnagdx.GlobalAudio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents a store for media objects. <br>
 * It works as a flyweight pattern.
 * If the requested object hasn't been loaded, it is loaded and returned.
 * It the requested object has been loaded before, it is just returned.
 *     <br>
 * Created by DevNatres on 05/12/2014.
 */
public class Store<T extends Disposable> implements Disposable {
    public enum EGlobalResourceType {
        NONE {
            @Override
            void dispose(Disposable disposable) {
                disposable.dispose();
            }
        },
        GLOBAL_SOUND {
            @Override
            void dispose(Disposable disposable) {
                GlobalAudio.dispose((Sound)disposable);
            }
        },
        GLOBAL_MUSIC {
            @Override
            void dispose(Disposable disposable) {
                GlobalAudio.dispose((Music)disposable);
            }
        };
        abstract void dispose(Disposable disposable);
    }

    public interface Generable<T> {
        public T generate(String resourceFileName);
    }

    private HashMap<String, T> store;
    private Store.Generable<T> generator;

    public Store(Store.Generable<T> generator) {
        store = new HashMap<String, T>();
        this.generator = generator;
    }

    public T getResource(String resourceFileName) {
        T resource = store.get(resourceFileName);

        if (resource == null) {
            resource = generator.generate(resourceFileName);
            store.put(resourceFileName, resource);
        }

        return resource;
    }

    /**
     * Dispose the resource as usual if globalResourceType is NONE.
     * Dispose the resource through its global resource generator if other type than NONE is specified.
     */
    public void dispose(EGlobalResourceType globalResourceType) {
        Iterator iterator = store.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            T value = (T)entry.getValue();
            globalResourceType.dispose(value);
        }
        store.clear();
    }

    @Override
    public void dispose() {
        dispose(EGlobalResourceType.NONE);
    }
}
