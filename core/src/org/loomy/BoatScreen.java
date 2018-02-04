package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.loomy.creature.Kraken;
import org.loomy.creature.SeaCreature;
import org.loomy.creature.SeaSerpent;
import org.loomy.job.*;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;
import static org.loomy.GameManager.HEIGHT;
import static org.loomy.GameManager.WIDTH;
import static org.loomy.GameManager.getDefaultSkin;

public class BoatScreen extends StageScreen
{
    private GameManager gameManager;
    private AssetManager assetManager;
    private Batch batch;
    private OrthographicCamera worldCamera;
    private ShapeRenderer shapeRenderer;

    private TextButton txtToggleSpyglass;
    private Slider sldSteering;

    private Label lblSurvivalTime;
    private Label lblPort;
    private Label lblStarboard;

    private final float BOAT_SPEED = 30.0f;

    public static List<Cannonball> cannonballs; //game jam !! hehe
    public static Crewman crewmanOnMast = null;
    public static Crewman crewmanOnWheel = null;
    public static boolean spyglassMode = false;

    public static final float CANNONBALL_SIZE = 32;
    private static final float CREATURE_SIZE_MUL = 15;
    public static final float CANNON_SHAKE = 0.5f;
    public static float shakeLeft = 0;
    private float defaultCamX = 0;
    private float defaultCamY = 0;
    public static float boatX;
    public static float steeringValue = 0;

    private ShaderProgram waterShader;
    private ShaderProgram fogShader;
    private ShaderProgram boatShader;

    public static final float BORDER_AT = 2_700;
    public static final float MAST_X = 0;
    public static final float MAST_Y = 50;
    private List<SeaCreature> seaCreatures;
    private int totalCreatures;
    private float deltaSinceStart;
    private final float DEATH_FADE = 6f;
    private float deathFade;

    private boolean death;
    private JobManager jobManager;

    public static ParticleEffect cannonShot_left;
    public static ParticleEffect cannonShot_right;

    private static Sound[] pirateSpeak;

    public BoatScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        init();
    }

    public static void speak()
    {
        pirateSpeak[MathUtils.random(pirateSpeak.length - 1)].play();
    }

    public void screenShake(float delta)
    {
        float currentPower = 500 * worldCamera.zoom * delta;
        float x = (random.nextFloat() - 0.5f) * currentPower;
        float y = (random.nextFloat() - 0.5f) * currentPower;
        worldCamera.translate(-x, -y);
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

        if(deathFade <= 0)
        {
            init();
        }

        Gdx.gl.glClearColor(0f, 149f/255f, 233f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!death)
        {
            txtToggleSpyglass.setVisible(crewmanOnMast != null);

            updateCreatureWave();
            jobManager.update(delta);
            for(int i = 0; i < cannonballs.size(); i++)
            {
                Cannonball cannonball = cannonballs.get(i);
                cannonball.update(delta);
                if(cannonball.getPosition().x > BORDER_AT || cannonball.getPosition().x < -BORDER_AT)
                {
                    cannonballs.remove(i);
                    i--;
                }
            }

            for(int i = 0; i < seaCreatures.size(); i++)
            {
                SeaCreature s = seaCreatures.get(i);
                s.update(delta);

                if(s.isDead())
                {
                    seaCreatures.remove(i);
                    i--;
                }else{
                    if(Math.abs(s.getX()) < 220 + 32 * CREATURE_SIZE_MUL/2.0)
                    {
                        death = true;
                        lblSurvivalTime = new Label(String.format("You survived %.0f seconds sailing in the seven seas", deltaSinceStart), getDefaultSkin());
                        lblSurvivalTime.setX(WIDTH/2 - 200);
                        lblSurvivalTime.setY(HEIGHT/2);
                        lblPort.setVisible(false);
                        lblStarboard.setVisible(false);
                        sldSteering.setVisible(false);
                        txtToggleSpyglass.setVisible(false);
                        getStage().addActor(lblSurvivalTime);
                    }
                }
            }

            worldCamera.position.x = defaultCamX;
            worldCamera.position.y = defaultCamY;

            if(shakeLeft > 0)
            {
                screenShake(delta);
                shakeLeft -= delta;
            }

            worldCamera.update();
        }else{
            deathFade -= delta;
        }

        batch.begin();
        batch.setProjectionMatrix(getCamera().combined);
        batch.setShader(spyglassMode ? fogShader : waterShader);
        Texture txtWaterTile = assetManager.get("water.png", Texture.class);
        batch.draw(txtWaterTile, 0, 0, WIDTH, HEIGHT);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.setShader(boatShader);
        Texture txtBoat = assetManager.get("boat.png", Texture.class);
        batch.draw(txtBoat, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);

        batch.setShader(fogShader);
        Texture txtCannonball = assetManager.get("cannon-ball.png", Texture.class);
        for(Cannonball c : cannonballs)
            batch.draw(txtCannonball, c.getPosition().x - txtCannonball.getWidth()/2,
                    c.getPosition().y - txtCannonball.getHeight()/2);


        batch.setShader(null);
        cannonShot_left.draw(batch, delta);
        cannonShot_right.draw(batch, delta);

        batch.setShader(fogShader);

        Texture txtKraken = assetManager.get("kraken.png", Texture.class);
        Texture txtKraken2 = assetManager.get("kraken_2.png", Texture.class);
        Texture txtSeaSerpent = assetManager.get("sea-serpent.png", Texture.class);

        for(SeaCreature s : seaCreatures)
        {
            if(s instanceof Kraken)
            {
                Texture krakenToDraw = s.getHitpoints() == 2 ? txtKraken : txtKraken2;
                batch.draw(krakenToDraw,
                        -boatX + s.getX() - krakenToDraw.getWidth() * CREATURE_SIZE_MUL/2,
                        s.getY() - krakenToDraw.getHeight() * CREATURE_SIZE_MUL/2,
                        0, 0,
                        krakenToDraw.getWidth() * CREATURE_SIZE_MUL,
                        krakenToDraw.getHeight() * CREATURE_SIZE_MUL, 1, 1, 0, 0, 0,
                        krakenToDraw.getWidth(),
                        krakenToDraw.getHeight(),
                        s.isFacingRight(), false);

            }else if(s instanceof SeaSerpent)
            {
                batch.draw(txtSeaSerpent,
                        -boatX + s.getX() - txtSeaSerpent.getWidth() * CREATURE_SIZE_MUL/2,
                        s.getY() - txtSeaSerpent.getHeight()* CREATURE_SIZE_MUL/2,
                        0, 0,
                        txtSeaSerpent.getWidth() * CREATURE_SIZE_MUL,
                        txtSeaSerpent.getHeight() * CREATURE_SIZE_MUL, 1, 1, 0, 0, 0,
                        txtSeaSerpent.getWidth(), txtSeaSerpent.getHeight(), s.isFacingRight(), false);
            }
        }

        Texture txtWheel = assetManager.get("wheel.png", Texture.class);
        batch.draw(txtWheel, 0 - txtWheel.getWidth()/2, -225 - txtWheel.getHeight()/2);

        batch.setShader(null);
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

            if(jl.getJob() instanceof TakeRammerJob)
            {
                batch.setShader(fogShader);
                Texture txtRammer = assetManager.get("rammer.png", Texture.class);
                batch.draw(txtRammer, jl.getX() - txtRammer.getWidth()/2, jl.getY() - txtRammer.getHeight()/2);
                batch.setShader(null);
            }else if(jl.getJob() instanceof ClimbMastJob)
            {
                if(jl.isAvailable())
                {
                    batch.setShader(fogShader);
                    Texture txtSpyglass = assetManager.get("spyglass.png", Texture.class);
                    batch.draw(txtSpyglass, jl.getX() - txtSpyglass.getWidth()/2, jl.getY() - txtSpyglass.getHeight()/2);
                    batch.setShader(null);
                }
            }else if(jl.getJob() instanceof CannonAmmoJob)
            {
                batch.setShader(fogShader);
                Texture txtAmmo = assetManager.get("ammo.png", Texture.class);
                batch.draw(txtAmmo, jl.getX() - txtAmmo.getWidth()/2, jl.getY() - txtAmmo.getHeight()/2);
                batch.setShader(null);
            }
        }


        Crewman selectedCrewman = jobManager.getSelectedCrewman();
        if(selectedCrewman != null)
        {
            float x = selectedCrewman == crewmanOnMast ? MAST_X : selectedCrewman.getX();
            float y = selectedCrewman == crewmanOnMast ? MAST_Y : selectedCrewman.getY();

            Texture txtSelectedCrewman = assetManager.get("selected-crewman.png", Texture.class);
            batch.draw(txtSelectedCrewman, x - txtSelectedCrewman.getWidth()/2,
                    y - txtSelectedCrewman.getHeight()/2);
        }

        batch.setShader(fogShader);
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
            if(c != crewmanOnMast)
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
        }

        batch.setShader(boatShader);
        Texture txtMast = assetManager.get("mast.png", Texture.class);
        batch.draw(txtMast, -txtBoat.getWidth()/2, -txtBoat.getHeight()/2);
        batch.setShader(fogShader);
        if(crewmanOnMast != null) {
            Texture txtCrewmanOnMast = assetManager.get("crewman-spyglass.png", Texture.class);
            TextureRegion tr = new TextureRegion(txtCrewmanOnMast);
            batch.draw(tr, MAST_X - tr.getRegionWidth() / 2,
                    MAST_Y - tr.getRegionHeight() / 2,
                    tr.getRegionWidth() / 2, tr.getRegionHeight() / 2,
                    txtCrewman.getWidth(), txtCrewman.getHeight(), 1, 1, 90);
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
        if(death)
        {
            batch.begin();
            batch.setProjectionMatrix(getCamera().combined);
            batch.setShader(null);
            Texture txtDeath = assetManager.get("death-fade.png", Texture.class);
            batch.setColor(1, 1, 1, 1 - (deathFade/DEATH_FADE));
            batch.draw(txtDeath, 0, 0);
            batch.end();
        }

        lblStarboard.setVisible(crewmanOnWheel != null);
        lblPort.setVisible(crewmanOnWheel != null);
        sldSteering.setVisible(crewmanOnWheel != null);

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

    public void init()
    {
        getStage().clear();
        crewmanOnMast = null;
        crewmanOnWheel = null;
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
        this.txtToggleSpyglass.setX(WIDTH - 250);
        this.txtToggleSpyglass.setY(20);
        getStage().addActor(txtToggleSpyglass);
        this.txtToggleSpyglass.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSpyglassMode(!spyglassMode);
            }
        });

        this.defaultCamX = worldCamera.position.x;
        this.defaultCamY = worldCamera.position.y;

        String vertexShader = Gdx.files.internal("shader.vs").readString();
        String fogShaderFS = Gdx.files.internal("fog.fs").readString();
        String boatShaderFS = Gdx.files.internal("boat.fs").readString();
        String waterShaderFS = Gdx.files.internal("water.fs").readString();

        fogShader = new ShaderProgram(vertexShader, fogShaderFS);
        if (!fogShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + fogShader.getLog());

        boatShader = new ShaderProgram(vertexShader, boatShaderFS);
        if (!boatShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + boatShader.getLog());

        waterShader = new ShaderProgram(vertexShader, waterShaderFS);
        if(!waterShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + waterShader.getLog());

        Music music = assetManager.get("sevenseasailing.wav", Music.class);
        music.setLooping(true);
        music.setVolume(0.02f);
        music.play();

        this.cannonShot_left = new ParticleEffect();
        this.cannonShot_left.load(Gdx.files.internal("cannon_left.pr"), Gdx.files.internal(""));

        this.cannonShot_right = new ParticleEffect();
        this.cannonShot_right.load(Gdx.files.internal("cannon_right.pr"), Gdx.files.internal(""));

        cannonShot_left.setPosition(-200, -100);
        cannonShot_right.setPosition(200, -100);

        this.pirateSpeak = new Sound[]{
            assetManager.get("sounds/ahoy.wav", Sound.class),
            assetManager.get("sounds/arghh.wav", Sound.class),
            assetManager.get("sounds/ay.wav", Sound.class),
            assetManager.get("sounds/booty.wav", Sound.class),
            assetManager.get("sounds/oi.wav", Sound.class),
            assetManager.get("sounds/rum.wav", Sound.class),
            assetManager.get("sounds/shark_bait.wav", Sound.class),
            assetManager.get("sounds/ye.wav", Sound.class)
        };

        deathFade = DEATH_FADE;
        totalCreatures = 0;
        deltaSinceStart = 0;
        death = false;
        boatX = 0;
        this.sldSteering = new Slider(0, 2, 1f, false, getDefaultSkin());
        this.sldSteering.setValue(1f);
        this.sldSteering.setWidth(220);
        this.sldSteering.setX(50);
        this.sldSteering.setY(50);
        getStage().addActor(sldSteering);

        this.lblPort = new Label("Port", getDefaultSkin());
        this.lblPort.setX(sldSteering.getX() - 20);
        this.lblPort.setY(sldSteering.getY() + 35);
        getStage().addActor(lblPort);

        this.lblStarboard = new Label("Starboard", getDefaultSkin());
        this.lblStarboard.setX(sldSteering.getX() + sldSteering.getWidth() - 50);
        this.lblStarboard.setY(sldSteering.getY() + 35);
        getStage().addActor(lblStarboard);

        steeringValue = 0;
    }
}
