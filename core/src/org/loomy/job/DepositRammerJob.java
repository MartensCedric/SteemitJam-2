package org.loomy.job;

import org.loomy.Item;

public class DepositRammerJob extends Job{
    @Override
    public Item requiresItem() {
        return Item.RAMMER;
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
        return 1;
    }
}
