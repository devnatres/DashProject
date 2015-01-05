package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;

/**
 * Created by David on 28/12/2014.
 */
public class Horde {
    private final Array<Foe> foes = new Array();
    private int score;
    private int deadInComboFoeCount;
    private int deadCount;

    public void add(Foe foe) {
        foes.add(foe);
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
        // TODO It can be optimized if every foe notify its dead to its horde
        for (int i = 0, n = foes.size; i < n; i++) {
            if (!foes.get(i).isDying()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDeadInCombo() {
        return deadInComboFoeCount == foes.size;
    }

    public void processFoeDamageResult(FoeDamageResult foeDamageResult) {
        Foe foe = foeDamageResult.getFoe();
        if (foe != null) {
            score += foeDamageResult.getScore();
            if (deadCount == 0 || foeDamageResult.isDeadInCombo()) {
                deadInComboFoeCount++;

                if (deadInComboFoeCount == 2) {
                    deadInComboFoeCount = deadInComboFoeCount + 0;
                }
            }
            deadCount++;
        }
    }

    public int getScore() {
        return score;
    }

}
