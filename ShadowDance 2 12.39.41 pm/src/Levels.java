import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class provides a blueprint for each level in the ShadowDance game.
 * Subclasses are expected to implement specific methods and features for each level
 */
public abstract class Levels {
    private boolean currPlaying = false;
    private final static String FONT_FILE = "res/FSO8BITR.TTF";
    protected final Font SCORE_FONT = new Font(FONT_FILE, 30);
    protected final static int SCORE_LOCATION = 35;
    protected static final int LEVEL_REFRESH = 0;
    protected static final int LEVEL_PLAYING = 1;
    private boolean mostRecent = false;
    private boolean cleared = false;


    /**
     * reads the csv file and accordingly inputs lanes and notes with the provided data in the csv file
     * into arrays to be used for gameplay
     */
    public abstract void readCsv();

    /**
     * checks if all notes have been used and therefore the level is finished
     * @return boolean This represents whether the level is finished
     */
    public abstract boolean checkFinished();


    /**
     * signals to start the level by setting the currPlaying boolean value to true
     */
    public void start_level() {
        currPlaying = true;
    }

    /**
     * signals to end the level by setting the currPlaying boolean value to false
     */
    public void end_level() {
        currPlaying = false;
    }

    /* signals that the level is currently active */

    /**
     * checks if the level is currently being played
     * @return 'true' if the level is currently being played, 'false' otherwise
     */
    public boolean isCurrPlaying() {
        return currPlaying;
    }

    /* handles the gameplay for the specific level */

    /**
     * Handles the gameplay for the level by calling all methods associated with the gameplay for the level
     * @param input This is the keyboard input for the current frame
     */
    public abstract void gameplay(Input input);


    /**
     * checks if the level was the one most recently played
     * @return 'true' if the level was most recently played, 'false' otherwise
     */
    public boolean isMostrecent() {
        return mostRecent;
    }

    /**
     * sets the value of the mostRecent parameter
     * @param mostRecent This represents whether the level was most recently played
     */
    public void setMostrecent(boolean mostRecent) {
        this.mostRecent = mostRecent;

    }

    /**
     * Gets whether the level was successfully cleared or not
     * @return 'true' if the level was cleared, 'false' otherwise
     */
    public boolean isLevelClear() {
        return cleared;
    }

    /**
     * Sets the value of the cleared parameter
     * @param cleared This represents whether the level was cleared or not
     */
    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    /**
     * resets the classes and state of all parameters in the level to their
     * default value so the level can be replayed
     */
    public abstract void refresh();



}
