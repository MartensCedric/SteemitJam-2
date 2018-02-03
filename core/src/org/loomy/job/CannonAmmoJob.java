package org.loomy.job;

import org.loomy.Item;

public class CannonAmmoJob extends Job {

    @Override
    public void updateJob(float delta) {
        super.updateJob(delta);
    }

    @Override
    public Item requiresItem() {
        return Item.NO_ITEM;
    }

    @Override
    public boolean requiresEmptyHands() {
        return true;
    }

    @Override
    public Item rewardedItem() {
        return Item.GUN_POWDER;
    }

    @Override
    protected float getTotalWork() {
        return 2;
    }
}
