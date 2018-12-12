package input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import planets.App;
import planets.IO.SaveManager;
import planets.windows.Game;
import planets.entities.planet.Planet;
import planets.windows.Window;

public class KeyEventHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent e) {

        if (e.isControlDown() && e.getCode() == KeyCode.A) {
            for (Planet p : Game.selectedPlanets) {
                p.setSelected(false);
            }
            Game.selectedPlanets.clear();
            for (Planet p : Game.galaxy.getPlanets()) {
                if (p.getOwner().isMainPlayer()) {
                    Game.setSelect(Game.selectedPlanets, p, true);
                }
            }
        }

        if (e.isControlDown() && e.getCode() == KeyCode.S) {
            SaveManager.save(App.game, "Quicksave");
        }

        if (e.isControlDown() && e.getCode() == KeyCode.D) {
            try {
                SaveManager.load(App.game, "Quicksave");
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        if (e.getCode() == KeyCode.UP) {
            Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() + 10);
        }
        if (e.getCode() == KeyCode.DOWN) {
            Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() - 10);
        }

        if (e.getCode() == KeyCode.ESCAPE) {
            App.menu.setSelectedWindow(Window.MAIN_MENU);
        }
    }

}
