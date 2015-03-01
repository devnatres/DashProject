package com.devnatres.dashproject.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.devnatres.dashproject.DashGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = DashGame.TITLE;
        config.width = DashGame.INITIAL_SCREEN_WIDTH;
        config.height = DashGame.INITIAL_SCREEN_HEIGHT;

		new LwjglApplication(DashGame.newInstance(), config);
	}
}
