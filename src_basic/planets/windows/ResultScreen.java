package planets.windows;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import planets.Planets;
import planets.ResourcesManager;

/**
 * Displays the end of the game
 *
 * @author Adri
 */
public class ResultScreen extends Window {

    @Override
    public void initAfter() {
        scene.setOnKeyPressed((KeyEvent event) -> {
            // ESCAPE -> Return to the main menu
            if (event.getCode() == KeyCode.ESCAPE) {
                Planets.menu.setSelectedWindow(Window.MAIN_MENU);
            }
        });
    }

    @Override
    public void setBackground() {
        this.background = ResourcesManager.getImageAsset("resultScreenBackground", "images/game-over.png", WIDTH, HEIGHT);
    }
}
