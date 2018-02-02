package org.loomy;

public class Crewman
{
    private Job job;
    private Item item = Item.NO_ITEM;

    public boolean hasJob() { return job != null; }
    public boolean isEmptyHanded() { return item == Item.NO_ITEM; }
}
