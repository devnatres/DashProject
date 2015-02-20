package com.devnatres.dashproject.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.LaunchParameters;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = LaunchParameters.TITLE;
        config.width = LaunchParameters.INITIAL_SCREEN_WIDTH;
        config.height = LaunchParameters.INITIAL_SCREEN_HEIGHT;

		new LwjglApplication(new DashGame(), config);
	}
}
