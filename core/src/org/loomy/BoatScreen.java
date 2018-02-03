package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.loomy.creature.Kraken;
import org.loomy.creature.SeaCreature;
import org.loomy.creature.SeaSerpent;
import org.loomy.job.Job;
import org.loomy.job.JobLocation;
import org.loomy.job.JobManager;

import java.util.ArrayList;
import java.util.List;

import static org.loomy.GameManager.HEIGHT;
import static org.loomy.GameManager.WIDTH;
import static org.loomy.GameManager.getDefaultSkin;

public class BoatScreen extends StageScreen{

    private GameManager gameManager;
    private AssetManager assetManager;
    private Batch batch;
    private OrthographicCamera worldCamera;
    private ShapeRenderer shapeRenderer;
    private boolean spyglassMode = true;
    private TextButton txtToggleSpyglass;

    public static List<Cannonball> cannonballs; //game jam !! hehe
    public static final float CANNONBALL_SIZE = 32;
    private static final float CREATURE_SIZE_MUL = 15;
    private List<SeaCreature> seaCreatures;
    private int totalCreatures = 0;
    private float deltaSinceStart = 0;

    private JobManager jobManager;

    public BoatScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        this.assetManager = gameManager.assetManager;
        this.worldCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.cannonballs = new ArrayList<>();
        this.seaCreatures = new ArrayList<>();
        this.batch = new SpriteBatch();
        this.jobManager = new JobManager();
        getInputMultiplexer().addProcessor(inputProcessor);
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
        setSpyglassMode(false);
        this.txtToggleSpyglass = new TextButton("Toggle Spyglass Mode", getDefaultSkin());
        this.txtToggleSpyglass.setX(WIDTH - 200);
        this.txtToggleSpyglass.setY(20);
        getStage().addActor(txtToggleSpyglass);
        this.txtToggleSpyglass.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSpyglassMode(!spyglassMode);
            }
        });
    }

    public void setSpyglassMode(boolean spyglassMode)
    {
        this.spyglassMode = spyglassMode;
        worldCamera.zoom = spyglassMode ? 7.5f : 1;
        worldCamera.update();
    }

    @Override
    public void render(float delta)
    {
        deltaSinceStart += delta;
        Gdx.gl.glClearColor(0f, 149f/255f, 233f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCreatureWave();
        jobManager.update(delta);
        for(int i = 0; i < cannonballs.size(); i++)
        {
            Cannonball cannonball = cannonballs.get(i);
            cannonball.update(delta);
            if(cannonball.getPosition().x > 3_500 || cannonball.getPosition().x < -3_500)
            {
                cannonballs.remove(i);
                i--;
            }
        }

        for(SeaCreature s : seaCreatures)
        {
            s.update(delta);
        }

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        Texture txtBoat = assetManager.get("boat.png", Texture.class);
        batch.draw(txtBoat, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);

        Texture txtCannonball = assetManager.get("cannon-ball.png", Texture.class);
        for(Cannonball c : cannonballs)
            batch.draw(txtCannonball, c.getPosition().x - txtCannonball.getWidth()/2,
                    c.getPosition().y - txtCannonball.getHeight()/2);


        Texture txtKraken = assetManager.get("kraken.png", Texture.class);
        Texture txtSeaSerpent = assetManager.get("sea-serpent.png", Texture.class);

        for(SeaCreature s : seaCreatures)
        {
            if(s instanceof Kraken)
            {
                batch.draw(txtKraken, s.getX() - txtKraken.getWidth() * CREATURE_SIZE_MUL/2,
                        s.getY() - txtKraken.getHeight() * CREATURE_SIZE_MUL/2,
                        txtKraken.getWidth() * CREATURE_SIZE_MUL, txtKraken.getHeight() * CREATURE_SIZE_MUL);
            }else if(s instanceof SeaSerpent)
            {
                batch.draw(txtSeaSerpent, s.getX() - txtSeaSerpent.getWidth() * CREATURE_SIZE_MUL/2
                        , s.getY() - txtSeaSerpent.getHeight()* CREATURE_SIZE_MUL/2,
                        txtSeaSerpent.getWidth() * CREATURE_SIZE_MUL, txtSeaSerpent.getHeight() * CREATURE_SIZE_MUL);
            }
        }

        Texture txtWheel = assetManager.get("wheel.png", Texture.class);
        batch.draw(txtWheel, 0 - txtWheel.getWidth()/2, -225 - txtWheel.getHeight()/2);

        Texture txtJobLocation = assetManager.get("job-location.png", Texture.class);
        Texture txtJobLocationProgress = assetManager.get("job-location-progress.png", Texture.class);
        Texture txtJobLocationFinished = assetManager.get("job-location-finished.png", Texture.class);
        Texture txtJobLocationReserved = assetManager.get("job-location-reserved.png", Texture.class);

        for(JobLocation jl : jobManager.getJobLocations())
        {
            Texture txtJobState = null;
            switch (jl.getJobState()) {
                case OPEN:
                    txtJobState = txtJobLocation;
                    break;
                case PROGRESS:
                    txtJobState = txtJobLocationProgress;
                    break;
                case FINISHED:
                    txtJobState = txtJobLocationFinished;
                    break;
                case RESERVED:
                    txtJobState = txtJobLocationReserved;
                    break;
            }

            batch.draw(txtJobState, jl.getX() - txtJobState.getWidth()/2,
                    jl.getY() - txtJobState.getHeight()/2);
        }

        Crewman selectedCrewman = jobManager.getSelectedCrewman();
        if(selectedCrewman != null)
        {
            Texture txtSelectedCrewman = assetManager.get("selected-crewman.png", Texture.class);
            batch.draw(txtSelectedCrewman, selectedCrewman.getX() - txtSelectedCrewman.getWidth()/2,
                    selectedCrewman.getY() - txtSelectedCrewman.getHeight()/2);
        }

        Texture txtCannon = assetManager.get("cannon.png", Texture.class);
        batch.draw(txtCannon, -152 - txtCannon.getWidth()/2, -102 - txtCannon.getHeight()/2);
        batch.draw(txtCannon, 120, -118, txtCannon.getWidth()/2, txtCannon.getHeight()/2
                , txtCannon.getWidth(), txtCannon.getHeight(), 1, 1, 0, 0, 0,
                txtCannon.getWidth(), txtCannon.getHeight(), true, false);

        Texture txtCrewman = assetManager.get("crewman.png", Texture.class);
        Texture txtCrewmanCannonball = assetManager.get("crewman-cannonballs.png", Texture.class);
        Texture txtCrewmanRammer = assetManager.get("crewman-rammer.png", Texture.class);


        for(Crewman c : jobManager.getCrewmen())
        {
            TextureRegion txtitem = null;
            switch (c.getItem()) {
                case NO_ITEM:
                    txtitem = new TextureRegion(txtCrewman);
                    break;
                case CANNONBALL:
                    txtitem = new TextureRegion(txtCrewmanCannonball);
                    break;
                case RAMMER:
                    txtitem = new TextureRegion(txtCrewmanRammer);
                    break;
            }

            batch.draw(txtitem, c.getX() - txtitem.getRegionWidth()/2, c.getY() - txtitem.getRegionHeight()/2,
                    txtitem.getRegionWidth()/2, txtitem.getRegionHeight()/2,
                    txtCrewman.getWidth(), txtCrewman.getHeight(), 1, 1, c.getDirection().angle());
        }


        batch.end();
        this.shapeRenderer.setProjectionMatrix(worldCamera.combined);
        this.shapeRenderer.begin();
        Color loadBg = new Color(58f/255f, 68f/255f, 102f/255f, 1f);
        Color loadBar = new Color(99f/255f, 199f/255f, 77f/255f, 1f);
        for(JobLocation jl : jobManager.getJobLocations())
        {
            if(jl.getJobState().equals(Job.JobState.PROGRESS))
            {
                float progress = jl.getJob().getProgress();
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(loadBg);
                shapeRenderer.rect(jl.getX() - 32, jl.getY() + 32, 64,12);

                shapeRenderer.setColor(loadBar);
                shapeRenderer.rect(jl.getX() - 32, jl.getY() + 32, 64 - 64 * progress, 12);
            }
        }

        this.shapeRenderer.end();
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);
        Texture txtMast = assetManager.get("mast.png", Texture.class);
        batch.draw(txtMast, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);
        batch.end();
        super.render(delta);
    }

    public void updateCreatureWave()
    {
        int creatureLimit = (int) (1 + (Math.pow(deltaSinceStart, 1.1)/20));
        if(totalCreatures < creatureLimit)
        {
            boolean kraken = MathUtils.randomBoolean(0.25f);
            seaCreatures.add(kraken ? new Kraken() : new SeaSerpent());
            totalCreatures++;
        }
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

    private InputProcessor inputProcessor = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {

            Viewport viewPort = getStage().getViewport();
            Vector3 pos = worldCamera.unproject(new Vector3(screenX,screenY,0),
                    viewPort.getScreenX(), viewPort.getScreenY(),
                    viewPort.getScreenWidth(), viewPort.getScreenHeight());
            return jobManager.processClick(pos.x, pos.y, button);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    };
}
