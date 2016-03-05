package com.subzero.picrossquest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.subzero.picrossquest.PicrossQuest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Picross Quest";
		config.width = 854;
		config.height = 480;
		new LwjglApplication(new PicrossQuest(), config);
	}
}
