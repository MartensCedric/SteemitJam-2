package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class GameManager
{
    public final static int WIDTH = 700;
    public final static int HEIGHT = 700;
    private Skin skin;

    public AssetManager assetManager;
    public static HashMap<String, Sound> soundMap = new HashMap<>();

    private static Skin defaultSkin;
    private static BitmapFont font;

    public static Skin getDefaultSkin()
    {
        if(defaultSkin == null)
        {
            defaultSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        }
        return defaultSkin;
    }

    public static BitmapFont getFont()
    {
        if(font == null)
            font = new BitmapFont();

        return font;
    }
}
