package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.menu.menuUtil.CreditsReader;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.nio.file.Paths;
import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class CreditsMenu extends Menu{

    public CreditsMenu(Game parent) {
        super(parent);
        VBox box = new VBox();
        box.setId(CREDITS_CONTENT_VBOX);

        GridPane pane = new GridPane();
        pane.setId(CREDITS_CONTENT_GRID);
        CreditsReader reader = CreditsReader.getInstance();

        ImageView title = new ImageView(Paths.get(ClassLoader.getSystemResource(Constants.GAME_TITLE).toExternalForm()).toString());
        title.setId(Constants.CREDITS_PROJECT_NAME_LABEL);
        box.getChildren().add(title); //Whole firs row

        Label leaderLabel = new Label(CREDITS_LEADER_LABEL_TEXT);
        Label leader      = new Label(reader.getProjectLeader());
        pane.addRow(0, leaderLabel, leader);

        Label mentorLabel = new Label(CREDITS_MENTOR_LABEL_TEXT);
        Label mentor      = new Label(reader.getProjectMentor());
        pane.addRow(1, mentorLabel, mentor);

        pane.add(new Separator() , 0, 2, 2, 1);

        Label teamLabel   = new Label(CREDITS_TEAM_LABEL_TEXT);
        pane.add(teamLabel, 0, 3);

        List<String> team = reader.getProjectTeam();

        for(int i = 0; i < team.size(); i++) {
            pane.add(new Label(team.get(i)), 1, i + 3);
        }

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setId(CREDITS_BACK_BUTTON);
        back.setOnAction(e -> {
            getGameParent().transition(this, getGameParent().getStartMenu(), Game.RIGHT);
        });

        box.getChildren().add(pane);
        box.setFillWidth(false);
        setCenter(box);
        setBottom(back);
    }

    @Override
    public void relaod() {
        //No implementation
    }
}
