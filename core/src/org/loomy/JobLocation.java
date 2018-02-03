package org.loomy;

import com.badlogic.gdx.math.Vector2;
import org.loomy.job.Job;

public class JobLocation
{
    private Crewman crewman;
    private Job job;
    private Vector2 position;

    public JobLocation(float x, float y, Job job)
    {
        this.position = new Vector2(x, y);
        this.job = job;
    }

    public void assign(Crewman crewman)
    {
        this.crewman = crewman;
    }

    public void removeCrewman() {
        this.crewman = null;
    }

    public boolean isAvailable() { return crewman == null; }

    public void update(float delta) {}

    public Job getJob() { return job; }

    public Crewman getCrewman() { return crewman; }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
