package com.devnatres.dashproject.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.gameconstants.Parameters;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = Parameters.USE_ACCELEROMETER;
        config.useCompass = Parameters.USE_COMPASS;

		initialize(new DashGame(), config);
	}
}
