package com.devnatres.dashproject.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.devnatres.dashproject.agents.Agent;
import com.devnatres.dashproject.gameconstants.Time;

import java.util.ArrayList;

/**
 * Created by DevNatres on 17/02/2015.
 */
public class Figure {
    private final ArrayList<Agent> agents = new ArrayList<Agent>();
    private final Texture description;

    public Figure(Texture description) {
        this.description = description;
    }

    public void add(Agent agent) {
        if (agent != null) agents.add(agent);
    }

    public void act() {
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            agent.act(Time.FRAME);
        }
    }

    public void draw(Batch batch) {
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            agent.draw(batch);
        }
        if (description != null) batch.draw(description, 0, 0);
    }
}
