/**
 * This class provides a blueprint for each type of Note in the ShadowDance game.
 */
public abstract class Notes {
    protected static int speed = 4;
    protected static int scoreFactor = 1;
    private static final int DOUBLE_SCORE_LENGTH = 480; // duration of a double score note
    private static boolean doubleScoreActive = false; // represents whether a double score note has been activated
    private static int doubleScoreFrame = 0; // represents how many frames the double score effect has been active for

    /**
     * This method activates the speed-up effect by increasing the speed of notes by 1
     */
    public static void speedUp() {
        speed += 1;
    }

    /**
     * This method activates the slow-down effect by decreasing the speed of notes by 1
     */
    public static void slowDown() {
        speed -= 1;
    }

    /**
     * This method activates the double score effect by doubling the score factor
     */
    public static void doubleScore() {
        doubleScoreActive = true;
        scoreFactor *= 2;
        doubleScoreFrame = 0;
    }

    /**
     *  This method checks and updates the current score factor used for scoring each note click
     */
    public static void setScoreFactor() {
        if (doubleScoreActive) {
            doubleScoreFrame++;
            if (doubleScoreFrame < DOUBLE_SCORE_LENGTH) {
                // still active, continue with current score factor
                return;
            } else {
                doubleScoreActive = false;
            }
        }
        // effect completed duration or isn't active, so we set the score factor back to original value
        scoreFactor = 1;

    }
    /**
     *  refreshes the state of all notes
     */
    public static void refreshAllNotes() {
        scoreFactor = 1;
        speed = 4;
        doubleScoreActive = false;
        doubleScoreFrame = 0;
    }
    /**
     * updates the state of the note
     * @param currFrame This is the current frame of the level
     */
    public abstract void update(int currFrame);







}
