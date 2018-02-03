package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import static org.loomy.GameManager.HEIGHT;
import static org.loomy.GameManager.WIDTH;

public class BoatScreen extends StageScreen{

    private GameManager gameManager;
    private AssetManager assetManager;
    private Batch batch;
    private OrthographicCamera worldCamera;

    private JobManager jobManager;

    public BoatScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        this.assetManager = gameManager.assetManager;
        this.worldCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.batch = new SpriteBatch();
        this.jobManager = new JobManager();


    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        Texture txtBoat = assetManager.get("boat.png", Texture.class);
        batch.draw(txtBoat, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);

        Texture txtJobLocation = assetManager.get("job-location.png", Texture.class);
        for(JobLocation jl : jobManager.getJobLocations())
        {
            batch.draw(txtJobLocation, jl.getX() - txtJobLocation.getWidth()/2,
                    jl.getY() - txtJobLocation.getHeight()/2);
        }

        Crewman selectedCrewman = jobManager.getSelectedCrewman();
        if(selectedCrewman != null)
        {
            Texture txtSelectedCrewman = assetManager.get("selected-crewman.png", Texture.class);
            batch.draw(txtSelectedCrewman, selectedCrewman.getX() - txtSelectedCrewman.getWidth()/2,
                    selectedCrewman.getY() - txtSelectedCrewman.getHeight()/2);
        }

        Texture txtCrewman = assetManager.get("crewman.png", Texture.class);

        TextureRegion trCrewman = new TextureRegion(txtCrewman);

        for(Crewman c : jobManager.getCrewmen())
            batch.draw(trCrewman, c.getX() - trCrewman.getRegionWidth()/2, c.getY() - trCrewman.getRegionHeight()/2,
                    trCrewman.getRegionWidth()/2, trCrewman.getRegionHeight()/2,
                    txtCrewman.getWidth(), txtCrewman.getHeight(), 1, 1, c.getRotation());

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
