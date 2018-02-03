package org.loomy.job;

import org.loomy.Item;

public class SteerShipJob extends Job {
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
        return Item.NO_ITEM;
    }

    @Override
    protected float getTotalWork() {
        return 1f;
    }
}
