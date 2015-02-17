package com.devnatres.dashproject.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.agents.Agent;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.resourcestore.HyperStore;
import com.devnatres.dashproject.tools.Tools;

/**
 * Created by DevNatres on 17/02/2015.
 */
public enum ETutorial {
    NONE {
        @Override
        public Tutorial createTutorial(HyperStore hyperStore) {
            return null;
        }
    },
    MOVEMENT {
        @Override
        public Tutorial createTutorial(HyperStore hyperStore) {
            Tutorial tutorial = new Tutorial(null);

            Figure figure1 = new Figure(null);

            Agent hero = new Agent(EAnimation.HERO_WALKING.create(hyperStore));
            hero.setPosition(10, 300);
            Vector2 targetPosition = new Vector2(100, 300);
            hero.addAction(Tools.getMoveToAction(hero.getAuxPosition(), targetPosition, 3));
            figure1.add(hero);

            Agent robot = new Agent(EAnimation.FOE_ROBOT_WALKING.create(hyperStore));
            robot.setPosition(400, 300);
            figure1.add(robot);

            tutorial.add(figure1);

            Figure figure2 = new Figure(null);

            Agent hero2 = new Agent(EAnimation.HERO_WALKING.create(hyperStore));
            hero2.setPosition(100, 300);
            figure2.add(hero2);

            figure2.add(robot);

            Agent block = new Agent(hyperStore.getTexture("pum.png"));
            block.setPosition(200, 300);
            figure2.add(block);

            tutorial.add(figure2);

            return tutorial;
        }
    };
    abstract public Tutorial createTutorial(HyperStore hyperStore);
}
