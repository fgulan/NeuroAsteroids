package hr.fer.zemris.sm.game.Utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hr.fer.zemris.sm.game.Constants.*;

public class HSDataUtility {
    private static HSDataUtility instance;

    private List<ScoreElement> highScoreList;

    private Path hsFile;

    private double min;

    private HSDataUtility() {
        min = Integer.MAX_VALUE;
        highScoreList = new ArrayList<>();
        try {
            getHighScoreFilePath();
            if (Files.exists(hsFile)) {

                //Loads all elements from file and sorts it
                highScoreList = Files.readAllLines(hsFile)
                        .stream()
                        .map(ScoreElement::parse)
                        .sorted(ScoreElement.BY_SCORE)
                        .collect(Collectors.toList());
                if (!highScoreList.isEmpty()) {
                    min = highScoreList.get(highScoreList.size() - 1).getScore();
                }

            } else {
                Files.createFile(hsFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HSDataUtility getInstance() {
        if (instance == null) {
            instance = new HSDataUtility();
        }
        return instance;
    }

    public List<ScoreElement> getHighScores() {
        return highScoreList;
    }

    public void flush() {
        StringBuilder sb = new StringBuilder();
        for (ScoreElement se : highScoreList) {
            sb.append(se.toString())
                    .append(System.lineSeparator());
        }

        try {
                Files.write(hsFile, sb.toString().getBytes());
        } catch (Exception e) {
            //hs.txt file could not be saved
            throw new RuntimeException(e);
        }
    }

    public boolean isHighScore(int score) {
        if (highScoreList.size() < MAX_HIGH_SCORE_LIST_SIZE) {
            return true;
        }
        return score > min;
    }

    public void addNewHighScore(ScoreElement el) {
        if (highScoreList.size() < MAX_HIGH_SCORE_LIST_SIZE) {
            highScoreList.add(el);
        } else if (el.getScore() > min) {
            highScoreList.add(el);
            highScoreList.sort(ScoreElement.BY_SCORE);
            highScoreList.remove(highScoreList.size() - 1);
            min = highScoreList.get(highScoreList.size() - 1).getScore();
        }
    }

    public void getHighScoreFilePath() {
        hsFile = Paths.get(System.getProperty("java.class.path")).getParent().resolve(HIGH_SCORE_FILE);
    }
}
