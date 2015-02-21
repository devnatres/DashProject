package com.devnatres.dashproject.agentsystem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

/**
 * An AgentRegistry object contains agents order by layers.<br>
 * It automatically removes agents that became invisible.<br>
 * <br>
 * Created by DevNatres on 22/12/2014.
 */
public class AgentRegistry {

    public enum EAgentLayer {
        FLOOR,
        TRUNK,
        SCORE,
    }

    private static final int LAYER_SIZE = EAgentLayer.values().length;
    private final Array<Array<Agent>> layers = new Array(LAYER_SIZE);

    public AgentRegistry() {
        for (int i = 0; i < LAYER_SIZE; i++) {
            layers.add(new Array<Agent>());
        }
    }

    public void render(float delta, Batch batch) {
        for (int layerIndex = 0; layerIndex < layers.size; layerIndex++) {
            Array<Agent> layer = layers.get(layerIndex);
            for (int agentIndex = 0; agentIndex < layer.size; agentIndex++) {
                Agent agent = layer.get(agentIndex);
                agent.act(delta);
                if (agent.isVisible()) {
                    agent.draw(batch);
                } else {
                    layer.removeIndex(agentIndex);
                    agentIndex--;
                }
            }
        }
    }

    public Agent register(Agent agent, EAgentLayer layer) {
        if (!layers.get(layer.ordinal()).contains(agent, true)) {
            layers.get(layer.ordinal()).add(agent);
        }
        return agent;
    }

    public Horde register(Horde horde, EAgentLayer layer) {
        for (int i = 0, n = horde.size(); i < n; i++) {
            register(horde.getFoe(i), layer);
        }
        return horde;
    }
}
