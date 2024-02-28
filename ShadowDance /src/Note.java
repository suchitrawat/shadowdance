import bagel.*;

/* The skeleton structure for this class (relevant to project 1)
was taken from the project 1A solution written by Stella Li */

/**
 * Class for normal notes
 */
public class Note extends Notes {
    private final Image image;
    private final int appearanceFrame;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;

    /**
     * creates a new instance of Note with the specified parameters
     * @param dir This represents the direction of the note
     * @param appearanceFrame This represents the frame the note appears in
     */
    public Note(String dir, int appearanceFrame) {
        image = new Image("res/note" + dir + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Checks if the note is currently active
     * @return 'true' if the note is active, 'false' otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the note has been completed
     * @return 'true' if the note has completed its use in the lane, 'false' otherwise
     */
    public boolean isCompleted() {return completed;}

    /**
     * Signals to stop drawing the note and that the note has been completed in the level
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    @Override
    public void update(int currFrame) {
        if (active) {
            y += speed;
        }

        if (currFrame >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * draws the note onto the screen using a default y value and the specified parameter
     * @param x This is the x value to draw the note onto
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Evaluates the score for the note
     * @param input This is the keyboard input being entered during gameplay
     * @param accuracy This computes the accuracy of the note when clicked
     * @param targetHeight This is the height of the centre of the lane target
     * @param relevantKey This is the key needed to be pressed to activate the note
     * @return the score for the note
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }

        }

        return 0;
    }
    /**
     * This method is used to reset the state of the note to enable the level to be replayed
     */
    public void refresh() {
        completed = false;
        y = 100;
    }


    /**
     * Gets the y value of the note
     * @return the y-coordinate of the note
     */
    public int getY() { return y;}

}
