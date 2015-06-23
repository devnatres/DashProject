package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.resourcestore.HyperStore;

import java.util.ArrayList;

/**
 * Represents an exposition <br>
 *     <br>
 * Created by DevNatres on 17/02/2015.
 */
public class Exposition implements Disposable {
    private final ArrayList<Figure> figures = new ArrayList<Figure>();
    private Figure currentFigure;
    private int index;
    private boolean isFinished = true;
    private final InputTranslator mainInputTranslator;
    private final Texture title;
    private final Texture tapToNext;
    private final Texture tapToFinish;
    private final HyperStore localHyperStore;

    private final int screenWidth;
    private final int screenHeight;

    public Exposition(DashGame dashGame, Texture title) {
        this.title = title;

        localHyperStore = new HyperStore();
        tapToNext = localHyperStore.getTexture("help/help_taptonext.png");
        tapToFinish = localHyperStore.getTexture("help/help_taptofinish.png");

        screenWidth = dashGame.getScreenWidth();
        screenHeight = dashGame.getScreenHeight();

        mainInputTranslator = dashGame.getClearedMainInputTranslator();
    }

    public void add(Figure figure) {
        if (figure != null) {
            figures.add(figure);
            if (isFinished) isFinished = false;
            if (currentFigure == null) currentFigure = figure;
        }
    }

    public void render(Batch batch) {
        if (currentFigure == null) return;

        currentFigure.act();
        currentFigure.draw(batch);

        if (title != null)  batch.draw(title, 0, screenHeight-title.getHeight());

        if (index == figures.size()-1) {
            batch.draw(tapToFinish, 0, 0);
        } else {
            batch.draw(tapToNext, 0, 0);
        }

        if (mainInputTranslator.isTouchDown()) {
            nextFigure();
        }
    }

    private void nextFigure() {
        if (index < figures.size()-1) {
            index++;
            currentFigure = figures.get(index);
        } else {
            isFinished = true;
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void dispose() {
        localHyperStore.dispose();
    }
}
