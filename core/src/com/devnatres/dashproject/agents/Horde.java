package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by David on 28/12/2014.
 */
public class Horde {
    private final Array<Foe> foes = new Array();
    private int killedFoesCount;
    private HordeGroup hordeGroup;
    private final HordeDamageResult hordeDamageResult;
    private final LevelScreen levelScreen;

    public Horde(LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
        hordeDamageResult = new HordeDamageResult();
    }
    /**
     * Add the foe to the horde but it is not notify to the foe.
     */
    public void addUnlinked(Foe foe) {
        foes.add(foe);
    }

    /**
     * Add the foe to the horde and let the foe know that horde.
     */
    public void addLinked(Foe foe) {
        foes.add(foe);
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

    public void removeKilled() {
        for (int i = 0; i < foes.size; i++) {
            Foe foe = foes.get(i);
            if (!foe.isVisible()) {
                foes.removeIndex(i);
                i--;
            }
        }
    }

    public boolean isKilled() {
        return killedFoesCount == foes.size;
    }

    public void countKilledFoe(Foe foe) {
        killedFoesCount++;
    }

    public void processFoeDamageResult(FoeDamageResult foeDamageResult) {
        hordeDamageResult.sumFoeScore(foeDamageResult.getScore());

        if (killedFoesCount == 1 || foeDamageResult.isDeadInCombo()) {
            hordeDamageResult.sumDeadInComboFoe();
            if (hordeDamageResult.getDeadInComboFoeCount() == foes.size) {
                hordeDamageResult.markHordeCombo();
                hordeDamageResult.setComboScore((int)(hordeDamageResult.getFoeScore() * 1.5));
                levelScreen.processHordeDamageResult(hordeDamageResult);

            }
        }
        hordeGroup.processHordeDamageResult(hordeDamageResult);
    }
}
