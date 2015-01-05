package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;

/**
 * Created by David on 05/01/2015.
 */
public class HordeGroup {
    private final Array<Horde> hordes = new Array();
    private final Horde globalHorde = new Horde();
    private boolean isFullHordeComboAvailable = true;
    private int maxConsecutiveHordeComboCount;
    private int currentConsecutiveHordeComboCount;

    public void add(Horde horde) {
        hordes.add(horde);

        for (int i = 0; i < horde.size(); i++) {
            globalHorde.add(horde.getFoe(i));
        }
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

    public int processFoeDamageResult(FoeDamageResult foeDamageResult) {
        int scoreHordeCombo = 0;

        Foe foe = foeDamageResult.getFoe();
        if (foe != null) {
            Horde horde = foe.getHorde();
            horde.processFoeDamageResult(foeDamageResult);

            if (horde.isKilled()) {
                if (horde.isDeadInCombo()) {
                    currentConsecutiveHordeComboCount++;
                    if (currentConsecutiveHordeComboCount > maxConsecutiveHordeComboCount) {
                        maxConsecutiveHordeComboCount = currentConsecutiveHordeComboCount;
                    }
                    scoreHordeCombo = (int)(horde.getScore() * 1.5f);
                } else {
                    isFullHordeComboAvailable = false;
                    currentConsecutiveHordeComboCount = 0;
                }
            }
        }

        return scoreHordeCombo;
    }
}
