package hr.fer.zemris.sm.game.menu.menuUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        try (InputStream src = getClass().getClassLoader().getResourceAsStream(CREDITS_PATH))  {
            List<String> lines = new BufferedReader(new InputStreamReader(src, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

            for(String line : lines) {
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
            throw new RuntimeException(ex);
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

