package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Utils.HSDataUtility;
import hr.fer.zemris.sm.game.Utils.ScoreElement;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;
/**
 *
 * Created by doctor on 02.12.15..
 */
public class ScoreMenu extends Menu{

    TableView table;

    public ScoreMenu(Game parent) {
        super(parent);
        setId(SCORE_MENU_ID);
        TableView table = createTable();

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setId(SCORE_MENU_BACK_BTN);
        back.setOnAction(e-> {
            parent.transition(this, parent.getStartMenu(), Game.RIGHT);
        });

        setCenter(table);
        setBottom(back);
    }

    private TableView createTable() {
        table = new TableView();
        table.setId(SCORE_MENU_TABLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn typeCol = new TableColumn(TYPE);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn nickCol = new TableColumn(SCORE_NICK);
        nickCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn scoreCol= new TableColumn(SCORE);
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        table.getColumns().addAll(typeCol, nickCol, scoreCol);

        table.getItems().addAll(HSDataUtility.getInstance().getHighScores());

        return table;
    }

    @Override
    public void reload() {
        table.getItems().clear();
        List<ScoreElement> scores = HSDataUtility.getInstance().getHighScores();
        scores.sort((o1, o2) -> (int)Math.signum(o2.getScore() - o1.getScore()));
        table.getItems().addAll(scores);
    }
}
