import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class represents Level 2 of Shadow Dance.
 * Level 2 features similar gameplay features to level 1, with the addition of special notes and a special lane. To
 * win, the player must beat a target score of 400 when all notes have fallen.
 */
public class Level2 extends Levels {
    private final static String CSV_FILE = "res/test2-60.csv";
    private final Lane[] lanes = new Lane[4];
    private SpecialLane specialLane;
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private final Accuracy accuracy = new Accuracy();
    private boolean paused = false;

    private int CLEAR_SCORE = 400;

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

                    if (laneType.equals("Special")) {
                        specialLane = new SpecialLane(pos);


                    } else {
                        Lane lane = new Lane(laneType, pos);
                        lanes[numLanes++] = lane;
                    }

                } else if (splitText[0].equals("Special")) {
                    // reading special notes for special lane
                    String type = splitText[1];
                    SpecialNote specialNote = new SpecialNote(type, Integer.parseInt(splitText[2]));
                    specialLane.addSpecialNote(specialNote);

                } else {
                    // reading notes for normal lanes
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
                            case "Bomb":
                                SpecialNote bombNote = new SpecialNote(splitText[1], Integer.parseInt(splitText[2]));
                                lane.addBombNote(bombNote);
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

    public void gameplay(Input input) {

        // gameplay

        SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

        // first update the factor we will mutyiply our scores by, this will change when double scores are active
        Notes.setScoreFactor();



        if (paused) {
            if (input.wasPressed(Keys.TAB)) {
                paused = false;

            }

            for (int i = 0; i < numLanes; i++) {
                lanes[i].draw();
            }

            specialLane.draw();

        } else {
            currFrame++;
            for (int i = 0; i < numLanes; i++) {
                score += lanes[i].update(input, accuracy, currFrame);
            }

            score += specialLane.update(input, accuracy, currFrame);

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
        specialLane.refresh();
        setCleared(false);
        Notes.refreshAllNotes();

    }
}
