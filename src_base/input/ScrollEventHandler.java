package input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import planets.Game;
import planets.entities.Planet;

public class ScrollEventHandler implements EventHandler<ScrollEvent> {

	@Override
	public void handle(ScrollEvent e) {
		System.out.println(e.getDeltaY());
		Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() + e.getDeltaY()/4);
    }

}
