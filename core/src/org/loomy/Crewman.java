package org.loomy;

import com.badlogic.gdx.math.Vector2;


public class Crewman
{
    private Job job;
    private Item item = Item.NO_ITEM;
    private Vector2 position = new Vector2();
    private Vector2 direction = new Vector2().setToRandomDirection();
    private float speed; //influences walking and productivity

    public boolean hasJob() { return job != null; }
    public boolean isEmptyHanded() { return item == Item.NO_ITEM; }

    public Crewman(float x, float y)
    {
        position = new Vector2(x, y);
    }

    public void update(float delta) {}

    public float getRotation()
    {
        return direction.angle();
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
