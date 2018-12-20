package planets.ui;

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

    /**
     * return the x position
     * @return x position
     */
    public double getPosX() {
        return this.bottomLeftX - this.width;
    }

    /**
     * return the y position
     * @return y position
     */
    public double getPosY() {
        return this.bottomLeftY - this.height;
    }

    /**
     * return the middle of x position
     * @return middle x position
     */
    public double getPosXMiddle() {
        return this.getPosX() + (this.width / 2);
    }
    
    /**
     * return the middle of y position
     * @return middle y position
     */
    public double getPosYMiddle() {
        return this.getPosY() + (this.height / 2);
    }
}
