package input;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import planets.windows.Game;

public class MouseReleaseHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent e) {
        if (!e.isPrimaryButtonDown()) {
            Game.primaryHeld = false;
        }
    }

}
