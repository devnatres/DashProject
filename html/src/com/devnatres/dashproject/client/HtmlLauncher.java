package com.devnatres.dashproject.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.LaunchParameters;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(LaunchParameters.INITIAL_SCREEN_WIDTH, LaunchParameters.INITIAL_SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new DashGame();
        }
}