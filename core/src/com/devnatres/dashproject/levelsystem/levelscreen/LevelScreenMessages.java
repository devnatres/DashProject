package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.graphics.Texture;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * Auxiliary structure for the messages of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
public class LevelScreenMessages {
    Agent agentMessage;
    int agentMessageDuration;

    final Texture dissipatedMessage;
    final Texture timeoutMessage;
    final Texture readyMessage;

    public LevelScreenMessages(LevelScreenSet set) {
        dissipatedMessage = set.hyperStore.getTexture("message_dissipated.png");
        timeoutMessage = set.hyperStore.getTexture("message_timeout.png");
        readyMessage = set.hyperStore.getTexture("message_ready.png");
    }
}
