package com.devnatres.dashproject.agents;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by David on 28/12/2014.
 */
public class Horde {
    private final Array<Foe> foes = new Array();
    private int foesCount; // It keeps the foes count although they were remove from the collection
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
     * Add the foe to the horde but it is not notify to the foe.
     */
    public void addUnlinked(Foe foe) {
        foes.add(foe);
        foesCount++;
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
        return killedFoesCount == foesCount;
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

    public void countKilledFoe(Foe foe) {
        killedFoesCount++;
    }

    public void processFoeDamageResult(FoeDamageResult foeDamageResult) {
        hordeDamageResult.sumFoeScore(foeDamageResult.getScore());

        if (killedFoesCount == 1 || foeDamageResult.isDeadInCombo()) {
            hordeDamageResult.sumDeadInComboFoe();
            if (hordeDamageResult.getDeadInComboFoeCount() == foesCount) {
                hordeDamageResult.markHordeCombo();
                hordeDamageResult.setComboScore((int)(hordeDamageResult.getFoeScore() * 1.5));
                levelScreen.processHordeDamageResult(hordeDamageResult);

            }
        }
        hordeGroup.processHordeDamageResult(hordeDamageResult);
    }

    public Vector2 getReferencePosition() {
        boolean initialized = false;
        float minDistance2 = 0;
        float currentDistance2;
        for (int i = 0; i < foes.size; i++) {
            Foe foe = foes.get(i);
            if (!foe.isDying()) {
                if (initialized) {
                    currentDistance2 = hero.getAuxCenter().dst2(foe.getAuxCenter());
                    if (currentDistance2 < minDistance2) {
                        referencePosition.set(foe.getAuxCenter());
                        minDistance2 = currentDistance2;
                    }
                } else {
                    initialized = true;
                    referencePosition.set(foe.getAuxCenter());
                    minDistance2 = hero.getAuxCenter().dst2(foe.getAuxCenter());
                }
            }
        }
        return referencePosition;
    }
}
