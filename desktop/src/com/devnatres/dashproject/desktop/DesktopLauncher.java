package com.devnatres.dashproject.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.devnatres.dashproject.DashGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = DashGame.TITLE;
        config.width = DashGame.SCREEN_WIDTH;
        config.height = DashGame.SCREEN_HEIGHT;
		config.addIcon("symbols/desktop_icon.png", Files.FileType.Internal);

		new LwjglApplication(DashGame.newInstance(), config);
	}
}
