package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.levelsystem.LevelScreen;

/**
 * Created by DevNatres on 05/01/2015.
 */
public class HordeGroup {
    private final Array<Horde> hordes = new Array();
    private final Horde globalHorde;
    private boolean isFullChainAvailable = true;
    private int maxConsecutiveChainCount;
    private int currentConsecutiveChainCount;
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
