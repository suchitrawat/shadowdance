import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/* The skeleton structure for this class (relevant to project 1)
was taken from the project 1A solution written by Stella Li */

/**
 * Solution for SWEN20003 Project 2B, Semester 2, 2023
 *
 * @author Suchit Rawat 1354121
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    protected final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private static final String INSTRUCTIONS = "SELECT LEVEL WITH\n\n     NUMBER KEYS\n\n" +
            "         1  2  3";
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String LEVEL_SELECT_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private final Level1 level1 = new Level1();
    private final Level2 level2 = new Level2();
    private final Level3 level3 = new Level3();

    private boolean levelClear = false;

    /**
     * Creates an instance of the Shadow Dance game class
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        // reading the respective csv files for each level to start the game
        level1.readCsv();
        level2.readCsv();
        level3.readCsv();
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }



    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * Allows players to replay a level once finished or select another level in the game
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // while none of the 3 levels have been started, show title screen
        if (!level1.isCurrPlaying() && !level2.isCurrPlaying() && !level3.isCurrPlaying()) {
            // now check whether we are displaying a start or end screen
            if (level1.isMostrecent() || level2.isMostrecent() || level3.isMostrecent()) {
                // end screen
                if (levelClear) {
                    TITLE_FONT.drawString(CLEAR_MESSAGE,
                            WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                            WINDOW_HEIGHT/2);
                } else {
                    TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                            WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                            WINDOW_HEIGHT/2);
                }
                INSTRUCTION_FONT.drawString(LEVEL_SELECT_MESSAGE,
                        WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(LEVEL_SELECT_MESSAGE)/2,
                        500);
                if (input.wasPressed(Keys.SPACE)) {
                    // return to start screen and refresh the levels
                    level1.setMostrecent(false);
                    level1.refresh();
                    level2.setMostrecent(false);
                    level2.refresh();
                    level3.setMostrecent(false);
                    level3.refresh();
                    levelClear = false;
                }
            } else {
                // starting screen
                TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
                INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                        TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
            }


            // insert level selecton implementation here
            if (input.wasPressed(Keys.NUM_1)) {

                if (!level2.isCurrPlaying() && !level3.isCurrPlaying()) {
                    // prompt pressed and others level aren't currently playing, so signal to start level
                    level1.start_level();
                    level1.setMostrecent(true);
                    level2.setMostrecent(false);
                    level3.setMostrecent(false);
                }

            } else if (input.wasPressed(Keys.NUM_2)) {
                if (!level1.isCurrPlaying() && !level3.isCurrPlaying()) {
                    level2.start_level();
                    level1.setMostrecent(false);
                    level2.setMostrecent(true);
                    level3.setMostrecent(false);
                }


            } else if (input.wasPressed(Keys.NUM_3)) {
                if (!level1.isCurrPlaying() && !level2.isCurrPlaying()) {
                    level3.start_level();
                    level1.setMostrecent(false);
                    level2.setMostrecent(false);
                    level3.setMostrecent(true);
                }

            }
        } else {
            playLevels(input);

        }

    }

    /**
     * Plays the level specified by the users input
     * @param input the keyboard input from the user, which determines the level selected
     */
    public void playLevels(Input input) {
        // gameplay
        // first checking which level is the most recent one we have interacted with
        if (level1.isMostrecent()) {
            if (level1.isCurrPlaying()) {
                // level still undergoing, continue gameplay and check whether level is cleared
                level1.gameplay(input);
                levelClear = level1.isLevelClear();
            }

        } else if (level2.isMostrecent()) {
            if (level2.isCurrPlaying()) {
                level2.gameplay(input);
                levelClear = level2.isLevelClear();
            }

        } else if (level3.isMostrecent()) {
            if (level3.isCurrPlaying()) {
                level3.gameplay(input);
                levelClear = level3.isLevelClear();
            }

        }

    }

}
