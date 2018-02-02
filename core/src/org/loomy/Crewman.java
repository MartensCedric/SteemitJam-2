package org.loomy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Crewman extends Actor
{
    private Job job;
    private Item item = Item.NO_ITEM;
    private Vector2 direction = new Vector2().setAngleRad((float) Math.PI);
    private float speed; //influences walking and productivity

    public boolean hasJob() { return job != null; }
    public boolean isEmptyHanded() { return item == Item.NO_ITEM; }

    public Crewman(float x, float y)
    {
        setX(x);
        setY(y);
    }

    public float getRotation()
    {
        return direction.angle();
    }
}
