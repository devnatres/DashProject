package com.devnatres.dashproject.store;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.GlobalAudio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by DevNatres on 05/12/2014.
 */
public class Store<T extends Disposable> implements Disposable {
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

    @Override
    public void dispose() {
        Iterator iterator = store.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            T value = (T)entry.getValue();
            value.dispose();
        }
        store.clear();
    }

    public void disposeGlobalMusic() {
        Iterator iterator = store.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            T value = (T)entry.getValue();
            GlobalAudio.dispose((Music)value);
        }
        store.clear();
    }

    public void disposeGlobalSound() {
        Iterator iterator = store.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            T value = (T)entry.getValue();
            GlobalAudio.dispose((Sound)value);
        }
        store.clear();
    }
}
