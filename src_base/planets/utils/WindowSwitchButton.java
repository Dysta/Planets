package planets.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import planets.windows.Menu;

/**
 *
 * @author Adri
 */
public class WindowSwitchButton extends Button {
    
    public WindowSwitchButton(String text, Menu menu, int page) {
        super(text);

        this.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                menu.setSelectedWindow(page);
            }
        });
    }
}
