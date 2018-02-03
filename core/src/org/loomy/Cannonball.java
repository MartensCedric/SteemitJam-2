package org.loomy;

import com.badlogic.gdx.math.Vector2;

public class Cannonball
{
    private Vector2 position;
    private boolean right;
    private float speed;

    public Cannonball(Vector2 position)
    {
        right = position.x > 0;
        this.position = position;
        this.position.x += right ? 100 : -100;
        this.speed = 250;
    }

    public void update(float delta)
    {
        position.x += (right ? speed : -speed) * delta;
    }

    public Vector2 getPosition() {
        return position;
    }
}
