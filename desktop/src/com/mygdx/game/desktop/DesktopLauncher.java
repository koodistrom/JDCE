package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.JDCEGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

        JDCEGame.setPlatformResolver(new DesktopResolver());
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new JDCEGame(), config);
		config.width = 1300;
		config.height = 800;
	}
}
