package input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import planets.windows.Game;
import planets.entities.planet.Planet;

public class ScrollEventHandler implements EventHandler<ScrollEvent> {

    @Override
    public void handle(ScrollEvent e) {
        Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() + e.getDeltaY() / 4);
    }

}
