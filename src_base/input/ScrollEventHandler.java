package input;

import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import planets.windows.Game;

/**
 * Handles any scroll related event from the user related to the Application.
 * 
 * @author Adri
 */
public class ScrollEventHandler implements EventHandler<ScrollEvent> {

    /**
     * Deals with the provided ScrollEvent
     * @param e the ScrollEvent provided by EventHandler
     */
    @Override
    public void handle(ScrollEvent e) {
        Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() + e.getDeltaY() / 4);
    }

}
