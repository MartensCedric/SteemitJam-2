package org.loomy.job;

import com.badlogic.gdx.math.Vector2;
import org.loomy.BoatScreen;
import org.loomy.Cannonball;
import org.loomy.Crewman;

public class JobLocation
{
    private Crewman crewman;
    private Vector2 position;
    private Job[] jobs;
    private int jobIndex = 0;

    public void nextJob()
    {
        if(jobs.length > 1)
        {
            jobIndex++;
            jobIndex = jobIndex % jobs.length;
        }
    }

    public JobLocation(float x, float y, Job j)
    {
        this(x, y, new Job[]{j});
    }

    public JobLocation(float x, float y, Job[] jobs)
    {
        this.position = new Vector2(x, y);
        this.jobs = jobs;
    }

    public void assign(Crewman crewman)
    {
        this.crewman = crewman;
        getJob().setJobFinished(() -> {
            crewman.setItem(getJob().rewardedItem());
            if(getJob() instanceof CannonFireJob)
            {
                BoatScreen.cannonballs.add(new Cannonball(position.cpy()));

                if(position.x < 0)
                    BoatScreen.cannonShot_left.start();
                else BoatScreen.cannonShot_right.start();

                BoatScreen.shakeLeft = BoatScreen.CANNON_SHAKE;
            }else if(getJob() instanceof ClimbMastJob)
            {
                BoatScreen.crewmanOnMast = crewman;
            }
        });
    }

    public void removeCrewman() {
        this.crewman = null;
    }

    public boolean isAvailable() { return crewman == null; }

    public void update(float delta)
    {
        getJob().updateJob(delta);
    }

    public Job getJob() { return jobs[jobIndex]; }

    public Job.JobState getJobState()
    {
        if(crewman != null && (crewman.getX() != getX()
                || crewman.getY() != getY()))
        {
            return Job.JobState.RESERVED;
        }else{
            return getJob().getJobState();
        }
    }

    public Crewman getCrewman() { return crewman; }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
