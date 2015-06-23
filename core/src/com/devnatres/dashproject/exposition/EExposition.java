package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimFoe;
import com.devnatres.dashproject.animations.EAnimHero;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.tools.Tools;

/**
 * This enum is a builder for prefabricated expositions. <br>
 *     <br>
 * Created by DevNatres on 17/02/2015.
 */
public enum EExposition {
    NONE {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return new Exposition(dashgame, null);
        }
    },
    REMOVE_ME {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_movement.png"));

            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics1a.png"));

            Agent hero = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));
            hero.setPosition(10, 300);
            Vector2 targetPosition = new Vector2(100, 300);
            hero.addAction(Tools.getMoveToAction(hero.getPosition(), targetPosition, 3));
            figure1.add(hero);

            Agent robot = new Agent(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore));
            robot.setPosition(400, 300);
            figure1.add(robot);

            exposition.add(figure1);

            Figure figure2 = new Figure(hyperStore.getTexture("help/help_basics1b.png"));

            Agent hero2 = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));
            hero2.setPosition(100, 300);
            figure2.add(hero2);

            figure2.add(robot);

            Agent block = new Agent(hyperStore.getTexture("fx/pum.png"));
            block.setPosition(200, 300);
            figure2.add(block);

            exposition.add(figure2);

            return exposition;
        }
    },
    BASICS1 {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_movement.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics1a.png"));

            final Vector2 position1 = new Vector2(50, 300);
            final Vector2 position2 = new Vector2(200, 300);

            Agent dash = new Agent(EAnimHero.DASH_HALO_NORMAL.create(hyperStore));
            dash.setCenter(position1);
            SequenceAction dashActions = new SequenceAction();
            dashActions.addAction(createMoveToCenterAction(dash, position1));
            dashActions.addAction(new DelayAction(60f));
            dashActions.addAction(createMoveToCenterAction(dash, position2));
            dash.addAction(createRepeatAction(dashActions, 120f));
            figure1.add(dash);

            Agent hero = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));
            hero.setCenter(dash.getCenter());
            SequenceAction heroActions = new SequenceAction();
            heroActions.addAction(createMoveToCenterAction(hero, position1));
            heroActions.addAction(new DelayAction(60f));
            heroActions.addAction(createMoveToCenterAction(hero, position2));
            hero.addAction(createRepeatAction(heroActions, 120f));
            figure1.add(hero);

            Agent finger = new Agent(hyperStore.getTexture("help/help_finger.png"));
            SequenceAction fingerActions = new SequenceAction();
            fingerActions.addAction(createMoveToOutAction(finger));
            fingerActions.addAction(new DelayAction(30f));
            fingerActions.addAction(createMoveToCenterAction(finger, position2));
            fingerActions.addAction(new DelayAction(30f));
            finger.addAction(createRepeatAction(fingerActions, 120f));
            figure1.add(finger);

            exposition.add(figure1);

            // Figure 2
            /*Figure figure2 = new Figure(hyperStore.getTexture("help/help_basics1b.png"));

            Agent hero2 = new Agent(EAnimHero.HERO_WALKING.create(hyperStore));
            hero2.setPosition(100, 300);
            figure2.add(hero2);

            figure2.add(robot);

            Agent block = new Agent(hyperStore.getTexture("fx/pum.png"));
            block.setPosition(200, 300);
            figure2.add(block);

            exposition.add(figure2);
*/
            return exposition;
        }
    },
    BASICS2 {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return null;
        }
    },
    COMBOS {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return null;
        }
    },
    TANKS {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return null;
        }
    },
    LOW_OBSTACLES {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return null;
        }
    },
    MINES {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            return null;
        }
    };
    abstract public Exposition createExposition(DashGame dashgame, HyperStore hyperStore);

    private static MoveToAction createMoveToOutAction(Agent agent) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(DashGame.getInstance().getScreenWidth(), DashGame.getInstance().getScreenHeight());
        return moveToAction;
    }

    private static MoveToAction createMoveToCenterAction(Agent agent, Vector2 center) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(center.x - agent.getWidth() / 2f, center.y - agent.getHeight() / 2f);
        return moveToAction;
    }

    private static RepeatAction createRepeatAction(SequenceAction sequenceAction, float finalTime) {
        sequenceAction.addAction(new DelayAction(finalTime));

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);

        return repeatAction;
    }

}
