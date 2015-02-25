package com.devnatres.dashproject.tools;

import java.util.ArrayList;


/**
 * Pool for any class objects.
 */
public final class Pool<T> {
    public interface Generable<T> {
        public T generate();
    }

    private final ArrayList<T> freeObjects;
    private final Generable<T> generator;
    private final int maxSize;

    public Pool(Generable<T> generator, int maxSize, boolean completePool) {
        this.generator = generator;
        this.maxSize = maxSize;

        this.freeObjects = new ArrayList<T>(maxSize);

        if (completePool) {
            completePool();
        }
    }

    public T get() {
        return freeObjects.size() == 0 ?
                generator.generate() :
                freeObjects.remove(freeObjects.size() - 1);
    }

    public void recycle(T object) {
        if (freeObjects.size() < maxSize) {
            freeObjects.add(object);
        }
    }

    void completePool() {
        for (int i = freeObjects.size(); i < maxSize; i++) {
            recycle(generator.generate());
        }
    }
}
