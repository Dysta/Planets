package planets.windows;

import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import planets.Planets;
import planets.ResourcesManager;

/**
 *
 * @author Adri
 */
public class ResultScreen extends Window {

    @Override
    public void init(double WIDTH, double HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        Game.root = new Group();
        Scene scene = new Scene(root);
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

        // Events
        gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        gc.setFill(Color.BISQUE);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.drawImage(ResourcesManager.gameOverBackground, 0, 0);

        scene.setOnKeyPressed((KeyEvent event) -> {
            // ESCAPE -> Return to the main menu
            if (event.getCode() == KeyCode.ESCAPE) {
                Planets.menu.setSelectedWindow(Window.MAIN_MENU);
            }
        });
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}