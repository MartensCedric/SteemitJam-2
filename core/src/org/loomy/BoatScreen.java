package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class BoatScreen extends StageScreen{

    private GameManager gameManager;
    private AssetManager assetManager;
    private Batch batch;

    private List<JobLocation> jobLocations;

    public BoatScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        this.assetManager = gameManager.assetManager;
        this.batch = new SpriteBatch();
        this.jobLocations = new ArrayList<>();

        JobLocation jobLookout = new JobLocation(0, 0);
        JobLocation jobCannonRight = new JobLocation(110, -100);
        JobLocation jobCannonLeft = new JobLocation(-110, -100);

        jobLocations.add(jobLookout);
        jobLocations.add(jobCannonRight);
        jobLocations.add(jobCannonLeft);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();
        Texture txtBoat = assetManager.get("boat.png", Texture.class);
        batch.draw(txtBoat, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);

        Texture txtJobLocation = assetManager.get("job-location.png", Texture.class);
        for(JobLocation jl : jobLocations)
        {
            batch.draw(txtJobLocation, jl.getX(), jl.getY());
        }
        batch.end();
        super.render(delta);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
