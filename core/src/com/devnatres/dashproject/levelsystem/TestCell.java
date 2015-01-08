package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.devnatres.dashproject.DnaCamera;

/**
 * Created by David on 08/01/2015.
 */
public class TestCell {
    private static final int CELL_PIXEL_WIDTH = 32;
    private static final int CELL_PIXEL_HEIGHT = 32;

    private final int column;
    private final int row;
    private final int type;

    private final ShapeRenderer shape;


    public TestCell(int column, int row, int type) {
        this.column = column;
        this.row = row;
        this.type = type;

        shape = new ShapeRenderer();
    }

    public void draw(DnaCamera camera) {
        if (type == 0) {
            shape.setColor(255, 255, 255, .7f);
        } else {
            shape.setColor(128, 0, 128, .7f);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle((column * CELL_PIXEL_WIDTH) + CELL_PIXEL_WIDTH/2,
                (row * CELL_PIXEL_HEIGHT) + CELL_PIXEL_HEIGHT/2,
                CELL_PIXEL_WIDTH/2);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
