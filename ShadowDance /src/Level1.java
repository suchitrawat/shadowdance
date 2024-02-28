import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class represents the first level of ShadowDance.
 * Normal and Hold Notes drop vertically from 4 lanes. The player must press the corresponding arrow keys when a
 * note overlaps with a stationary note symbol at the bottom, with accuracy determining points. To finish, the player
 * must beat a target score of 150 when all notes have fallen.
 */
public class Level1 extends Levels {
    private final static String CSV_FILE = "res/level1-60.csv";
    private final Lane[] lanes = new Lane[4];
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private final Accuracy accuracy = new Accuracy();
    private boolean paused = false;

    private int CLEAR_SCORE = 150;



    @Override
    public void readCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }


    @Override
    public boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void gameplay(Input input) {

        // gameplay

        SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

        if (paused) {
            if (input.wasPressed(Keys.TAB)) {
                paused = false;

            }

            for (int i = 0; i < numLanes; i++) {
                lanes[i].draw();
            }

        } else {
            currFrame++;
            for (int i = 0; i < numLanes; i++) {
                score += lanes[i].update(input, accuracy, currFrame);
            }

            accuracy.update(LEVEL_PLAYING);
            if (checkFinished()) {
                // level finished
                end_level();
                if (score >= CLEAR_SCORE) {
                    setCleared(true);
                }
            }

            if (input.wasPressed(Keys.TAB)) {
                paused = true;

            }
        }


    }

    @Override
    public void refresh() {
        accuracy.update(LEVEL_REFRESH);
        currFrame = 0;
        score = 0;
        for (int i = 0; i < numLanes; i++) {
            lanes[i].refresh();
        }
        setCleared(false);

    }


}



