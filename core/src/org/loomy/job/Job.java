package org.loomy.job;

import org.loomy.Item;

public abstract class Job
{
    protected boolean inProgress;
    protected float workLeft;
    protected JobState jobState = JobState.OPEN;
    protected Runnable jobFinished = null;

    public Job()
    {
        resetJob();
    }

    public void resetJob() {
        inProgress = false;
        workLeft = getTotalWork();
        jobState = JobState.OPEN;
    }
    public void startJob()
    {
        jobState = JobState.PROGRESS;
        inProgress = true;
    }
    public void updateJob(float delta)
    {
        if(inProgress)
            workLeft -= delta;

        if(workLeft <= 0)

        {   jobState = JobState.FINISHED;
            if(jobFinished != null)
            {
                jobFinished.run();
            }
        }
    }

    public abstract Item requiresItem();
    public abstract boolean requiresEmptyHands();
    public abstract Item rewardedItem();

    public JobState getJobState() { return jobState; }

    public enum JobState{
        OPEN,
        RESERVED,
        PROGRESS,
        FINISHED
    }

    protected abstract float getTotalWork();

    public float getProgress() {
        return workLeft/getTotalWork();
    }
}
