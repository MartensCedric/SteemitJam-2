package org.loomy.creature;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.loomy.BoatScreen;
import org.loomy.Cannonball;
import org.loomy.GameManager;
import org.loomy.MathUtil;

import static org.loomy.BoatScreen.BORDER_AT;
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
        position = new Vector2(right ? -BORDER_AT - 50 : BORDER_AT + 50, 0);
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

                if(hitpoints <= 0)
                    GameManager.soundMap.get("monster_death").play();
                else GameManager.soundMap.get("monster_hit").play();

                cannonballs.remove(i);
                i--;
            }
        }
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getSize() { return size; }
    public boolean isDead() { return hitpoints <= 0; }
    public int getHitpoints() { return hitpoints; }
}
