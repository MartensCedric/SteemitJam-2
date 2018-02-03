package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class JobManager
{
    private List<Crewman> crewmen;
    private List<JobLocation> jobLocations;
    private Crewman selectedCrewman;

    private static final int CREWMAN_SIZE = 64;
    private static final int JOB_LOCATION_SIZE = 48;

    public JobManager()
    {
        this.crewmen = new ArrayList<>();
        this.jobLocations = new ArrayList<>();

        JobLocation jobLookout = new JobLocation(0, 0);
        JobLocation jobCannonRight = new JobLocation(110, -100);
        JobLocation jobCannonLeft = new JobLocation(-110, -100);
        JobLocation jobAmmoLeft = new JobLocation(-110, -160);
        JobLocation jobSteering = new JobLocation(0, -275);

        jobLocations.add(jobLookout);
        jobLocations.add(jobCannonRight);
        jobLocations.add(jobCannonLeft);
        jobLocations.add(jobAmmoLeft);
        jobLocations.add(jobSteering);

        crewmen.add(new Crewman(0, -300));
    }

    public void assignJob(Crewman crewman, JobLocation jobLocation)
    {

    }

    public boolean canDoJob(Crewman crewman, JobLocation jobLocation)
    {
        if(crewman.hasJob())
            return false;

        if(!jobLocation.isAvailable())
            return false;

        if(jobLocation.getJob().requiresEmptyHands() && !crewman.isEmptyHanded())
            return false;

        return jobLocation.getJob().requiresItem() == crewman.getItem();
    }

    public void update(float delta)
    {

    }

    public List<Crewman> getCrewmen() { return crewmen; }
    public List<JobLocation> getJobLocations() { return jobLocations; }
    public Crewman getSelectedCrewman() { return selectedCrewman; }

    public boolean processClick(float x, float y, int button)
    {
        System.out.println("Click at " + x + " " + y);
        if(button == Input.Buttons.LEFT)
        {
            if(selectedCrewman == null)
            {
                for(Crewman c : crewmen)
                {
                    float cx = c.getX();
                    float cy = c.getY();
                    float size = CREWMAN_SIZE;

                    if(MathUtil.isInside(x, y,
                    cx - size/2, cy - size/2,
                    cx + size/2, cy + size/2))
                    {
                        selectedCrewman = c;
                        System.out.println("Selected crew man!");
                        return true;
                    }
                }
            }
        }else if(button == Input.Buttons.RIGHT)
        {

        }

        return false;
    }
}
