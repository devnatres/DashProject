package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by David on 05/01/2015.
 */
public class HordeGroup {
    private final Array<Horde> hordes = new Array();
    private final Horde globalHorde;
    private boolean isFullHordeComboAvailable = true;
    private int maxConsecutiveHordeComboCount;
    private int currentConsecutiveHordeComboCount;
    private final LevelScreen levelScreen;
    private final HordeGroupDamageResult hordeGroupDamageResult;

    public HordeGroup(LevelScreen levelSCreen) {
        this.levelScreen = levelSCreen;
        globalHorde = new Horde(levelSCreen);

        hordeGroupDamageResult = new HordeGroupDamageResult();
    }

    public void addLinked(Horde horde) {
        hordes.add(horde);

        for (int i = 0; i < horde.size(); i++) {
            globalHorde.addUnlinked(horde.getFoe(i));
        }

        horde.setHordeGroup(this);
    }

    public Horde getGlobalHorde() {
        return globalHorde;
    }

    public void removeKilled() {
        for (int i = 0; i < hordes.size; i++) {
            Horde horde = hordes.get(i);
            if (horde.isKilled()) {
                hordes.removeIndex(i);
                i--;
            }
        }

        globalHorde.removeKilled();
    }

    public void processHordeDamageResult(HordeDamageResult hordeDamageResult) {
        if (hordeDamageResult.isDeadInHordeCombo()) {
            currentConsecutiveHordeComboCount++;
            if (currentConsecutiveHordeComboCount > maxConsecutiveHordeComboCount) {
                maxConsecutiveHordeComboCount = currentConsecutiveHordeComboCount;
            }
        } else {
            isFullHordeComboAvailable = false;
            currentConsecutiveHordeComboCount = 0;
        }
    }
}
