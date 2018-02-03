package org.loomy;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static org.loomy.GameManager.HEIGHT;
import static org.loomy.GameManager.WIDTH;

/**
 * Created by 1544256 on 2017-04-26.
 */
public abstract class StageScreen implements Screen
{
    private Stage stage;
    private InputMultiplexer inputMultiplexer;

    public StageScreen() {
        this.inputMultiplexer = new InputMultiplexer();
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        inputMultiplexer.addProcessor(getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta)
    {
        stage.act();
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public OrthographicCamera getCamera() {
        return (OrthographicCamera) stage.getCamera();
    }

    public InputMultiplexer getInputMultiplexer() { return inputMultiplexer; }
}
