package com.devnatres.dashproject.agents;

import com.badlogic.gdx.audio.Sound;
import com.devnatres.dashproject.GlobalAudio;
import com.devnatres.dashproject.agents.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.gameconstants.EAnimations;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.store.HyperStore;

/**
 * Created by DevNatres on 14/02/2015.
 */
public class Mine extends Agent {
    private static final int DAMAGE = 3;

    private final LevelScreen levelScreen;
    private final TransientAgent explosion;
    private final Hero hero;
    private final Sound explosionSound;

    public Mine(LevelScreen levelScreen,
                HyperStore hyperStore) {
        super(EAnimations.MINE.create(hyperStore));

        this.levelScreen = levelScreen;
        hero = levelScreen.getHero();
        explosion = new TransientAgent(EAnimations.MINE_EXPLOSION.create(hyperStore));
        explosionSound = hyperStore.getSound("sounds/hit.ogg");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (auxArea.overlaps(hero.auxArea)) {
            hero.receiveDamage(DAMAGE);

            explosion.setCenter(getAuxCenter());
            levelScreen.register(explosion, EAgentLayer.FLOOR);

            GlobalAudio.play(explosionSound, .1f);
            setVisible(false);
        }
    }
}
