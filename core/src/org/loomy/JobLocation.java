package org.loomy;

import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

public class JobLocation
{
    private Crewman crewman;
    private Job job;
    private Vector2 position;

    public JobLocation(float x, float y)
    {
        this.position = new Vector2(x, y);
    }

    public boolean isAvailable() { return crewman == null; }

    public void update(float delta) {}

    public Job getJob() { return job; }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
