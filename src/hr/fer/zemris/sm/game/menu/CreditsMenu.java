package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.menu.menuUtil.CreditsReader;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class CreditsMenu extends Menu{

    public CreditsMenu(Game parent) {
        super(parent);
        GridPane pane = new GridPane();
        CreditsReader reader = CreditsReader.getInstance();

        Label projectNameLabel = new Label(CREDITS_PROJECT_NAME_LABEL_TEXT);
        projectNameLabel.setId(CREDITS_PROJECT_NAME_LABEL);
        pane.add(projectNameLabel, 0, 0, 2, 1); //Whole firs row


        Label leaderLabel = new Label(CREDITS_LEADER_LABLE_TEXT);
        Label leader      = new Label(reader.getProjectLeader());
        pane.addRow(1, leaderLabel, leader);

        Label mentorLabel = new Label(CREDITS_MENTOR_LABEL_TEXT);
        Label mentor      = new Label(reader.getProjectMentor());
        pane.addRow(2, mentorLabel, mentor);

        Label teamLabel   = new Label(CREDITS_TEAM_LABEL_TEXT);
        pane.add(teamLabel, 3,0);

        List<String> team = reader.getProjectTeam();

        for(int i = 0; i < team.size(); i++) {
            pane.add(new Label(team.get(i)), i + 3, 1);
        }

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> {
            getGameParent().transition(this, getGameParent().getStartMenu(), Game.RIGHT);
        });

        setCenter(pane);
    }

    @Override
    public void relaod() {
        //No implementation
    }
}
