package planets;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import planets.Entities.Galaxy;

public class Game {
	
	// Attributes
	private Stage stage;
	private GraphicsContext gc;
	// Entities
	private Galaxy galaxy;
	// Constants
	
	// Methods
	
	// Stage options are defined by the game
	public void setStage(Stage stage_p, String title) {
		this.stage = stage_p;
		stage.setTitle(title);
		stage.setResizable(false);
	}

	// GraphicsContext is defined by the game
	public void show(double WIDTH, double HEIGHT) {
		Group root = new Group();
		Scene scene = new Scene(root);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
		
		gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		gc.setFill(Color.BISQUE);
		gc.setStroke(Color.RED);
		gc.setLineWidth(1);

		stage.setScene(scene);
		stage.show();
	}
	// Game instantiation 
	// Game behavior
	
	public void handle(long arg0) {
		
	}
	
}
