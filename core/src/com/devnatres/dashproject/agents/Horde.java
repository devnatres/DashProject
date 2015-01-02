package com.devnatres.dashproject.agents;

import com.badlogic.gdx.utils.Array;

/**
 * Created by David on 28/12/2014.
 */
public class Horde {
    private final Array<Foe> foes = new Array();

    public void add(Foe foe) {
        foes.add(foe);
    }

    public void add(Horde horde) {
        for (int i = 0, n = horde.size(); i < n; i++) {
            foes.add(horde.getFoe(i));
        }
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
        for (int i = 0, n = foes.size; i < n; i++) {
            if (foes.get(i).isVisible()) {
                return false;
            }
        }
        return true;
    }

}
