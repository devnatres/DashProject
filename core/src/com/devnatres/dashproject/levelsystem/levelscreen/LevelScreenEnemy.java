package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.Foe;
import com.devnatres.dashproject.agentsystem.HordeGroup;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.nonagentgraphics.Number;

/**
 * Auxiliary class that represents the management of the enemies. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
class LevelScreenEnemy {
    private static final int Y_MARGIN = 10;
    private static final int X_MARGIN = 10;

    private final HordeGroup hordeGroup;
    private int maxHordeCount;
    private int lastHordeCount;

    private final Array<Foe> comboLivingFoes = new Array();
    private Foe firstComboFoe;
    private Foe lastDeadFoe;

    private final Number hordeCountNumber;

    public LevelScreenEnemy(LevelScreen levelScreen, LevelScreenSet set, LevelScreenLevel level) {
        hordeGroup = new HordeGroup(levelScreen);

        LevelMap map = level.map;
        LevelId levelId = level.levelId;
        maxHordeCount = map.extractLevelScript(levelScreen, set.localHyperStore, levelId.getScriptName());

        hordeCountNumber = new Number(EAnimMedley.NUMBERS_GOLD.create(set.localHyperStore), Number.ENumberType.INTEGER);

        int x = DashGame.getInstance().getScreenWidth() - hordeCountNumber.getDigitWidth() - X_MARGIN;
        int y = DashGame.getInstance().getScreenHeight() - hordeCountNumber.getDigitHeight() - Y_MARGIN;
        hordeCountNumber.setUnitPosition(x, y);

        hordeCountNumber.setValueDirectly(maxHordeCount);
        lastHordeCount = maxHordeCount;
    }

    public void clearCombo() {
        comboLivingFoes.clear();
        firstComboFoe = null;
    }

    public boolean thereIsLastDeadFoe() {
        return lastDeadFoe != null;
    }

    public Vector2 getLastDeadFoeCenter() {
        return (lastDeadFoe != null) ? lastDeadFoe.getCenter() : null;
    }

    public void setLastDeadFoe(Foe foe) {
        lastDeadFoe = foe;
    }

    public boolean isFirstComboFoe(Foe foe) {
        return firstComboFoe == foe;
    }

    public boolean thereAreComboLivingFoesThatContains(Foe foe) {
        return comboLivingFoes.size == 0 || comboLivingFoes.contains(foe, true);
    }

    public void removeComboLivingFoe(Foe foe) {
        comboLivingFoes.removeValue(foe, true);
    }

    public void addComboFoe(Foe foe) {
        if (!foe.isDying()) {
            comboLivingFoes.add(foe);
        }

        if (firstComboFoe == null) {
            firstComboFoe = foe;
        }
    }

    public void restoreNotAttackedFoesAccordingToComboLivingFoes(Array<Foe> attackedFoes) {
        for (int i = 0; i < comboLivingFoes.size; i++) {
            Foe comboLivingFoe = comboLivingFoes.get(i);
            if (!attackedFoes.contains(comboLivingFoe, true)) {
                comboLivingFoe.recoverLife();
                if (firstComboFoe == comboLivingFoe) {
                    firstComboFoe = null;
                }
                comboLivingFoes.removeValue(comboLivingFoe, true);
                i--;
            }
        }
    }

    public HordeGroup getHordeGroup() {
        return hordeGroup;
    }

    public boolean isDeadHordeCountChanged() {
        return (lastHordeCount > hordeCountNumber.getIntValue()) && (hordeCountNumber.getIntValue() > 0);
    }

    public void updateLastHordeCount() {
        lastHordeCount = hordeCountNumber.getIntValue();
    }

    public Number getHordeCountNumber() {
        return hordeCountNumber;
    }

    public void subtractCurrentHordeCount(int removedHordes) {
        hordeCountNumber.sumValue(-removedHordes);
    }
}
