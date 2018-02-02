package org.loomy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class SteemitJam extends Game {

	private GameManager gameManager;

	@Override
	public void create () {
		this.gameManager = new GameManager();
		AssetManager assetManager = new AssetManager();
		assetManager.load("job-location.png", Texture.class);
		assetManager.load("boat.png", Texture.class);
		assetManager.load("crewman.png", Texture.class);
		assetManager.finishLoading();
		this.gameManager.assetManager = assetManager;
		setScreen(new BoatScreen(gameManager));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
