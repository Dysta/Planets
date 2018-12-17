package ui;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets.Planets;
import planets.entities.Player;
import planets.windows.Game;

/**
 * UI element displaying the current percentage of sent ships for each mission.
 * 
 * @author Adri
 */
public class SelectPercentage {

	private final GraphicsContext gc;
	private final Group root;

	private final double bottomLeftX;
	private final double bottomLeftY;
	private double width;
	private double height;

	private final Text text;
	private final TextFlow tf;
	
	private final Player p;
	
	public SelectPercentage(double bottomLeftX, double bottomLeftY) {
		this.text = new Text();
		this.tf = new TextFlow();
		this.p = Game.mainPlayer;
		this.gc = Planets.game.gc;
		this.root = Game.root;
		this.bottomLeftX = bottomLeftX;
		this.bottomLeftY = bottomLeftY;
		this.update();
	}
	
    public void update() {
        text.setFont(gc.getFont());
        text.setFill(Color.WHITE);
        text.setText(((int) p.getEffectivesPercent()) + " %");

        width = text.getLayoutBounds().getWidth();
        height = text.getLayoutBounds().getHeight();
        tf.setLayoutX(getPosXMiddle() - width / 2);
        tf.setLayoutY(getPosYMiddle() - height / 2);

        tf.getChildren().remove(text);
        root.getChildren().remove(tf);
        tf.getChildren().add(text);
        root.getChildren().add(tf);
    }


    public double getPosX() {
        return this.bottomLeftX - this.width;
    }

    public double getPosY() {
        return this.bottomLeftY - this.height;
    }

    public double getPosXMiddle() {
        return this.getPosX() + (this.width / 2);
    }

    public double getPosYMiddle() {
        return this.getPosY() + (this.height / 2);
    }
}
