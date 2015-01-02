package com.devnatres.dashproject.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.Parameters;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Parameters.TITLE;
        config.width = Parameters.INITIAL_SCREEN_WIDTH;
        config.height = Parameters.INITIAL_SCREEN_HEIGHT;

		new LwjglApplication(new DashGame(), config);
	}
}
