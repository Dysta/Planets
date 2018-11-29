package planets;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public final static boolean DEBUG = true;
    private final static double WIDTH = 1280;
    private final static double HEIGHT = 720;
    
    private final static double TICKRATE = 60;
    
    private static long last_tick;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        App.last_tick = System.currentTimeMillis();
        
        // Create Game and start it
        Game game = new Game();
        try {
            ResourcesManager.initGameAssets(WIDTH, HEIGHT);
        } catch (Exception e) {
            System.err.println("Xavier fait nimp: " + e);
        }
        game.setStage(primaryStage, "Planets");
        game.show(WIDTH, HEIGHT);
        game.initGame(WIDTH, HEIGHT);

        new AnimationTimer() {
            public void handle(long arg0) {
                long now = System.currentTimeMillis();
                
                if(now - App.last_tick >= 1 / App.TICKRATE) {
                    App.last_tick = now;
                    game.handle(arg0);
                }
            }
        }.start();
    }
}
