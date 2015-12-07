package hr.fer.zemris.game.menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static hr.fer.zemris.game.Constants.*;

/**
 * Created by doctor on 03.12.15..
 */
public class CreditsMenu extends Menu{

    public CreditsMenu(Game parent) {
        super(parent);

        Label credits;
        try {
            String info = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(CREDITS_PATH).getPath())));
            credits = new Label(info);
        } catch (IOException e) {
            credits = new Label("Error while loading credits");
        }
        credits.setId(CREDITS_LABEL);

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> parent.transition(this, parent.getStartMenu(), Game.RIGHT));

        VBox content = new VBox();
        content.setId(CREDITS_MENU_CONTENT);
        content.getChildren().addAll(credits, back);
        setCenter(content);
    }
}
