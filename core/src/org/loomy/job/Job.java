package org.loomy.job;

import org.loomy.Item;

public abstract class Job
{
    protected float workLeft;

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
}
