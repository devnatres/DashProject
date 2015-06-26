package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.resourcestore.HyperStore;

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
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_movement.png"));

            final Vector2 position1 = new Vector2(50, 300);
            final Vector2 position2 = new Vector2(200, 300);

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics1a.png"), 180);

            ExpoHero expoHero = new ExpoHero(hyperStore, position1);
            expoHero.addMoveToCenterAction(position1, 60f);
            expoHero.addMoveToCenterAction(position2);
            expoHero.assignToFigure(figure1);

            ExpoFinger expoFinger = new ExpoFinger(hyperStore, position2);
            expoFinger.addHideAction(30f);
            expoFinger.addDisplayAction();
            expoFinger.assignToFigure(figure1);

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
}
