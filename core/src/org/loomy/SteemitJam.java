package org.loomy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class SteemitJam extends Game {

	private GameManager gameManager;

	@Override
	public void create () {
		this.gameManager = new GameManager();
		AssetManager assetManager = new AssetManager();

		assetManager.load("job-location.png", Texture.class);
		assetManager.load("job-location-finished.png", Texture.class);
		assetManager.load("job-location-progress.png", Texture.class);
		assetManager.load("job-location-reserved.png", Texture.class);
		assetManager.load("boat.png", Texture.class);
		assetManager.load("crewman.png", Texture.class);
		assetManager.load("crewman-cannonballs.png", Texture.class);
		assetManager.load("crewman-rammer.png", Texture.class);
		assetManager.load("crewman-spyglass.png", Texture.class);
		assetManager.load("selected-crewman.png", Texture.class);
		assetManager.load("cannon-ball.png", Texture.class);
		assetManager.load("cannon.png", Texture.class);
		assetManager.load("mast.png", Texture.class);
		assetManager.load("wheel.png", Texture.class);
		assetManager.load("kraken.png", Texture.class);
		assetManager.load("kraken_2.png", Texture.class);
		assetManager.load("sea-serpent.png", Texture.class);
		assetManager.load("fog.png", Texture.class);
		assetManager.load("water.png", Texture.class);

		assetManager.load("sevenseasailing.wav", Music.class);
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
