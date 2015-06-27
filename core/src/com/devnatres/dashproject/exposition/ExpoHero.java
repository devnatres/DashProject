package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimHero;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper class that represents a hero and a dash-radio in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class ExpoHero extends ExpoAgent {

    private SequenceAction sequenceDashAction;
    private SequenceAction sequenceHeroAction;

    private final Agent dash;
    private final Agent hero;

    public ExpoHero(HyperStore hyperStore, Vector2 center) {
        this(hyperStore, center, false);
    }

    public ExpoHero(HyperStore hyperStore, Vector2 center, boolean crouched) {
        sequenceDashAction = new SequenceAction();
        sequenceHeroAction = new SequenceAction();

        dash = new Agent(EAnimHero.DASH_HALO_NORMAL.create(hyperStore));

        if (crouched) {
            hero = new Agent(EAnimHero.HERO_CROUCHING.create(hyperStore));
        } else {
            hero = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));
        }

        dash.setCenter(center);
        hero.setCenter(center);
    }

    @Override
    public void assignToFigure(Figure figure) {
        addAgentToFigure(dash, sequenceDashAction, figure);
        addAgentToFigure(hero, sequenceHeroAction, figure);
    }

    public void addMoveToCenterAction(Vector2 center, float time) {
        addMoveToCenterAndWaitAction(dash, sequenceDashAction, center, time);
        addMoveToCenterAndWaitAction(hero, sequenceHeroAction, center, time);
    }

    public void addMoveToCenterAction(Vector2 center) {
        addMoveToCenterAction(center, 0);
    }

    public void addHideAction() {
        addHideAndWaitAction(dash, sequenceDashAction, 0);
        addHideAndWaitAction(hero, sequenceHeroAction, 0);
    }

    public void addHideAction(float time) {
        addHideAndWaitAction(dash, sequenceDashAction, time);
        addHideAndWaitAction(hero, sequenceHeroAction, time);
    }

    public void addDisplayAction() {
        addDisplayAndWaitAction(dash, sequenceDashAction, 0);
        addDisplayAndWaitAction(hero, sequenceHeroAction, 0);
    }

    public void addHideAndDisplayAction(float time) {
        addHideAndDisplayAction(dash, sequenceDashAction, time);
        addHideAndDisplayAction(hero, sequenceHeroAction, time);
    }

    public void addDisplayAction(float time) {
        addDisplayAndWaitAction(dash, sequenceDashAction, time);
        addDisplayAndWaitAction(hero, sequenceHeroAction, time);
    }

}
