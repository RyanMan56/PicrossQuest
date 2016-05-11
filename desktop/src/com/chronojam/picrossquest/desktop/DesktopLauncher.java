package com.chronojam.picrossquest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chronojam.picrossquest.PicrossQuest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Picross Quest";
		config.width = 854;
		config.height = 480;
		new LwjglApplication(new PicrossQuest(), config);
	}
}
