package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.graphics.Texture;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * Created by DevNatres on 24/02/2015.
 */
public class LevelScreenMessages {
    Agent agentMessage;
    int agentMessageDuration;

    Texture dissipatedMessage;
    Texture timeoutMessage;
    Texture readyMessage;

    public LevelScreenMessages(LevelScreen levelScreen) {
        LevelScreenSet set = levelScreen.getSet();

        dissipatedMessage = set.hyperStore.getTexture("message_dissipated.png");
        timeoutMessage = set.hyperStore.getTexture("message_timeout.png");
        readyMessage = set.hyperStore.getTexture("message_ready.png");
    }
}
