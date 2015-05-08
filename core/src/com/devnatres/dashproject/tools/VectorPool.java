package com.devnatres.dashproject.tools;

import com.badlogic.gdx.math.Vector2;


/**
 * Specific pool for vectors.
 */
public final class VectorPool {
    public static final int MAX_VECTORS = 10;

    private static final Pool<Vector2> pool = new Pool<Vector2>(new Pool.Generable<Vector2>() {
        @Override
        public Vector2 generate() {
            return  new Vector2();
        }
    }, MAX_VECTORS, true);

    /**
     * To assure VectorPool class pre-loading, not obligatory
     */
    public static final void initialize() {}

    public static final Vector2 get() {
        return pool.get();
    }

    public static final void recycle(Vector2 vector) {
        pool.recycle(vector);
    }
}
