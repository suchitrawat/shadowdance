import bagel.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents and enemy in level 3 of Shadow Dance
 */
public class Enemy {
    private final static Image image = new Image("res/enemy.png");
    private int x;
    private final int y;
    private final int POS_OFFSET = 100;
    private final int MAX_X = 900;
    private final int MAX_Y = 500;
    private final int MIN_X = 100;
    private int direction;
    private final int LEFT = 1;
    private final int RIGHT = -1;
    private boolean active = true;
    private final static int KILL_RADIUS = 62;

    /**
     * Creates an instance of the enemy class, the initial x position is randomly chosen between 100 and 900
     * and the y position is randomly chosen between 100 and 500,
     * with left or right direction also being randomly chosen
     */
    public Enemy() {
        Random randomX = new Random();
        Random randomY = new Random();
        // randomly generating the inital x and y position
        x = randomX.nextInt(MAX_X) + POS_OFFSET;
        y = randomY.nextInt(MAX_Y) + POS_OFFSET;

        // randomly generate 0 or 1 to determine whether to initially go right or left respectively
        Random randomDir = new Random();
        int randDir = randomDir.nextInt(2);
        if (randDir == 0) {
            direction = RIGHT;
        } else {
            direction = LEFT;
        }
    }

    /**
     * updates the state of the enemy
     */
    public void update() {
        if (active) {
            // check if we have reached the border and update direction if so
            if (x >= MAX_X) {
                direction = RIGHT;
            } else if (x <= MIN_X) {
                direction = LEFT;
            }
            // update x posii
            x += direction;
            draw();
        }

    }

    /**
     * draws the enemy onto the screen
     */
    public void draw() {
        image.draw(x,y);
    }

    /**
     * gets the value of the x-coordinate
     * @return The current value of the x-coordinate
     */
    public int getX() {return x;}

    /**
     * gets the value of the y-coordinate
     * @return The current value of the y-coordinate
     */
    public int getY() {return y;}

    /**
     * Checks for collision between the enemy and any active projectile, deactivating the enemy if a collision occurs
     * @param projectiles These are all the projectiles that have been created by the guardian in the level
     */
    public void checkCollision(ArrayList<Projectile> projectiles) {
        // go through each active projectile
        for (int currProjectile = 0; currProjectile < projectiles.size(); currProjectile++) {

            if (projectiles.get(currProjectile).isActive()) {
                // compute the distance between the projectile and the enemy
                double projectileX =  projectiles.get(currProjectile).getX();
                double projectileY =  projectiles.get(currProjectile).getY();
                double distance = Math.sqrt((projectileX - x)*(projectileX - x) + (projectileY - y)*(projectileY-y));
                if (distance <= KILL_RADIUS) {
                    // within specified radius, so eliminate (deactivate) the enemy
                    active = false;
                }

            }


        }

    }

    /**
     * Checks if the enemy is currently active
     * @return 'true' if the enemy is active, 'false' otherwise
     */
    public boolean isActive() {return active;}


}
