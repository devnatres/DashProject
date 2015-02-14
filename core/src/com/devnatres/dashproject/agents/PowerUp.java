package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
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
    private static final float FLYING_SPEED = 15f;

    private static final int FAVORABLE_CASES = 3; // TODO SET PROBABILITY
    private static final int POSSIBLE_CASES = 3;

    private static final int POWER_UP_RADIO2 = 48 * 48;

    private static final int ZONE_MARGIN = 4;

    private static final int MESSAGE_DURATION = 45;

    private static final float EXTRA_TIME = 2.5f;
    private static final int EXTRA_LIFE = 3;
    private static final int IMMUNITY_DURATION = 300;
    private static final int EXTRA_DASH_DURATION = 600;

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
            void activateEffect(LevelScreen levelScreen) {
                levelScreen.addTime(EXTRA_TIME);
            }

            @Override
            DnaAnimation getMessage(HyperStore hyperStore) {
                return EAnimations.POWER_UP_MESSAGE_TIME.create(hyperStore);
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
            void activateEffect(LevelScreen levelScreen) {
                levelScreen.getHero().addLife(EXTRA_LIFE);
            }

            @Override
            DnaAnimation getMessage(HyperStore hyperStore) {
                return EAnimations.POWER_UP_MESSAGE_LIFE.create(hyperStore);
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
            void activateEffect(LevelScreen levelScreen) {
                levelScreen.getHero().activateExtraDash(EXTRA_DASH_DURATION);
            }

            @Override
            DnaAnimation getMessage(HyperStore hyperStore) {
                return EAnimations.POWER_UP_MESSAGE_DASH.create(hyperStore);
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
            void activateEffect(LevelScreen levelScreen) {
                levelScreen.getHero().addImmunity(IMMUNITY_DURATION);
            }

            @Override
            DnaAnimation getMessage(HyperStore hyperStore) {
                return EAnimations.POWER_UP_MESSAGE_IMMUNITY.create(hyperStore);
            }
        };
        abstract boolean isConvenient(LevelScreen levelScreen);
        abstract DnaAnimation getAnimation(HyperStore hyperStore);
        abstract void activateEffect(LevelScreen levelScreen);
        abstract DnaAnimation getMessage(HyperStore hyperStore);

    }
    private static EPowerUpType[] powerUpTypes = EPowerUpType.values();

    public static void generatePowerUpIfLucky(HyperStore hyperStore,
                                              LevelScreen levelScreen,
                                              Vector2 baseCenter) {

        boolean thereIsLuck = Tools.randomBoolean(FAVORABLE_CASES, POSSIBLE_CASES);
        if (!thereIsLuck) return;

        EPowerUpType type = selectType(levelScreen);

        if (type != null) {
            CoordinateInt coordinateInt = selectPosition(levelScreen, baseCenter);
            Vector2 targetCenter = new Vector2();
            levelScreen.getMap().setCellCenter(coordinateInt.a, coordinateInt.b, targetCenter);
            PowerUp powerUP = new PowerUp(levelScreen, hyperStore, type, baseCenter, targetCenter);
            levelScreen.register(powerUP, EAgentLayer.FLOOR);
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

        int iniColumn = centerColumn - ZONE_MARGIN;
        if (iniColumn < 0) iniColumn = 0;

        int endColumn = centerColumn + ZONE_MARGIN;
        if (endColumn >= levelMap.getMapWidth()) endColumn = levelMap.getMapWidth()-1;

        int iniRow = centerRow - ZONE_MARGIN;
        if (iniRow < 0) iniRow = 0;

        int endRow = centerRow + ZONE_MARGIN;
        if (endRow >= levelMap.getMapHeight()) endRow = levelMap.getMapHeight()-1;

        Vector2 center = new Vector2();
        boolean isLookingForAPosition = true;
        boolean thereIsTargetPosition = false;
        int column = Tools.randomInt(iniColumn, endColumn);
        int row = Tools.randomInt(iniRow, endRow);
        while (isLookingForAPosition) {
            levelMap.setCellCenter(column, row, center);
            float distance2 = hero.getAuxCenter().dst2(center);
            if (!levelMap.isBlockCell(column, row) && distance2 > POWER_UP_RADIO2) {
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

    /*************************************************/
    /* NON STATIC PROPERTIES AND METHODS             */
    /*************************************************/

    final Hero hero;
    final Sound captureSound;
    final EPowerUpType type;
    final LevelScreen levelScreen;
    final HyperStore hyperStore;
    final Agent messageAgent;
    boolean flying;

    private PowerUp(LevelScreen levelScreen,
                    HyperStore hyperStore,
                    EPowerUpType type,
                    Vector2 baseCenter,
                    Vector2 targetCenter) {
        super(type.getAnimation(hyperStore));

        setCenter(baseCenter);

        this.type = type;
        this.levelScreen = levelScreen;
        this.hyperStore = hyperStore;

        hero = levelScreen.getHero();
        captureSound = hyperStore.getSound("sounds/power_up.ogg");

        messageAgent = new Agent(type.getMessage(hyperStore));
        flying = true;

        Vector2 basePosition = new Vector2(baseCenter).add(-getWidth() / 2, -getHeight() / 2);
        Vector2 targetPosition = new Vector2(targetCenter).add(-getWidth() / 2, -getHeight() / 2);
        Action moveAction = Tools.getMoveToAction(basePosition, targetPosition, FLYING_SPEED);
        addAction(moveAction);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (flying) {
            if (getActions().size == 0) {
                flying = false;
            }
        } else {
            float distance2 = hero.getAuxCenter().dst2(getAuxCenter());
            if (distance2 <= POWER_UP_RADIO2) {
                type.activateEffect(levelScreen);

                levelScreen.setAgentMessage(messageAgent, MESSAGE_DURATION);

                GlobalAudio.play(captureSound, .1f);
                setVisible(false);
            }
        }
    }
}
