package input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import planets.Game;
import planets.entities.Planet;

public class KeyEventHandler implements EventHandler<KeyEvent> {

	@Override
	public void handle(KeyEvent e) {

		if(e.isControlDown() && e.getCode() == KeyCode.A) {
			for(Planet p : Game.selectedPlanets) {
				p.setSelected(false);
			}
			Game.selectedPlanets.clear();
			for(Planet p : Game.galaxy.getPlanets()) {
				if(p.getOwner().isMainPlayer()) {
					Game.setSelect(Game.selectedPlanets, p, true);
				}
			}
		}
    }

}
