package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by DevNatres on 05/01/2015.
 */
public class HordeGroup {
    private final Array<Horde> hordes = new Array();
    private final Horde globalHorde;
    private boolean isFullHordeComboAvailable = true;
    private int maxConsecutiveHordeComboCount;
    private int currentConsecutiveHordeComboCount;
    private final LevelScreen levelScreen;

    public HordeGroup(LevelScreen levelSCreen) {
        this.levelScreen = levelSCreen;
        globalHorde = new Horde(levelSCreen);
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

    public Horde getHorde(int index) {
        return hordes.get(index);
    }

    public int size() {
        return hordes.size;
    }

    public void removeKilledHordes() {
        for (int i = 0; i < hordes.size; i++) {
            Horde horde = hordes.get(i);
            if (horde.isKilled()) {
                hordes.removeIndex(i);
                i--;
            }
        }
        globalHorde.removeKilledFoes();
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

    public int getMaxConsecutiveHordeComboCount() {
        return maxConsecutiveHordeComboCount;
    }

    public boolean isFullHordeComboAvailable() {
        return isFullHordeComboAvailable;
    }
}
