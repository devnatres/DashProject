package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.graphics.Texture;
import com.devnatres.dashproject.agentsystem.Agent;

/**
 * Auxiliary structure for the messages of LevelScreen. <br>
 *     <br>
 * Created by DevNatres on 24/02/2015.
 */
public class LevelScreenMessages {
    Agent messageAgent;
    int messageAgentDuration;
    final Agent lineAgent;
    final Texture dissipatedMessage;
    final Texture timeoutMessage;
    final Texture readyMessage;

    public LevelScreenMessages(LevelScreenSet set) {
        lineAgent = new Agent(set.localHyperStore.getTexture("fx/line.png"));
        dissipatedMessage = set.localHyperStore.getTexture("messages/message_dissipated.png");
        timeoutMessage = set.localHyperStore.getTexture("messages/message_timeout.png");
        readyMessage = set.localHyperStore.getTexture("messages/message_ready.png");
    }
}
