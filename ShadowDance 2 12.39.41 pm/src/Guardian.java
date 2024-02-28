import bagel.Image;

import java.util.ArrayList;

/**
 * Represents a guardian entity in level 3 of Shadow Dance
 */
public class Guardian {
    private static final int GUARDIAN_X = 800;
    private static final int GUARDIAN_Y = 600;
    private final static Image image = new Image("res/guardian.png");
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * draws the Guardian onto the screen and calls to draw projectiles based on the parameters
     * @param triggered This represents whether the keyboard input for firing a projectile was triggered
     * @param enemies This represents the enemies created in the game
     */
    public void draw(boolean triggered, ArrayList<Enemy> enemies) {

        image.draw(GUARDIAN_X, GUARDIAN_Y);
        // check if shoot button was triggered
        if (triggered) {
            // yes, create a new projectile
            Projectile projectile = new Projectile(enemies);
            projectiles.add(projectile);
        }

        // draw all the currently active projectiles
        drawProjectiles();
    }

    /**
     * updates the state of all projectiles
     */
    public void drawProjectiles() {
        // we iterate through each projectile created
        for (int currProjectile = 0; currProjectile < projectiles.size(); currProjectile++) {
            // and update its state, which will draw all the active ones and check for collisions with enemies
            projectiles.get(currProjectile).update();

        }
    }

    /**
     * Gets all the projectiles created by the guardian
     * @return An array of all the projectiles created by the enemy
     */
    public ArrayList<Projectile> getProjectiles() {return projectiles; }

    /**
     * Clears the array of projectiles created by the guardian to reset the state of the class back to default
     */
    public void refresh() {
        projectiles.clear();
    }
}
