import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This class represents Level 3 of Shadow Dance.
 * Level 3 has the same features as level 1 and 2, with the addition of entities such as a guardian and enemies. To
 * win, the player must beat a target score of 350 when all notes have fallen.
 */
public class Level3 extends Levels {
    private final static String CSV_FILE = "res/level3-60.csv";
    private final Lane[] lanes = new Lane[4];
    private SpecialLane specialLane;
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private final Accuracy accuracy = new Accuracy();
    private boolean paused = false;
    private final int CLEAR_SCORE = 350;
    // level 3 has a single guardian and can have multiple enemies
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final Guardian guardian = new Guardian();


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

        // every 600 frames, we create another enemy
        if (currFrame % 600 == 0) {
            Enemy enemy = new Enemy();
            enemies.add(enemy);
        }

        // drawing the guardian
        guardian.draw(input.wasPressed(Keys.LEFT_SHIFT), enemies);

        // check through each enemy, show the active ones, and check for collission with arrow
        for (int currEnemy = 0; currEnemy < enemies.size(); currEnemy++) {
            enemies.get(currEnemy).update();
            enemies.get(currEnemy).checkCollision(guardian.getProjectiles());
        }



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
                // check if an enemy has stolen the note and also update the score
                lanes[i].checkEnemyCollision(enemies);
                score += lanes[i].update(input, accuracy, currFrame);
            }
            // same for special lane and its notes
            specialLane.checkEnemyCollision(enemies);
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
        enemies.clear();
        guardian.refresh();

    }
}
