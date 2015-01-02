package com.devnatres.dashproject.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.Parameters;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Parameters.INITIAL_SCREEN_WIDTH, Parameters.INITIAL_SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new DashGame();
        }
}