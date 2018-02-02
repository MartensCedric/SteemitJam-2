package org.loomy;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class JobLocation extends Actor
{
    private Crewman crewman;
    private Job job;

    public JobLocation(float x, float y)
    {
        setX(x);
        setY(y);
    }

    public boolean isAvailable() { return crewman != null; }
}
