package org.loomy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.loomy.SteemitJam;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 700;
		config.height = 700;
		config.resizable = false;
		config.title = "Sailing on the Seven Seas";
		new LwjglApplication(new SteemitJam(), config);
	}
}
