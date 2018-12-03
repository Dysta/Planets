package planets;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import static planets.Game.root;
import planets.entities.Galaxy;
import planets.utils.DebugUtils;

public class App extends Application {

    public final static boolean DEBUG = false;
    private final static double WIDTH = 1280;
    private final static double HEIGHT = 720;

    private final static int REFRESHRATE = 60;
    private final static int TICKRATE = 5;

    private static long last_tick;
    private static long last_frame;
    private static ArrayList<Long> ticks;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        App.last_tick = System.currentTimeMillis();
        App.last_frame = System.currentTimeMillis();

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

        App.ticks = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            App.ticks.add((long) 0);
        }

        new AnimationTimer() {
            public void handle(long arg0) {
                long now = System.currentTimeMillis();

                if (now - App.last_tick >= 1000 / App.TICKRATE) {
                    game.handle(arg0);
                    App.ticks.remove(0);
                    App.ticks.add(now);
                }

                if (now - App.last_frame >= 1000 / App.REFRESHRATE) {
                    App.last_frame = now;

                    if (App.DEBUG) {
                        System.out.println("-------------- Tick n°" + Game.ticks + " (tickrate: " + DebugUtils.tickRate(App.ticks) + ") --------------");
                        System.out.println("Nodes : " + DebugUtils.getAllNodes(root).size());
                    }
                    game.updateUI();
                }
            }
        }.start();
    }
}
