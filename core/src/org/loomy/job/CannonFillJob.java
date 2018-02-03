package org.loomy.job;

import org.loomy.Item;

public class CannonFillJob extends Job{
    @Override
    public Item requiresItem() {
        return Item.CANNONBALL;
    }

    @Override
    public boolean requiresEmptyHands() {
        return false;
    }

    @Override
    public Item rewardedItem() {
        return Item.NO_ITEM;
    }

    @Override
    protected float getTotalWork() {
        return 3;
    }
}
