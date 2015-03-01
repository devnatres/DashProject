package com.devnatres.dashproject.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.devnatres.dashproject.DashGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = DashGame.USE_ACCELEROMETER;
        config.useCompass = DashGame.USE_COMPASS;

		initialize(DashGame.newInstance(), config);
	}
}
