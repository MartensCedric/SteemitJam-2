package org.loomy;

import com.badlogic.gdx.math.Vector2;
import org.loomy.job.JobLocation;


public class Crewman
{
    private Item item = Item.NO_ITEM;
    private Vector2 position;
    private Vector2 direction;
    private JobLocation walkingTarget;
    private JobLocation currentJob;

    private float speed; //influences walking and productivity

    public boolean isEmptyHanded() { return item == Item.NO_ITEM; }

    public Crewman(float x, float y)
    {
        position = new Vector2(x, y);
        direction = new Vector2().setToRandomDirection();
        speed = 75f;
    }

    public void update(float delta) {
        if(hasTarget())
        {
            Vector2 target = new Vector2(walkingTarget.getX(), walkingTarget.getY());
            direction.set(new Vector2(target).sub(position));
            direction.nor();

            position.add(direction.cpy().scl(speed * delta));

            if(position.epsilonEquals(target, 1f))
            {
                position = target;
                walkingTarget.getJob().startJob();
                currentJob = walkingTarget;
                walkingTarget = null;
            }
        }
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }

    public boolean hasTarget() { return walkingTarget != null; }

    public float getSpeed() { return speed; }

    public Item getItem() {
        return item;
    }

    public void setTarget(JobLocation target) {
        this.walkingTarget = target;
    }

    public JobLocation getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(JobLocation currentJob) {
        this.currentJob = currentJob;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
