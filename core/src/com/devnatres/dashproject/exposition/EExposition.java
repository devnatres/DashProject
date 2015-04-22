package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.gameconstants.EAnimation;
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
    BASICS1 {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("message_movement.png"));

            Figure figure1 = new Figure(hyperStore.getTexture("message_basics1a.png"));

            Agent hero = new Agent(EAnimation.HERO_WALKING.create(hyperStore));
            hero.setPosition(10, 300);
            Vector2 targetPosition = new Vector2(100, 300);
            hero.addAction(Tools.getMoveToAction(hero.getPosition(), targetPosition, 3));
            figure1.add(hero);

            Agent robot = new Agent(EAnimation.FOE_ROBOT_WALKING.create(hyperStore));
            robot.setPosition(400, 300);
            figure1.add(robot);

            exposition.add(figure1);

            Figure figure2 = new Figure(hyperStore.getTexture("message_basics1b.png"));

            Agent hero2 = new Agent(EAnimation.HERO_WALKING.create(hyperStore));
            hero2.setPosition(100, 300);
            figure2.add(hero2);

            figure2.add(robot);

            Agent block = new Agent(hyperStore.getTexture("pum.png"));
            block.setPosition(200, 300);
            figure2.add(block);

            exposition.add(figure2);

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
}