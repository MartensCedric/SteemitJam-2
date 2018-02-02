package org.loomy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class GameManager
{
    public final static int WIDTH = 700;
    public final static int HEIGHT = 700;
    public AssetManager assetManager;
    public HashMap<String, Sound> soundMap = new HashMap<>();
}
