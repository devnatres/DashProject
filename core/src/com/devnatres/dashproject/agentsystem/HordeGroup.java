package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * The group of alive hordes.
 * Created by DevNatres on 05/01/2015.
 */
public class HordeGroup {
    private final Array<Horde> hordes = new Array();
    private final Horde globalHorde;
    private boolean isFullChainAvailable = true;
    private int maxConsecutiveChainCount;
    private int currentConsecutiveChainCount;

    public HordeGroup(LevelScreen levelSCreen) {
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

    public int removeKilledHordes() {
        int removedHordes = 0;
        for (int i = 0; i < hordes.size; i++) {
            Horde horde = hordes.get(i);
            if (horde.isKilled()) {
                hordes.removeIndex(i);
                i--;
                removedHordes++;
            }
        }
        globalHorde.removeKilledFoes();

        return removedHordes;
    }

    public void addHordeDamageResult(HordeDamageResult hordeDamageResult) {
        if (hordeDamageResult.isDeadInChain()) {
            currentConsecutiveChainCount++;
            if (currentConsecutiveChainCount > maxConsecutiveChainCount) {
                maxConsecutiveChainCount = currentConsecutiveChainCount;
            }
        } else {
            isFullChainAvailable = false;
            currentConsecutiveChainCount = 0;
        }
    }

    public int getMaxConsecutiveChainCount() {
        return maxConsecutiveChainCount;
    }

    public boolean isFullChainAvailable() {
        return isFullChainAvailable;
    }
}
