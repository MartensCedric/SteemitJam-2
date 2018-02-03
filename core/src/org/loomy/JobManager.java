package org.loomy;

import java.util.ArrayList;
import java.util.List;

public class JobManager
{
    private List<Crewman> crewmen;
    private List<JobLocation> jobLocations;
    private Crewman selectedCrewman;

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
        this.selectedCrewman = crewmen.get(0);
    }

    public void update(float delta)
    {

    }

    public List<Crewman> getCrewmen() { return crewmen; }
    public List<JobLocation> getJobLocations() { return jobLocations; }
    public Crewman getSelectedCrewman() { return selectedCrewman; }
}
