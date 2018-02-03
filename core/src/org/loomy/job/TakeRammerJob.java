package org.loomy.job;

import org.loomy.Item;

public class TakeRammerJob extends Job{
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
        return Item.RAMMER;
    }

    @Override
    protected float getTotalWork() {
        return 1;
    }
}
