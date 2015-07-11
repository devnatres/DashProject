package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.audio.Sound;
import com.devnatres.dashproject.agentsystem.AgentRegistry.EAgentLayer;
import com.devnatres.dashproject.animations.EAnimFoe;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * High level class to represent mines.<br>
 * <br>
 * Created by DevNatres on 14/02/2015.
 */
public class Mine extends Agent {
    private static final int DAMAGE = 2;

    private final LevelScreen levelScreen;
    private final TransientAgent explosion;
    private final Hero hero;

    private final GlobalAudio globalAudio = GlobalAudio.getInstance();
    private final Sound explosionSound;

    public Mine(LevelScreen levelScreen, HyperStore hyperStore) {
        super(EAnimFoe.MINE.create(hyperStore));

        this.levelScreen = levelScreen;
        hero = levelScreen.getHero();
        explosion = new TransientAgent(EAnimFoe.MINE_EXPLOSION.create(hyperStore));
        explosionSound = hyperStore.getSound("sounds/mine.ogg");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getVolumeRectangle().overlaps(hero.getVolumeRectangle())) {
            hero.receiveDamage(DAMAGE);

            explosion.setCenter(getCenter());
            levelScreen.register(explosion, EAgentLayer.FLOOR);

            globalAudio.play(explosionSound, .5f);
            setVisible(false);
        }
    }
}
