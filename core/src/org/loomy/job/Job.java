package org.loomy.job;

import org.loomy.Item;

public abstract class Job
{
    protected float workLeft;
    protected JobState jobState = JobState.OPEN;

    public Job()
    {
        resetJob();
    }

    public abstract void resetJob();
    public void updateJob(float delta)
    {
        workLeft -= delta;
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

}
