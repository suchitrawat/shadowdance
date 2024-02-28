import bagel.*;

import java.util.ArrayList;

/**
 * This class represents a Special Lane featured in level 2 and 3.
 * Only special notes fall down this lane, and space key is used to activate the special notes
 */
public class SpecialLane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final int location;
    private final Image image = new Image("res/laneSpecial.png");
    private final ArrayList<SpecialNote> specNotes = new ArrayList<>();
    private int numSpecNotes = 0;
    private int currSpecNote = 0;
    private static final int STEAL_DISTANCE = 104;
    private static final Keys relevantKey = Keys.SPACE;

    /**
     * Creates a new instance of special lane with the specified parameters
     * @param location This is the x coordinate of the lane
     */
    public SpecialLane(int location) {
        this.location = location;
    }

    /**
     * adds a special note to the special note array in the special lane
     * @param n This is the Special Note being inserted into the array
     */
    public void addSpecialNote(SpecialNote n) {
        specNotes.add(numSpecNotes++, n);
    }

    /**
     * updates all the notes in the Special lane
     * @param input This is the keyboard input being entered during gameplay
     * @param accuracy This computes the accuracy of the note when clicked
     * @param currFrame This is the current frame of the level
     * @return the score assigned for the lane in the current frame
     */
    public int update(Input input, Accuracy accuracy, int currFrame) {
        draw();

        for (int i = currSpecNote; i < numSpecNotes; i++) {
            specNotes.get(i).update(currFrame);
        }
        if (currSpecNote < numSpecNotes) {
            // constantly check if we have activated the special note
            int score = specNotes.get(currSpecNote).checkActivation(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (specNotes.get(currSpecNote).isCompleted()) {
                // note completed, now check if player successfully activated the note
                if (specNotes.get(currSpecNote).isEffectActive()) {
                    // yes, so find which type of note we are activating
                    if (specNotes.get(currSpecNote).getType().equals("SpeedUp")) {
                        // update the speed of all notes by +1 and deactivate the note
                        Notes.speedUp();
                    } else if (specNotes.get(currSpecNote).getType().equals("SlowDown")) {
                        // update the speed of all notes by -1 and deactivate the note
                        Notes.slowDown();
                    } else if (specNotes.get(currSpecNote).getType().equals("DoubleScore")) {
                        // signal to double the score for 480 frames
                        Notes.doubleScore();
                    } else if (specNotes.get(currSpecNote).getType().equals("Bomb")) {
                        // clear the lane
                        clearLane();
                    }
                    // signal that effect has been applied
                    specNotes.get(currSpecNote).effectComplete();
                }

                // yes, move onto the next special note
                currSpecNote++;
            }
            return score;
        }
        return Accuracy.NOT_SCORED;


    }

    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currSpecNote; i < numSpecNotes; i++) {
            specNotes.get(i).draw(location);
        }

    }

    /**
     * This method is used to reset the state of the lane and its notes so the level can be restarted from scratch
     */
    public void refresh() {
        currSpecNote = 0;

        for (int i = currSpecNote; i < numSpecNotes; i++) {
            // refreshing the state of each note in the lane
            specNotes.get(i).refresh();
        }

    }

    /**
     * this method is used to clear the special lane when a bomb note is activated
     * */
    public void clearLane() {
        for (int i = 0; i < numSpecNotes; i++) {
            if (specNotes.get(i).isActive()) {
                specNotes.get(i).deactivate();
            }
        }
    }

    /**
     * This method is used to check collisions between notes in the special lane and enemies in the game
     * @param enemies These are the enemies in the game being checked for collision with the notes in the lane
     */
    public void checkEnemyCollision(ArrayList<Enemy> enemies) {
        for (int currEnemy = 0; currEnemy < enemies.size(); currEnemy++) {
            if (enemies.get(currEnemy).isActive()) {
                // get x and y point of a specific enemy
                int enemyX = enemies.get(currEnemy).getX();
                int enemyY = enemies.get(currEnemy).getY();
                for (int i = 0; i < numSpecNotes; i++) {
                    // only check for notes currently being drawn
                    if (specNotes.get(i).isActive()) {
                        // now get the x and y point of each note to compare to the enemy
                        int noteX = location;
                        int noteY = specNotes.get(i).getY();
                        int distance = (int) Math.sqrt((noteX - enemyX) * (noteX - enemyX) +
                                (noteY - enemyY) * (noteY - enemyY));

                        // if the distance is less than 104, then we must deactivate the note
                        if (distance < STEAL_DISTANCE) {
                            specNotes.get(i).deactivate();
                        }

                    }

                }
            }

        }
    }
}

