package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.agentsystem.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.levelsystem.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;

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
        super(EAnimation.MINE.create(hyperStore));

        this.levelScreen = levelScreen;
        hero = levelScreen.getHero();
        explosion = new TransientAgent(EAnimation.MINE_EXPLOSION.create(hyperStore));
        explosionSound = hyperStore.getSound("sounds/hit.ogg");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getVolumeRectangle().overlaps(hero.getVolumeRectangle())) {
            hero.receiveDamage(DAMAGE);

            explosion.setCenter(getCenter());
            levelScreen.register(explosion, EAgentLayer.FLOOR);

            GlobalAudio.play(explosionSound, .1f);
            setVisible(false);
        }
    }
}
