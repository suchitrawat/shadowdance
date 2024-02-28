import bagel.*;

/**
 * This class represents a Special Note.
 * It can be of type Bomb, DoubleScore, SpeedUp and SlowDown, and each type has its own unique effects
 */
public class SpecialNote extends Notes {
    private int y = 100;
    private final Image image;
    private boolean active = false;
    private boolean effectActive = false;
    private boolean completed = false;
    private final int appearanceFrame;
    private final String type;

    /**
     * Creates a new instance of SpecialNote with the specified parameters
     * @param type This represents the type of special note
     * @param appearanceFrame This represents the frame the note first appears in
     */
    public SpecialNote(String type, int appearanceFrame) {
        this.type = type;
        if (type.equals("DoubleScore")) {
            type = "2x";
        }
        image = new Image("res/note" + type + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Signals to stop drawing the note and that the note has been completed in the level
     */
    public void deactivate() {
        active = false;
        completed = true;
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

    @Override
    public void update(int currFrame) {
        if (active) {
            y += speed;
        }

        if (currFrame >= appearanceFrame && !completed) {
            // currently in frame so mark as active
            active = true;

        }
    }

    /**
     * Updates the activation state of a special note
     * @param input This is the keyboard input being entered during gameplay
     * @param accuracy This computes the accuracy of the note when clicked
     * @param targetHeight This is the height of the centre of the lane target
     * @param relevantKey This is the key needed to be pressed to activate the note
     * @return the score for the note based on the parameters
     */
    // checks if a special note has been activated, and activates if prompted
    public int checkActivation(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {

        if (isActive()) {

            // evaluate accuracy of the key press

            int score = accuracy.evaluateActivation(y, targetHeight, input.wasPressed(relevantKey), type);

            if (score != Accuracy.NOT_SCORED) {

                if (score == Accuracy.NOT_ACTIVATED) {
                    score = 0; // no score for missing
                } else {
                    if (score == Accuracy.BOMB_SCORE) {
                        score = 0; // no score for hitting a bomb
                    }
                    effectActive = true;
                }
                deactivate();
                return score;

            }


        }
        return 0;

    }

    /**
     * Checks if the note has been completed
     * @return 'true' if the note has completed its use in the lane, 'false' otherwise
     */
    public boolean isCompleted() {return completed;}


    /**
     * Checks if the note is currently active
     * @return 'true' if the note is active, 'false' otherwise
     */
    public boolean isActive() {return active;}

    /**
     * Gets the type of the note
     * @return the type of special note
     */
    public String getType() { return type; }

    /**
     * This method is used to reset the state of the note to enable the level to be replayed
     */
    public void refresh() {
        effectActive = false;
        completed = false;
        y = 100;
    }

    /**
     * Gets whether the note's effects are currently activated
     * @return 'true' if note effect is currently activated, 'false' otherwise
     */
    public boolean isEffectActive() {return effectActive;}

    /**
     * Signals that the note effect has been used or can no longer be used (due to a miss)
     */
    public void effectComplete() {effectActive = false;}

    /**
     * Gets the y value of the note
     * @return the y-coordinate of the note
     */
    public int getY() {return y;}
}
