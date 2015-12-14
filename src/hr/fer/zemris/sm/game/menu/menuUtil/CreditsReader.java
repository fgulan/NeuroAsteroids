package hr.fer.zemris.sm.game.menu.menuUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static hr.fer.zemris.sm.game.Constants.*;

/**
 * Created by Fredi Šarić on 14.12.15.
 */
public class CreditsReader {

    public static final String delimiter = ":";

    private static CreditsReader instance;

    private String projectLeader;
    private String projectMentor;
    private List<String> projectTeam;


    private CreditsReader(){
        projectTeam = new ArrayList<>();
        try {
            List<String> credits = Files.readAllLines(Paths.get(ClassLoader.getSystemResource(CREDITS_PATH).getPath()));

            for(String line : credits) {
                if(line.startsWith(CREDITS_LEADER)) {
                    projectLeader = line.trim().split(delimiter, 2)[1];
                }
                if(line.startsWith(CREDITS_MENTOR)) {
                    projectMentor = line.trim().split(delimiter, 2)[1];
                }
                if(line.startsWith(CREDITS_TEAM)) {
                    projectTeam.add(line.trim().split(delimiter, 2)[1]);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public static CreditsReader getInstance() {
        if(instance == null) {
            instance = new CreditsReader();
        }
        return instance;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public String getProjectMentor() {
        return projectMentor;
    }

    public List<String> getProjectTeam() {
        return projectTeam;
    }
}

