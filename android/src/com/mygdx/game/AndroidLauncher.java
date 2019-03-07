package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    ConnectingService connectingService;
    String test = "test";
	@Override
	protected void onCreate (Bundle savedInstanceState) {
        JDCEGame.setPlatformResolver(new AndroidResolver());
		super.onCreate(savedInstanceState);
        System.out.println("edes Android launcher toimii");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new JDCEGame(), config);

		connectingService= new ConnectingService();
        Intent intent = new Intent(this,ConnectingService.class);
        startService(intent);

	}
}
