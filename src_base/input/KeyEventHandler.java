package input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import planets.Planets;
import planets.IO.SaveManager;
import planets.entities.Galaxy;
import planets.windows.Game;
import planets.entities.planet.Planet;
import planets.windows.Window;

/**
 * Handles any key related event from the user related to the Application.
 * 
 * @author Adri
 */
public class KeyEventHandler implements EventHandler<KeyEvent> {

    /**
     * Deals with the provided KeyEvent
     * @param e the KeyEvent provided by EventHandler
     */
    @Override
    public void handle(KeyEvent e) {

        // CTRL + A -> Select all planets
        if (e.isControlDown() && e.getCode() == KeyCode.A) {
            for (Planet p : Game.selectedPlanets) {
                p.setSelected(false);
            }
            Game.selectedPlanets.clear();
            for (Planet p : Galaxy.getPlanets()) {
                if (p.getOwner().isMainPlayer()) {
                    Game.setSelect(Game.selectedPlanets, p, true);
                }
            }
        }

        // CTRL + S -> Save the game to Quicksave.planets
        if (e.isControlDown() && e.getCode() == KeyCode.S) {
            SaveManager.save(Planets.game, "Quicksave");
        }

        // CTRL + D -> Load a game from Quicksave.planets
        if (e.isControlDown() && e.getCode() == KeyCode.D) {
            try {
                SaveManager.load("Quicksave");
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        // Debug
        if (e.getCode() == KeyCode.F) {
            Game.toggleFreeze();
        }

        // UP ARROW -> Increase the percentage of ships per mission
        if (e.getCode() == KeyCode.UP) {
            Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() + 10);
        }
        // DOWN ARROW -> Decrease the percentage of ships per mission
        if (e.getCode() == KeyCode.DOWN) {
            Game.mainPlayer.setEffectivesPercent(Game.mainPlayer.getEffectivesPercent() - 10);
        }

        // ESCAPE -> Return to the main menu
        if (e.getCode() == KeyCode.ESCAPE) {
            Planets.menu.setSelectedWindow(Window.MAIN_MENU);
        }
    }

}
