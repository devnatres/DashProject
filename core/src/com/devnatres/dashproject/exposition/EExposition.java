package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.animations.EAnimFoe;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.gameconstants.Time;
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
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_basics1.png"));

            final Vector2 position1 = new Vector2(80, 400);
            final Vector2 position2 = new Vector2(240, position1.y);

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics1a.png"), 200);

            ExpoHero expoHero1 = new ExpoHero(hyperStore, position1);
            expoHero1.addMoveToCenterAction(position1, 120);
            expoHero1.addMoveToCenterAction(position2);
            expoHero1.assignToFigure(figure1);

            ExpoFinger expoFinger1 = new ExpoFinger(hyperStore, position2);
            expoFinger1.addHideAction(30);
            expoFinger1.addDisplayAndHideAnimAction();
            expoFinger1.assignToFigure(figure1);

            exposition.add(figure1);

            // Figure 2
            Figure figure2 = new Figure(hyperStore.getTexture("help/help_basics1b.png"), 200);

            final Vector2 position3 = new Vector2(240, 350);
            final Vector2 position4 = new Vector2(240, 500);
            final Vector2 positionFoe = new Vector2(210, 500);
            final Vector2 positionWall = new Vector2(240, 425);

            ExpoHero expoHero2 = new ExpoHero(hyperStore, position3);
            expoHero2.addDisplayAction();
            expoHero2.addMoveToCenterAction(position3, 120);
            expoHero2.addHideAction(43);
            expoHero2.addMoveToCenterAction(position4);
            expoHero2.addDisplayAction();
            expoHero2.assignToFigure(figure2);

            ExpoAnim expoFoe2 = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoe);
            expoFoe2.addDisplayAction(120);
            expoFoe2.addHideAction();
            expoFoe2.assignToFigure(figure2);

            ExpoAnim expoFoeAttack2 = new ExpoAnim(EAnimFoe.FOE_ROBOT_DYING.create(hyperStore), positionFoe);
            expoFoeAttack2.addHideAction(120);
            expoFoeAttack2.addDisplayAndHideAction(45);
            expoFoeAttack2.assignToFigure(figure2);

            ExpoAnim expoBlock2 = new ExpoAnim(createAnimation(hyperStore, "help/help_wall_h.png"), positionWall);
            expoBlock2.assignToFigure(figure2);

            ExpoHeroAttack expoHeroAttack2 = new ExpoHeroAttack(hyperStore, position4);
            expoHeroAttack2.addHideAction(120);
            expoHeroAttack2.addDisplayAndHideAction(45);
            expoHeroAttack2.assignToFigure(figure2);

            ExpoFinger expoFinger2 = new ExpoFinger(hyperStore, position4);
            expoFinger2.addHideAction(30);
            expoFinger2.addDisplayAndHideAnimAction();
            expoFinger2.assignToFigure(figure2);

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

    private static DnaAnimation createAnimation(HyperStore hyperStore, String name) {
        return new DnaAnimation(Time.FRAME, new TextureRegion(hyperStore.getTexture(name)));
    }

    abstract public Exposition createExposition(DashGame dashgame, HyperStore hyperStore);

}
