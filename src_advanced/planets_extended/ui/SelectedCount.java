package planets_extended.ui;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets_extended.Planets;
import planets_extended.entities.Player;
import planets_extended.entities.planet.Planet;
import planets_extended.windows.Game;

/**
 * UI element displaying the current amount of ships that have been selected
 *
 * @author Adri
 */
public class SelectedCount {

    private final GraphicsContext gc;
    private final Group root;

    private final double posX;
    private final double posY;
    private double width;
    private double height;

    private final Text text;
    private final TextFlow tf;

    public SelectedCount(double posX, double posY) {
        this.text = new Text();
        this.tf = new TextFlow();
        this.gc = Planets.game.gc;
        this.root = Game.root;
        this.posX = posX;
        this.posY = posY;
        this.update();
    }

    public void update() {
        text.setFont(gc.getFont());
        text.setFill(Color.WHITE);

        int total = 0;
        double power = 0;
        for (Planet pl : Game.selectedPlanets) {
            total += pl.getNbShip() * Game.mainPlayer.getEffectivesPercent()/100;
            power += pl.getPower() * Game.mainPlayer.getEffectivesPercent()/100;
        }

        text.setText("Selected : "+total+" (est. power: "+Math.round(power)+")");

        tf.setLayoutX(getPosXMiddle());
        tf.setLayoutY(getPosYMiddle());

        tf.getChildren().remove(text);
        root.getChildren().remove(tf);
        tf.getChildren().add(text);
        root.getChildren().add(tf);
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public double getPosXMiddle() {
        return this.getPosX() + (this.width / 2);
    }

    public double getPosYMiddle() {
        return this.getPosY() + (this.height / 2);
    }
}
