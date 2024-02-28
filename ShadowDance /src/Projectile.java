import bagel.*;
import org.lwjgl.system.windows.WindowProcI;

import java.util.ArrayList;

/**
 * This class represents a projectile in Shadow Dance.
 * It is created by the guardian when attacking an enemy and moves towards the cloest enemy target
 */
public class Projectile {
    private static final int INITIAL_X = 800;
    private static final int INITIAL_Y = 600;
    private final static Image image = new Image("res/arrow.png");
    private double x;
    private double y;
    private static final int speed = 6;
    // default value set to max and updated in constuctor to find closest enemy
    private int closestDistance = Integer.MAX_VALUE;
    private double radians = DEFAULT_RADIAN;
    private static double DEFAULT_RADIAN = Math.PI; // when no enemies on screen, simply shoot projectile straight
    private boolean active = true;
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    /**
     * Creates an instance of the projectile class using the specified parameters
     * @param enemies This is the array of enemies currently created in the level, which is used to determine
     *                which enemy is closest and therefore which direction the projectile will be shot
     */
    public Projectile(ArrayList<Enemy> enemies) {
        x = INITIAL_X;
        y = INITIAL_Y;
        // we locate the closest enemy to the projectile at the time it is launched
        for (int currEnemy = 0; currEnemy < enemies.size(); currEnemy++) {
            if (enemies.get(currEnemy).isActive()) {
                int enemyX = enemies.get(currEnemy).getX();
                int enemyY = enemies.get(currEnemy).getY();
                int distance = (int) Math.sqrt((x - enemyX) * (x - enemyX) + (y - enemyY) * (y - enemyY));
                if (distance < closestDistance) {
                    // closest one so far, so record the radians for the direction of movement
                    closestDistance = distance;
                    radians = Math.atan2(enemyY - y, enemyX - x);
                }
            }

        }
    }

    /**
     * Updates the state of the projectile
     */
    public void update() {

        // draw projectile while it is still within the window boundaries
        if (x > 0 && x < WINDOW_WIDTH && y > 0 && y < WINDOW_HEIGHT) {
            x += speed * Math.cos(radians);
            y += speed * Math.sin(radians);
            draw();
        } else {
            active = false;
        }

    }

    /**
     * draws the projectile onto the screen
     */
    public void draw() { image.draw(x,y, new DrawOptions().setRotation(radians));}

    /**
     * gets the x-coordinate of the projectile
     * @return the x-coordinate of the projectile
     */
    public double getX() {return x;}

    /**
     * gets the y-coordinate of the projectile
     * @return the y-coordinate of the projectile
     */
    public double getY() {return y;}

    /**
     * Gets whether the projectilen is currently active
     * @return 'true' if the projectile is active, 'false' otherwise
     */
    public boolean isActive() {return active;}

}
