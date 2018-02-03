package org.loomy;

public abstract class Job
{
    protected float workLeft;
    public abstract void resetJob();
    public abstract void updateJob(float delta);
    public abstract Item requiresItem();
    public abstract boolean requiresEmptyHands();
}
