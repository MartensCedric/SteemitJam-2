package org.loomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import org.loomy.job.CannonAmmoJob;
import org.loomy.job.CannonRamJob;

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

        jobLocations.add(new JobLocation(0, 0, new CannonRamJob()));
        jobLocations.add(new JobLocation(110, -100, new CannonRamJob()));
        jobLocations.add(new JobLocation(-110, -100, new CannonRamJob()));
        jobLocations.add(new JobLocation(-110, -160, new CannonAmmoJob()));
        jobLocations.add(new JobLocation(0, -275, new CannonRamJob()));

        crewmen.add(new Crewman(0, -300));
    }

    public void assignJob(Crewman crewman, JobLocation jobLocation)
    {
        jobLocation.assign(crewman);
    }

    public boolean canDoJob(Crewman crewman, JobLocation jobLocation)
    {
        if(hasJob(crewman))
            return false;

        if(!jobLocation.isAvailable())
            return false;

        if(jobLocation.getJob().requiresEmptyHands() && !crewman.isEmptyHanded())
            return false;

        return jobLocation.getJob().requiresItem() == crewman.getItem();
    }

    public void update(float delta)
    {
        for(Crewman c : crewmen)
            c.update(delta);

        for(JobLocation jl : jobLocations)
            jl.update(delta);
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
            if(selectedCrewman != null)
            {
                for(JobLocation jl : jobLocations)
                {
                    float jx = jl.getX();
                    float jy = jl.getY();
                    float size = JOB_LOCATION_SIZE;

                    if(MathUtil.isInside(x, y,
                            jx - size/2, jy - size/2,
                            jx + size/2, jy + size/2))
                    {

                        if(canDoJob(selectedCrewman, jl))
                        {
                            assignJob(selectedCrewman, jl);
                            System.out.println("Crewman now has a job!");
                        }else{
                            System.out.println("Failed to assign job!");
                        }

                        selectedCrewman = null;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hasJob(Crewman crewman)
    {
        for(JobLocation jl : jobLocations)
            if(jl.getCrewman() == crewman)
                return true;

        return false;
    }
}
