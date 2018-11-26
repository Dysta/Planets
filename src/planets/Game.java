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
import planets.Entities.Planet;
import ship.Ship;

public class Game {
	
	// Attributes
	private Stage stage;
	private GraphicsContext gc;
	private Canvas canvas;
	// Entities
	private Galaxy galaxy;
	// Constants
	public static Group root;
	
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
		canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);
		
		// Events
		
		gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		gc.setFill(Color.BISQUE);
		gc.setStroke(Color.RED);
		gc.setLineWidth(1);
		
		gc.drawImage(ResourcesManager.background, 0, 0);

		Game.root = root;
		stage.setScene(scene);
		this.initEvents(scene);
		stage.show();
	}
	
	private void initEvents(Scene scene) {
		
	}
	
	public void initGame(double width, double height) {
		Galaxy galaxy = new Galaxy(width, height, 5, 2, 40, 80, 140, 50);
		this.galaxy = galaxy;

		for(Planet p : this.galaxy.getPlanets()) {
			p.initRender(Game.root);
		}
	}
	// Game instantiation 
	// Game behavior
	
	public void handle(long arg0) {
		for(Planet p : this.galaxy.getPlanets()) {
			//p.render(this.gc);
			for(Ship s : p.getShips()) {
				//s.render(this.gc);
			}
			p.productionTick();
		}
	}
	
}
