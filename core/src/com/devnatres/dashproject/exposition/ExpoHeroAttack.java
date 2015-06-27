package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimHero;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Helper class that represents a hero attack and a dash-radio in an exposition. <br>
 *     <br>
 * Created by DevNatres on 24/06/2015.
 */
public class ExpoHeroAttack extends ExpoAgent {

    private SequenceAction sequenceDashAction;
    private SequenceAction sequenceHeroAttackAction;

    private final Agent dash;
    private final Agent heroAttack;

    public ExpoHeroAttack(HyperStore hyperStore, Vector2 center) {
        sequenceDashAction = new SequenceAction();
        sequenceHeroAttackAction = new SequenceAction();

        dash = new Agent(EAnimHero.DASH_HALO_NORMAL.create(hyperStore));
        heroAttack = new Agent(EAnimHero.HERO_ATTACKING.create(hyperStore));

        dash.setCenter(center);
        heroAttack.setCenter(center);
    }

    @Override
    public void assignToFigure(Figure figure) {
        addAgentToFigure(dash, sequenceDashAction, figure);
        addAgentToFigure(heroAttack, sequenceHeroAttackAction, figure);
    }

    public void addMoveToCenterAction(Vector2 center, float time) {
        addMoveToCenterAndWaitAction(dash, sequenceDashAction, center, time);
        addMoveToCenterAndWaitAction(heroAttack, sequenceHeroAttackAction, center, time);
    }

    public void addMoveToCenterAction(Vector2 center) {
        addMoveToCenterAction(center, 0);
    }

    public void addHideAction(float time) {
        addHideAndWaitAction(dash, sequenceDashAction, time);
        addHideAndWaitAction(heroAttack, sequenceHeroAttackAction, time);
    }

    public void addDisplayAction() {
        addDisplayAndWaitAction(dash, sequenceDashAction, 0);
        addDisplayAndWaitAction(heroAttack, sequenceHeroAttackAction, 0);
    }

    public void addDisplayAction(float time) {
        addDisplayAndWaitAction(dash, sequenceDashAction, time);
        addDisplayAndWaitAction(heroAttack, sequenceHeroAttackAction, time);
    }

    public void addDisplayAndHideAction(float time) {
        addDisplayAndHideAction(dash, sequenceDashAction, time);
        addDisplayAndHideAction(heroAttack, sequenceHeroAttackAction, time);
    }


}
