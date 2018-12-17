package input;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import planets.windows.Game;

/**
 * Handles any mouse release related event from the user related to the Application.
 * 
 * @author Adri
 */
public class MouseReleaseHandler implements EventHandler<MouseEvent> {

    
    /**
     * Deals with the provided MouseEvent when it's a release
     * @param e the MouseEvent provided by EventHandler
     */
    @Override
    public void handle(MouseEvent e) {
        if (!e.isPrimaryButtonDown()) {
            Game.primaryHeld = false;
        }
    }

}
