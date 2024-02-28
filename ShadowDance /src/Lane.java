import bagel.*;

import java.util.ArrayList;

/* The skeleton structure for this class (relevant to project 1)
was taken from the project 1A solution written by Stella Li */

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private final ArrayList<Note> notes = new ArrayList<>();
    private int numNotes = 0;
    private final ArrayList<HoldNote> holdNotes = new ArrayList<>();
    private int numHoldNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private int currHoldNote = 0;
    private final ArrayList<SpecialNote> bombNotes = new ArrayList<>();
    private int currBombNote = 0;
    private int numBombNotes = 0;

    private static final int STEAL_DISTANCE = 104;


    /**
     * Creates an instance of a Lane class from the specified parameters
     * @param dir the direction-type of the lane
     * @param location the x-position of the lane
     */
    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
        }
    }

    /**
     * gets the lane-type
     * @return the lane type
     */
    public String getType() {
        return type;
    }

    /**
     * updates all the notes in the lane
     * @param input This is the keyboard input being entered during gameplay
     * @param accuracy This computes the accuracy of the note when clicked
     * @param currFrame This is the current frame of the level
     * @return the score assigned for the lane in the current frame
     */
    public int update(Input input, Accuracy accuracy, int currFrame) {

        draw();
        // loops through all notes in the lane and draws active ones in their correct frame
        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).update(currFrame);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).update(currFrame);
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).update(currFrame);
        }

        // grabs the score of the notes

        if (currNote < numNotes) {
            if (currBombNote < numBombNotes) {
                if (notes.get(currNote).getY() > bombNotes.get(currBombNote).getY()) {
                    // to prevent overlap between bomb and normal note scoring, we score whichever is lower
                    int score = notes.get(currNote).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
                    if (notes.get(currNote).isCompleted()) {
                        currNote++;
                        return score;
                    }
                }
            } else {
                int score = notes.get(currNote).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
                if (notes.get(currNote).isCompleted()) {
                    currNote++;
                    return score;
                }
            }
        }

        if (currBombNote < numBombNotes) {
            int score = bombNotes.get(currBombNote).checkActivation(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (bombNotes.get(currBombNote).isCompleted()) {
                // check if we successfully activated the bomb
                if (bombNotes.get(currBombNote).isEffectActive()) {
                    clearLane();
                }
                bombNotes.get(currBombNote).effectComplete();
                currBombNote++;
                return score;
            }
        }


        if (currHoldNote < numHoldNotes) {
            int score = holdNotes.get(currHoldNote).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (holdNotes.get(currHoldNote).isCompleted()) {
                currHoldNote++;
            }
            return score;

        }




        return Accuracy.NOT_SCORED;
    }

    /**
     * adds a note to the lane
     * @param n the note to be added to the lane
     */
    public void addNote(Note n) {
        notes.add(numNotes++, n);
    }

    /**
     * adds a hold note to the lane
     * @param hn the hold note to be added to the lane
     */
    public void addHoldNote(HoldNote hn) {
        holdNotes.add(numHoldNotes++, hn);
    }

    /**
     * adds a bomb note to the lane
     * @param bn the bomb note to be added to the lane
     */
    public void addBombNote(SpecialNote bn) {
        bombNotes.add(numBombNotes++, bn);
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!notes.get(i).isCompleted()) {
                return false;
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if (!holdNotes.get(j).isCompleted()) {
                return false;
            }
        }

        for (int k = 0; k < numBombNotes; k++) {
            if (!bombNotes.get(k).isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).draw(location);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).draw(location);
        }
        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).draw(location);
        }

    }


    /**
     * refreshes the state of each note in the lane
     */
    public void refresh() {
        currHoldNote = 0;
        currNote = 0;
        currBombNote = 0;

        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).refresh();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).refresh();
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).refresh();
        }

    }

    /**
     * clears all currently activated notes in the lane
     */
    public void clearLane() {
        // deactivates all notes when a bomb note has been activated
        for (int i = 0; i < numNotes; i++) {
            if(notes.get(i).isActive()) {
                notes.get(i).deactivate();
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if(holdNotes.get(j).isActive()) {
                holdNotes.get(j).deactivate();
            }
        }

        for (int k = 0; k < numBombNotes; k++) {
            if(bombNotes.get(k).isActive()) {
                bombNotes.get(k).deactivate();
            }
        }

    }
    /** This method is used to check collisions between notes in the normal lane and enemies in the game
     * @param enemies These are the enemies in the game being checked for collision with the notes in the lane
     */
    public void checkEnemyCollision(ArrayList<Enemy> enemies) {
        for (int currEnemy = 0; currEnemy < enemies.size(); currEnemy++) {
            // only checking collision with active enemies
            if (enemies.get(currEnemy).isActive()) {
                // get x and y point of a specific enemy
                int enemyX = enemies.get(currEnemy).getX();
                int enemyY = enemies.get(currEnemy).getY();
                for (int i = 0; i < numNotes; i++) {
                    // only check for notes currently being drawn
                    if (notes.get(i).isActive()) {
                        // now get the x and y point of each note to compare to the enemy
                        int noteX = location;
                        int noteY = notes.get(i).getY();
                        int distance = (int) Math.sqrt((noteX - enemyX)*(noteX - enemyX) +
                                (noteY-enemyY)*(noteY-enemyY));

                        // if the distance is less than 104, then we must deactivate the note
                        if (distance < STEAL_DISTANCE) {
                            notes.get(i).deactivate();
                        }

                    }

                }

                // same logic and method applies for the hold and bomb notes

                for (int j = 0; j < numHoldNotes; j++) {
                    if (holdNotes.get(j).isActive()) {
                        int noteX = location;
                        int noteY = holdNotes.get(j).getY();
                        int distance = (int) Math.sqrt((noteX - enemyX)*(noteX - enemyX) +
                                (noteY-enemyY)*(noteY-enemyY));
                        if (distance < STEAL_DISTANCE) {
                            holdNotes.get(j).deactivate();
                        }

                    }

                }

                for (int k = 0; k < numBombNotes; k++) {
                    if (bombNotes.get(k).isActive()) {
                        int noteX = location;
                        int noteY = bombNotes.get(k).getY();
                        int distance = (int) Math.sqrt((noteX - enemyX)*(noteX - enemyX) +
                                (noteY-enemyY)*(noteY-enemyY));

                        if (distance < STEAL_DISTANCE) {
                            bombNotes.get(k).deactivate();
                        }

                    }

                }

            }

        }


    }

}
