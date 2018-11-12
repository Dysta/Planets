package planets;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	private final static double WIDTH = 1280;
	private final static double HEIGHT = 720;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Create Game and start it
		Game game = new Game();
		
		game.setStage(primaryStage, "Planets");
		game.show(WIDTH, HEIGHT);
	}
}
