package org.loomy.creature;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.loomy.BoatScreen;
import org.loomy.Cannonball;
import org.loomy.MathUtil;

import static org.loomy.BoatScreen.cannonballs;

public abstract class SeaCreature
{
    protected float speed;
    protected float size;
    protected int hitpoints;

    protected Vector2 position;
    private boolean right;

    public boolean isFacingRight() { return right; }

    public SeaCreature()
    {
        right = MathUtils.randomBoolean();
        position = new Vector2(right ? -3_600 : 3_600, 0);
    }

    public void update(float delta)
    {
        position.x += (isFacingRight() ? speed : -speed) * delta;

        for(int i = 0; i < cannonballs.size(); i++)
        {
            Cannonball c = cannonballs.get(i);
            if(MathUtil.isBetween(c.getPosition().x,
    getX() - getSize()/2,
    getX() + getSize()/2))
            {
                hitpoints--;
                cannonballs.remove(i);
                i--;
            }
        }
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getSize() { return size; }
    public boolean isDead() { return hitpoints <= 0; }
}
