package ui;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets.entities.Player;

public class SelectPercentage {

	private GraphicsContext gc;
	private Group root;

	private double bottomLeftX;
	private double bottomLeftY;
	private double width;
	private double height;

	private Text text;
	private TextFlow tf;
	
	private Player p;
	
	public SelectPercentage(GraphicsContext gc, Group root, Player p, double bottomLeftX, double bottomLeftY) {
		this.text = new Text();
		this.tf = new TextFlow();
		this.p = p;
		this.gc = gc;
		this.root = root;
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
