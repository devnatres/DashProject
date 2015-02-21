package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * A group of foes. <br>
 * <br>
 * Created by DevNatres on 28/12/2014.
 */
public class Horde {
    private static final float COMBO_SCORE_FACTOR = 1.5f;

    private final Array<Foe> foes = new Array();
    private int maxFoesCount;
    private int killedFoesCount;
    private HordeGroup hordeGroup;
    private final HordeDamageResult hordeDamageResult;
    private final LevelScreen levelScreen;
    private final Hero hero;

    private final Vector2 referencePosition;

    public Horde(LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
        hordeDamageResult = new HordeDamageResult();
        referencePosition = new Vector2();
        hero = levelScreen.getHero();
    }
    /**
     * Add the foe to the horde but it is not notify to the foe.<br>
     * It is useful if you want to maintain a same foe in multiple hordes for management purposes
     * additionally to the true horde in the game.
     */
    public void addUnlinked(Foe foe) {
        foes.add(foe);
        maxFoesCount++;
    }

    /**
     * Add the foe to the horde and let the foe know that horde.
     */
    public void addLinked(Foe foe) {
        addUnlinked(foe);
        foe.setHorde(this);
    }

    void setHordeGroup(HordeGroup hordeGroup) {
        this.hordeGroup = hordeGroup;
    }

    public Foe getFoe(int index) {
        return foes.get(index);
    }

    public int size() {
        return foes.size;
    }

    public boolean isKilled() {
        return killedFoesCount == maxFoesCount;
    }

    public void removeKilledFoes() {
        for (int i = 0; i < foes.size; i++) {
            Foe foe = foes.get(i);
            if (foe.isDying()) {
                foes.removeIndex(i);
                i--;
            }
        }
    }

    public void countKilledFoe() {
        killedFoesCount++;
    }

    public void addFoeDamageResult(FoeDamageResult foeDamageResult) {
        hordeDamageResult.sumFoeScore(foeDamageResult.getScore());

        if (killedFoesCount == 1 || foeDamageResult.isDeadInCombo()) {
            hordeDamageResult.sumDeadInCombo();
            if (hordeDamageResult.getDeadInComboCount() == maxFoesCount) {
                hordeDamageResult.markHordeCombo();
                hordeDamageResult.setComboScore((int)(hordeDamageResult.getFoeScore() * COMBO_SCORE_FACTOR));
                levelScreen.processHordeDamageResult(hordeDamageResult);
            }
        }

        if (isKilled()) hordeGroup.addHordeDamageResult(hordeDamageResult);
    }

    public Vector2 getReferencePosition() {
        boolean initialized = false;
        float minDistance2 = 0;
        float currentDistance2;
        for (int i = 0; i < foes.size; i++) {
            Foe foe = foes.get(i);
            if (!foe.isDying()) {
                if (initialized) {
                    currentDistance2 = hero.getCenter().dst2(foe.getCenter());
                    if (currentDistance2 < minDistance2) {
                        referencePosition.set(foe.getCenter());
                        minDistance2 = currentDistance2;
                    }
                } else {
                    initialized = true;
                    referencePosition.set(foe.getCenter());
                    minDistance2 = hero.getCenter().dst2(foe.getCenter());
                }
            }
        }
        return referencePosition;
    }
}
