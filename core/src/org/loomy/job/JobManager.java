package org.loomy.job;

import com.badlogic.gdx.Input;
import org.loomy.BoatScreen;
import org.loomy.Crewman;
import org.loomy.MathUtil;

import java.util.ArrayList;
import java.util.List;

import static org.loomy.BoatScreen.boatX;
import static org.loomy.BoatScreen.crewmanOnMast;
import static org.loomy.BoatScreen.crewmanOnWheel;

public class JobManager {
    private List<Crewman> crewmen;
    private List<JobLocation> jobLocations;
    private Crewman selectedCrewman;

    private static final int CREWMAN_SIZE = 64;
    private static final int JOB_LOCATION_SIZE = 48;

    public JobManager() {
        this.crewmen = new ArrayList<>();
        this.jobLocations = new ArrayList<>();

        jobLocations.add(new JobLocation(0, 0, new ClimbMastJob()));
        jobLocations.add(new JobLocation(0, -150, new Job[]{new TakeRammerJob(), new DepositRammerJob()}));
        jobLocations.add(new JobLocation(110, -100, new Job[]{new CannonFillJob(), new CannonRamJob(), new CannonFireJob()}));
        jobLocations.add(new JobLocation(-110, -100, new Job[]{new CannonFillJob(), new CannonRamJob(), new CannonFireJob()}));
        jobLocations.add(new JobLocation(-110, 70, new CannonAmmoJob()));
        jobLocations.add(new JobLocation(110, 70, new CannonAmmoJob()));
        jobLocations.add(new JobLocation(0, -275, new SteerShipJob()));

        crewmen.add(new Crewman(0, -300));
        crewmen.add(new Crewman(20, 130));
    }

    public void assignJob(Crewman crewman, JobLocation jobLocation) {

        if(crewman.getCurrentJob() != null)
        {
            crewman.getCurrentJob().removeCrewman();
            crewman.getCurrentJob().getJob().resetJob();
        }

        jobLocation.assign(crewman);
    }

    public boolean canDoJob(Crewman crewman, JobLocation jobLocation) {

        if (!jobLocation.isAvailable())
            return false;

        if (jobLocation.getJob().requiresEmptyHands() && !crewman.isEmptyHanded())
            return false;

        return jobLocation.getJob().requiresItem() == crewman.getItem();
    }

    public void update(float delta) {
        for (Crewman c : crewmen)
            c.update(delta);

        for (JobLocation jl : jobLocations)
            jl.update(delta);
    }

    public List<Crewman> getCrewmen() {
        return crewmen;
    }

    public List<JobLocation> getJobLocations() {
        return jobLocations;
    }

    public Crewman getSelectedCrewman() {
        return selectedCrewman;
    }

    public boolean processClick(float x, float y, int button) {
        System.out.println("Click at " + x + " " + y);
        x-= boatX;
        if (button == Input.Buttons.LEFT) {

            for (Crewman c : crewmen) {
                float cx = c == crewmanOnMast ? 0 : c.getX();
                float cy = c == crewmanOnMast ? 50 : c.getY();
                float size = CREWMAN_SIZE;

                if((c.getCurrentJob() == null && !c.hasTarget()) ||
                        (c.getCurrentJob() != null && c.getCurrentJob().getJob().getJobState() ==
                        Job.JobState.FINISHED))
                {
                    if (MathUtil.isInside(x, y,
                            cx - size / 2, cy - size / 2,
                            cx + size / 2, cy + size / 2)) {
                        selectedCrewman = c;
                        System.out.println("Selected crew man!");
                        BoatScreen.speak();
                        return true;
                    }
                }
            }

        } else if (button == Input.Buttons.RIGHT) {
            if (selectedCrewman != null) {
                for (JobLocation jl : jobLocations) {
                    float jx = jl.getX();
                    float jy = jl.getY();
                    float size = JOB_LOCATION_SIZE;

                    if (MathUtil.isInside(x, y,
                            jx - size / 2, jy - size / 2,
                            jx + size / 2, jy + size / 2)) {

                        if (canDoJob(selectedCrewman, jl)) {

                            jobLocations.forEach(j -> {
                                if(!j.isAvailable())
                                {
                                    if(j.getCrewman().getX() == selectedCrewman.getX() &&
                                            j.getCrewman().getY() == selectedCrewman.getY())
                                    {
                                        j.nextJob();
                                    }
                                }
                            });

                            if(selectedCrewman == crewmanOnMast)
                            {
                                crewmanOnMast = null;
                                BoatScreen.spyglassMode = false;
                            }else if(selectedCrewman == crewmanOnWheel)
                            {
                                crewmanOnWheel = null;
                            }

                            assignJob(selectedCrewman, jl);
                            System.out.println("Crewman now has a job!");
                            BoatScreen.speak();
                            selectedCrewman.setTarget(jl);
                        } else {
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
}
