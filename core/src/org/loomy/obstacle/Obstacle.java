package org.loomy.obstacle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.loomy.BoatScreen;

public abstract class Obstacle
{
    protected float size;
    protected Vector2 position;

    public Obstacle()
    {
        position = new Vector2();
        position.y = BoatScreen.BORDER_AT;
        position.x = (float) (MathUtils.random(BoatScreen.BORDER_AT) - BoatScreen.BORDER_AT/2.0) + BoatScreen.boatX; //Can only spawn close to boat
    }

    public void update(float delta)
    {
        position.y -= BoatScreen.BOAT_SPEED *  delta;
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
