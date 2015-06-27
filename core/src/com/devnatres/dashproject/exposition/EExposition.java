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

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics1_a.png"), 200);

            final Vector2 position1 = new Vector2(80, 400);
            final Vector2 position2 = new Vector2(240, position1.y);

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
            Figure figure2 = new Figure(hyperStore.getTexture("help/help_basics1_b.png"), 200);

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
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_basics2.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_basics2_a.png"), 330);

            final Vector2 positionOrigin1 = new Vector2(80, 400);
            final Vector2 positionLimit1 = new Vector2(280, positionOrigin1.y);
            final Vector2 positionFinger1 = new Vector2(400, positionOrigin1.y);
            final Vector2 positionArrow1 = new Vector2((positionOrigin1.x+positionLimit1.x)/2f, positionOrigin1.y);

            ExpoHero expoHero1 = new ExpoHero(hyperStore, positionOrigin1);
            expoHero1.addMoveToCenterAction(positionOrigin1, 210);
            expoHero1.addMoveToCenterAction(positionLimit1);
            expoHero1.assignToFigure(figure1);

            ExpoAnim expoShadow1 = new ExpoAnim(createAnimation(hyperStore, "help/help_mark_shadow.png"), positionLimit1);
            expoShadow1.addHideAction(120);
            expoShadow1.addDisplayAndHideAction(90);
            expoShadow1.assignToFigure(figure1);

            ExpoAnim expoArrow1 = new ExpoAnim(createAnimation(hyperStore, "help/help_arrow.png"), positionArrow1);
            expoArrow1.addHideAction(120);
            expoArrow1.addDisplayAndHideAction(90);
            expoArrow1.assignToFigure(figure1);

            ExpoFinger expoFinger1 = new ExpoFinger(hyperStore, positionFinger1);
            expoFinger1.addHideAction(30);
            expoFinger1.addDisplayAndHideAnimAction();
            expoFinger1.assignToFigure(figure1);

            exposition.add(figure1);

            // Figure 2
            Figure figure2 = new Figure(hyperStore.getTexture("help/help_basics2_b.png"), 330);

            final Vector2 positionWall = new Vector2(240, 425);
            final Vector2 positionOrigin2 = new Vector2(100, positionWall.y);
            final Vector2 positionMid2 = new Vector2(240, 350);
            final Vector2 positionFinal2 = new Vector2(320, 500);
            final Vector2 positionFoe2 = new Vector2(350, 500);

            ExpoHero expoHero2 = new ExpoHero(hyperStore, positionOrigin2);
            expoHero2.addDisplayAction();
            expoHero2.addMoveToCenterAction(positionOrigin2, 120);
            expoHero2.addMoveToCenterAction(positionMid2, 120);
            expoHero2.addHideAction(42);
            expoHero2.addMoveToCenterAction(positionFinal2);
            expoHero2.addDisplayAction();
            expoHero2.assignToFigure(figure2);

            ExpoAnim expoFoe2 = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoe2);
            expoFoe2.addDisplayAction(240);
            expoFoe2.addHideAction();
            expoFoe2.assignToFigure(figure2);

            ExpoAnim expoFoeAttack2 = new ExpoAnim(EAnimFoe.FOE_ROBOT_DYING.create(hyperStore), positionFoe2);
            expoFoeAttack2.addHideAction(240);
            expoFoeAttack2.addDisplayAndHideAction(60);
            expoFoeAttack2.assignToFigure(figure2);

            ExpoHeroAttack expoHeroAttack2 = new ExpoHeroAttack(hyperStore, positionFinal2);
            expoHeroAttack2.addHideAction(240);
            expoHeroAttack2.addDisplayAndHideAction(45);
            expoHeroAttack2.assignToFigure(figure2);

            ExpoAnim expoBlock2 = new ExpoAnim(createAnimation(hyperStore, "help/help_wall_h.png"), positionWall);
            expoBlock2.assignToFigure(figure2);

            ExpoFinger expoFingerMid2 = new ExpoFinger(hyperStore, positionMid2);
            expoFingerMid2.addHideAction(30);
            expoFingerMid2.addDisplayAndHideAnimAction();
            expoFingerMid2.assignToFigure(figure2);

            ExpoFinger expoFingerFinal2 = new ExpoFinger(hyperStore, positionFinal2);
            expoFingerFinal2.addHideAction(150);
            expoFingerFinal2.addDisplayAndHideAnimAction();
            expoFingerFinal2.assignToFigure(figure2);

            exposition.add(figure2);

            return exposition;
        }
    },
    CHAINS {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_chains.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_chains_a.png"), 320);

            final Vector2 positionWall = new Vector2(240, 425);
            final Vector2 positionOrigin = new Vector2(100, positionWall.y);
            final Vector2 positionA = new Vector2(250, 350);
            final Vector2 positionB = new Vector2(350, 500);
            final Vector2 positionC = new Vector2(230, 560);
            final Vector2 positionFoeA = new Vector2(positionA.x-30, positionA.y);
            final Vector2 positionFoeB = new Vector2(positionB.x, positionB.y+30);
            final Vector2 positionFoeC = new Vector2(positionC.x-30, positionC.y);

            ExpoHero expoHero = new ExpoHero(hyperStore, positionOrigin);
            expoHero.addDisplayAction();
            expoHero.addMoveToCenterAction(positionOrigin, 120);
            expoHero.addHideAction(133);
            expoHero.addMoveToCenterAction(positionC);
            expoHero.addDisplayAction();
            expoHero.assignToFigure(figure1);

            ExpoAnim expoFoeA = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoeA);
            expoFoeA.addDisplayAction(120);
            expoFoeA.addHideAction();
            expoFoeA.assignToFigure(figure1);

            ExpoAnim expoFoeB = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoeB);
            expoFoeB.addDisplayAction(165);
            expoFoeB.addHideAction();
            expoFoeB.assignToFigure(figure1);

            ExpoAnim expoFoeC = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoeC);
            expoFoeC.addDisplayAction(210);
            expoFoeC.addHideAction();
            expoFoeC.assignToFigure(figure1);

            ExpoAnim expoFoeAttackA = new ExpoAnim(EAnimFoe.FOE_ROBOT_DYING.create(hyperStore), positionFoeA);
            expoFoeAttackA.addHideAction(120);
            expoFoeAttackA.addDisplayAndHideAction(45);
            expoFoeAttackA.assignToFigure(figure1);

            ExpoAnim expoFoeAttackB = new ExpoAnim(EAnimFoe.FOE_ROBOT_DYING.create(hyperStore), positionFoeB);
            expoFoeAttackB.addHideAction(165);
            expoFoeAttackB.addDisplayAndHideAction(45);
            expoFoeAttackB.assignToFigure(figure1);

            ExpoAnim expoFoeAttackC = new ExpoAnim(EAnimFoe.FOE_ROBOT_DYING.create(hyperStore), positionFoeC);
            expoFoeAttackC.addHideAction(210);
            expoFoeAttackC.addDisplayAndHideAction(45);
            expoFoeAttackC.assignToFigure(figure1);

            ExpoHeroAttack expoHeroAttackA = new ExpoHeroAttack(hyperStore, positionA);
            expoHeroAttackA.addHideAction(120);
            expoHeroAttackA.addDisplayAndHideAction(45);
            expoHeroAttackA.assignToFigure(figure1);

            ExpoHeroAttack expoHeroAttackB = new ExpoHeroAttack(hyperStore, positionB);
            expoHeroAttackB.addHideAction(165);
            expoHeroAttackB.addDisplayAndHideAction(45);
            expoHeroAttackB.assignToFigure(figure1);

            ExpoHeroAttack expoHeroAttackC = new ExpoHeroAttack(hyperStore, positionC);
            expoHeroAttackC.addHideAction(210);
            expoHeroAttackC.addDisplayAndHideAction(45);
            expoHeroAttackC.assignToFigure(figure1);

            ExpoAnim expoBlock = new ExpoAnim(createAnimation(hyperStore, "help/help_wall_h.png"), positionWall);
            expoBlock.assignToFigure(figure1);

            ExpoFinger expoFingerA = new ExpoFinger(hyperStore, positionA);
            expoFingerA.addHideAction(30);
            expoFingerA.addDisplayAndHideAnimAction();
            expoFingerA.assignToFigure(figure1);

            ExpoFinger expoFingerB = new ExpoFinger(hyperStore, positionB, true);
            expoFingerB.addHideAction(135);
            expoFingerB.addDisplayAndHideAnimAction();
            expoFingerB.assignToFigure(figure1);

            ExpoFinger expoFingerC = new ExpoFinger(hyperStore, positionC, true);
            expoFingerC.addHideAction(180);
            expoFingerC.addDisplayAndHideAnimAction();
            expoFingerC.assignToFigure(figure1);

            exposition.add(figure1);

            // Figure 2
            exposition.add(figure1, hyperStore.getTexture("help/help_chains_b.png"));

            return exposition;
        }
    },
    TANKS {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_tanks.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_tanks_a.png"), 300);

            final Vector2 positionWall = new Vector2(240, 425);
            final Vector2 positionA = new Vector2(240, 350);
            final Vector2 positionB = new Vector2(290, 520);
            final Vector2 positionC = new Vector2(280, 505);
            final Vector2 positionTank = new Vector2(positionB.x-30, positionB.y-15);


            ExpoAnim expoFoe = new ExpoAnim(EAnimFoe.FOE_TANK_WALKING.create(hyperStore), positionTank);
            expoFoe.addDisplayAction(120);
            expoFoe.addHideAction();
            expoFoe.assignToFigure(figure1);

            ExpoAnim expoTankStun = new ExpoAnim(EAnimFoe.FOE_TANK_STUNNING.create(hyperStore), positionTank);
            expoTankStun.addHideAction(120);
            expoTankStun.addDisplayAndHideAction(45);
            expoTankStun.assignToFigure(figure1);

            ExpoAnim expoTankDie = new ExpoAnim(EAnimFoe.FOE_TANK_DYING.create(hyperStore), positionTank);
            expoTankDie.addHideAction(166);
            expoTankDie.addDisplayAndHideAction(60);
            expoTankDie.assignToFigure(figure1);

            ExpoHeroAttack expoHeroAttackB = new ExpoHeroAttack(hyperStore, positionB);
            expoHeroAttackB.addHideAction(120);
            expoHeroAttackB.addDisplayAndHideAction(45);
            expoHeroAttackB.assignToFigure(figure1);

            ExpoHeroAttack expoHeroAttackC = new ExpoHeroAttack(hyperStore, positionC);
            expoHeroAttackC.addHideAction(166);
            expoHeroAttackC.addDisplayAndHideAction(45);
            expoHeroAttackC.assignToFigure(figure1);

            ExpoHero expoHero = new ExpoHero(hyperStore, positionA);
            expoHero.addMoveToCenterAction(positionA, 120);
            expoHero.addHideAction();
            expoHero.addMoveToCenterAction(positionC, 90);
            expoHero.addDisplayAction();
            expoHero.assignToFigure(figure1);

            ExpoAnim expoBlock = new ExpoAnim(createAnimation(hyperStore, "help/help_wall_h.png"), positionWall);
            expoBlock.assignToFigure(figure1);

            ExpoFinger expoFingerA = new ExpoFinger(hyperStore, positionB, true);
            expoFingerA.addHideAction(90);
            expoFingerA.addDisplayAndHideAnimAction();
            expoFingerA.assignToFigure(figure1);

            ExpoFinger expoFingerC = new ExpoFinger(hyperStore, positionC, true);
            expoFingerC.addHideAction(136);
            expoFingerC.addDisplayAndHideAnimAction();
            expoFingerC.assignToFigure(figure1);

            exposition.add(figure1);

            return exposition;
        }
    },
    LOW_BLOCKS {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_low_blocks.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_low_blocks_a.png"), 240);

            final Vector2 positionBlock = new Vector2(240, 425);
            final Vector2 positionOrigin = new Vector2(120, 300);
            final Vector2 positionFinal = new Vector2(240, 360);
            final Vector2 positionFoe = new Vector2(300, 500);

            ExpoHero expoHero = new ExpoHero(hyperStore, positionOrigin);
            expoHero.addDisplayAction(120);
            expoHero.addHideAction();
            expoHero.assignToFigure(figure1);

            ExpoHero expoHeroCrouched = new ExpoHero(hyperStore, positionFinal, true);
            expoHeroCrouched.addHideAction(120);
            expoHeroCrouched.addDisplayAction();
            expoHeroCrouched.assignToFigure(figure1);

            ExpoAnim expoFoe = new ExpoAnim(EAnimFoe.FOE_ROBOT_WALKING.create(hyperStore), positionFoe);
            expoFoe.addDisplayAction(240);
            expoFoe.addHideAction();
            expoFoe.assignToFigure(figure1);

            ExpoAnim expoBlock = new ExpoAnim(createAnimation(hyperStore, "help/help_wall_low.png"), positionBlock);
            expoBlock.assignToFigure(figure1);

            ExpoFinger expoFinger = new ExpoFinger(hyperStore, positionFinal);
            expoFinger.addHideAction(30);
            expoFinger.addDisplayAndHideAnimAction();
            expoFinger.assignToFigure(figure1);

            exposition.add(figure1);

            return exposition;

        }
    },
    MINES {
        @Override
        public Exposition createExposition(DashGame dashgame, HyperStore hyperStore) {
            Exposition exposition = new Exposition(dashgame, hyperStore.getTexture("help/help_mines.png"));

            // Figure 1
            Figure figure1 = new Figure(hyperStore.getTexture("help/help_mines_a.png"), 240);

            final Vector2 position = new Vector2(240, 450);

            ExpoAnim expoMine = new ExpoAnim(EAnimFoe.MINE.create(hyperStore), position);
            expoMine.assignToFigure(figure1);

            exposition.add(figure1);

            return exposition;
        }
    };

    private static DnaAnimation createAnimation(HyperStore hyperStore, String name) {
        return new DnaAnimation(Time.FRAME, new TextureRegion(hyperStore.getTexture(name)));
    }

    abstract public Exposition createExposition(DashGame dashgame, HyperStore hyperStore);

}
