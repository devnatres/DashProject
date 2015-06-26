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

    private final SequenceAction sequenceDashAction;
    private final SequenceAction sequenceHeroAction;

    private final Agent dash;
    private final Agent hero;

    public ExpoHero(HyperStore hyperStore, Vector2 center) {
        sequenceDashAction = new SequenceAction();
        sequenceHeroAction = new SequenceAction();

        dash = new Agent(EAnimHero.DASH_HALO_NORMAL.create(hyperStore));
        hero = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));

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
}
