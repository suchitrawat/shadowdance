import bagel.*;

/* The skeleton structure for this class (relevant to project 1)
was taken from the project 1A solution written by Stella Li */

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {
    protected static final int PERFECT_SCORE = 10;
    protected static final int GOOD_SCORE = 5;
    protected static final int BAD_SCORE = -1;
    protected static final int MISS_SCORE = -5;
    protected static final int NOT_SCORED = 0;
    protected static final String PERFECT = "PERFECT";
    protected static final String GOOD = "GOOD";
    protected static final String BAD = "BAD";
    protected static final String MISS = "MISS";
    protected static final int PERFECT_RADIUS = 15;
    protected static final int GOOD_RADIUS = 50;
    protected static final int BAD_RADIUS = 100;
    protected static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;
    private static final int LEVEL_REFRESH = 0; // signal to refresh the level
    private static final int ACTIVATION_RADIUS = 15;
    protected static final int BOMB_SCORE = -2; // signals a bomb has been activated

    // the messages for successful activation of a special  note
    private static final String SPEED_UP = "Speed Up";
    private static final String SLOW_DOWN = "Slow Down";
    private static final String DOUBLE_SCORE = "Double Score";

    private static final String CLEAR_LANE = "Clear Lane";

    // the state of a special note in the special lane, inluding the score for activation
    public static final int NOT_ACTIVATED = -1;
    private static final int ACTIVATED_SCORE = 15;


    /**
     * sets the accuracy of the note click
     * @param accuracy This is the accuracy instance we want to set the accuracy to
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * evaluates the score of a note click
     * @param height This is the height of the centre of the note
     * @param targetHeight This is the height of the centre of the lane target
     * @param triggered This represents whether the specified keyboard input was triggered
     * @return int This returns the score of the note
     */

    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE * Notes.scoreFactor;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE * Notes.scoreFactor;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE * Notes.scoreFactor;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE * Notes.scoreFactor;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE * Notes.scoreFactor;
        }

        return NOT_SCORED;

    }

    /**
     * when a note is clicked, draws the relevant accuracy message
     * @param levelState represents whether the level has finished or is still playing
     */
    public void update(int levelState) {
        if (levelState == LEVEL_REFRESH) {
            // refreshing frameCount and accuracy value so level can be played again
            frameCount = 0;
            currAccuracy = null;
        }
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }
    }

    /**
     * Evaluates the score and activation status for a special note
     * @param height This is the height of the centre of the note
     * @param targetHeight This is the height of the centre of the lane target
     * @param triggered This represents whether the specified keyboard input was triggered
     * @param type This represents the type of special note
     * @return int This returns the score of the note
     */
    public int evaluateActivation(int height, int targetHeight, boolean triggered, String type) {
        int distance = Math.abs(height - targetHeight);
        if (triggered) {
            if (distance <= ACTIVATION_RADIUS) {
                // clicked within the specified radius, now check which type of note we activated
                switch (type) {
                    case "DoubleScore":
                        setAccuracy(DOUBLE_SCORE);
                        break;
                    case "SlowDown":
                        setAccuracy(SLOW_DOWN);
                        break;
                    case "SpeedUp":
                        setAccuracy(SPEED_UP);
                        break;
                    case "Bomb":
                        setAccuracy(CLEAR_LANE);
                        return BOMB_SCORE;
                }

                return ACTIVATED_SCORE * Notes.scoreFactor;
            } else {
                // missed the activation radius, signal to not activate the special effects
                return NOT_ACTIVATED;
            }

        } else if (height >= (Window.getHeight())) {
            // special note out of frame before we clicked so simply deactivate it
            return NOT_ACTIVATED;
        }

        // note still moving and no prompt to activate has been provided yet
        return NOT_SCORED;

    }
}
