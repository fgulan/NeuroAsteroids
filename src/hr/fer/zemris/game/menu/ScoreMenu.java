package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.Utils.HSDataUtility;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static hr.fer.zemris.game.Constants.*;
/**
 *
 * Created by doctor on 02.12.15..
 */
public class ScoreMenu extends Menu{

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
        TableView table = new TableView();
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
}
