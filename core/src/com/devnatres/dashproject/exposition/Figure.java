package com.devnatres.dashproject.exposition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.gameconstants.Time;

import java.util.ArrayList;

/**
 * Represents an animated figure for expositions. <br>
 * It is composed with agents.
 *     <br>
 * Created by DevNatres on 17/02/2015.
 */
public class Figure {
    private final ArrayList<Agent> agents = new ArrayList<Agent>();
    private Texture description;
    private int count;
    private final int finalCount;

    public Figure(Texture description, int finalCount) {
        this.description = description;
        this.finalCount = finalCount;
    }

    public void add(Agent agent) {
        if (agent != null) agents.add(agent);
    }

    public void act() {
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            agent.act(Time.FRAME);

            if (agent.getActions().size > 0 && count == finalCount) {
                agent.getActions().first().restart();
            }
        }

        if (count == finalCount) {
            count = 0;
        } else {
            count++;
        }
    }

    public void draw(Batch batch) {
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            agent.draw(batch);
        }
        if (description != null) batch.draw(description, 0, 100);
    }

    public void setDescription(Texture description) {
        this.description = description;
    }
}
