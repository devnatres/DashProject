package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.DnaAnimation;
import com.devnatres.dashproject.GlobalAudio;
import com.devnatres.dashproject.agents.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.space.CoordinateInt;
import com.devnatres.dashproject.store.HyperStore;
import com.devnatres.dashproject.tools.Tools;


/**
 * Created by DevNatres on 10/02/2015.
 */
public class PowerUp extends Agent {

    private static final int FAVORABLE_CASES = 3;
    private static final int POSSIBLE_CASES = 3;

    private static final int LATERAL_MARGIN = 4;

    private static final float EXTRA_TIME = 2.5f;

    private enum EPowerUpType {
        TIME {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return true;
            }

            @Override
            DnaAnimation getAnimation(HyperStore hyperStore) {
                return EAnimations.POWER_UP.create(hyperStore);
            }

            @Override
            void activateEffect(LevelScreen levelScreen, HyperStore hyperStore) {
                levelScreen.addTime(EXTRA_TIME);
                createMessage(levelScreen, hyperStore, EAnimations.POWER_UP_MESSAGE_TIME);
            }

        },
        LIFE {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasMaxLife();
            }

            @Override
            DnaAnimation getAnimation(HyperStore hyperStore) {
                return EAnimations.POWER_UP.create(hyperStore);
            }

            @Override
            void activateEffect(LevelScreen levelScreen, HyperStore hyperStore) {
                createMessage(levelScreen, hyperStore, EAnimations.POWER_UP_MESSAGE_LIFE);
            }
        },
        DASH {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasExtraDash();
            }

            @Override
            DnaAnimation getAnimation(HyperStore hyperStore) {
                return EAnimations.POWER_UP.create(hyperStore);
            }

            @Override
            void activateEffect(LevelScreen levelScreen, HyperStore hyperStore) {
                createMessage(levelScreen, hyperStore, EAnimations.POWER_UP_MESSAGE_DASH);
            }
        },
        IMMUNITY {
            @Override
            boolean isConvenient(LevelScreen levelScreen) {
                return !levelScreen.getHero().hasImmunity();
            }

            @Override
            DnaAnimation getAnimation(HyperStore hyperStore) {
                return EAnimations.POWER_UP.create(hyperStore);
            }

            @Override
            void activateEffect(LevelScreen levelScreen, HyperStore hyperStore) {
                createMessage(levelScreen, hyperStore, EAnimations.POWER_UP_MESSAGE_IMMUNITY);
            }
        };
        abstract boolean isConvenient(LevelScreen levelScreen);
        abstract DnaAnimation getAnimation(HyperStore hyperStore);
        abstract void activateEffect(LevelScreen levelScreen, HyperStore hyperStore);

        private static void createMessage(LevelScreen levelScreen, HyperStore hyperStore, EAnimations eAnimations) {
            TransientAgent agent = new TransientAgent(eAnimations.create(hyperStore));
            agent.setCenter(levelScreen.getHero().getAuxCenter());
            levelScreen.register(agent, EAgentLayer.SCORE);
        }
    }
    private static EPowerUpType[] powerUpTypes = EPowerUpType.values();

    public static void generatePowerUpIfLucky(HyperStore hyperStore,
                                              LevelScreen levelScreen,
                                              Vector2 basePosition) {

        boolean thereIsLuck = Tools.randomBoolean(FAVORABLE_CASES, POSSIBLE_CASES);
        if (!thereIsLuck) return;

        EPowerUpType type = selectType(levelScreen);

        if (type != null) {
            CoordinateInt coordinateInt = selectPosition(levelScreen, basePosition);

            LevelMap map = levelScreen.getMap();
            float x = map.getX(coordinateInt.a);
            float y = map.getY(coordinateInt.b);
            PowerUp powerUp = new PowerUp(hyperStore, levelScreen, type, x, y);
        }
    }

    private static EPowerUpType selectType(LevelScreen levelScreen) {
        int index = Tools.randomInt(0, powerUpTypes.length-1);
        int initialIndex = index;
        EPowerUpType powerUpType = powerUpTypes[index];
        while (!powerUpType.isConvenient(levelScreen)) {
            index++;
            if (index == powerUpTypes.length) {
                index = 0;
            }

            // Security check: if there isn't a valid power-up then do not iterate any more.
            if (initialIndex == index) {
                powerUpType = null;
                break;
            } else {
                powerUpType = powerUpTypes[index];
            }
        }

        return powerUpType;
    }

    private static CoordinateInt selectPosition(LevelScreen levelScreen, Vector2 referencePosition) {
        Hero hero = levelScreen.getHero();

        LevelMap levelMap = levelScreen.getMap();
        final int centerColumn = levelMap.getColumn(referencePosition.x);
        final int centerRow = levelMap.getRow(referencePosition.y);

        int iniColumn = centerColumn - LATERAL_MARGIN;
        if (iniColumn < 0) iniColumn = 0;

        int endColumn = centerColumn + LATERAL_MARGIN;
        if (endColumn >= levelMap.getMapWidth()) endColumn = levelMap.getMapWidth()-1;

        int iniRow = centerRow - LATERAL_MARGIN;
        if (iniRow < 0) iniRow = 0;

        int endRow = centerRow + LATERAL_MARGIN;
        if (endRow >= levelMap.getMapHeight()) endRow = levelMap.getMapHeight()-1;

        boolean isLookingForAPosition = true;
        boolean thereIsTargetPosition = false;
        int column = Tools.randomInt(iniColumn, endColumn);
        int row = Tools.randomInt(iniRow, endRow);
        while (isLookingForAPosition) {
            if (!levelMap.isBlockCell(column, row) && !hero.isOnCell(column, row)) {
                thereIsTargetPosition = true;
                isLookingForAPosition = false;
            } else {
                column++;
                if (column > endColumn) {
                    column = iniColumn;
                    row++;
                    if (row > endRow) {
                        row = iniRow;
                    }
                    if (column == iniColumn && row == iniRow) {
                        isLookingForAPosition = false;
                    }
                }
            }
        }

        return thereIsTargetPosition ? new CoordinateInt(column, row) : null;
    }

    final Hero hero;
    final Sound captureSound;
    final EPowerUpType type;
    final LevelScreen levelScreen;
    final HyperStore hyperStore;
    private PowerUp(HyperStore hyperStore,
                    LevelScreen levelScreen,
                    EPowerUpType type,
                    float x, float y) {
        super(type.getAnimation(hyperStore));

        setPosition(x, y);
        levelScreen.register(this, EAgentLayer.FLOOR);

        this.type = type;
        this.levelScreen = levelScreen;
        this.hyperStore = hyperStore;

        hero = levelScreen.getHero();
        captureSound = hyperStore.getSound("sounds/power_up.ogg");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (auxArea.overlaps(hero.auxArea)) {
            type.activateEffect(levelScreen, hyperStore);
            GlobalAudio.play(captureSound, .1f);
            setVisible(false);
        }
    }
}
