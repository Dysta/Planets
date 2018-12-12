package planets;

import planets.windows.Menu;
import planets.windows.Game;
import planets.windows.Window;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import static planets.windows.Game.root;
import planets.entities.Galaxy;
import planets.utils.DebugUtils;
import planets.windows.Load;

public class App extends Application {

    public final static boolean DEBUG = false;

    private final static double WIDTH = 1280;
    private final static double HEIGHT = 720;

    private final static double MENU_WIDTH = 600;
    private final static double MENU_HEIGHT = 400;

    private final static int REFRESHRATE = 60;
    private final static int TICKRATE = 60;
    private final static int AI_THINKRATE = 2;

    private static long last_tick;
    private static long last_frame;
    private static long last_think;
    private static ArrayList<Long> ticks;

    public static Menu menu;
    public static Game game;
    public static Load load;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        menu = new Menu();
        load = new Load(menu);

        try {
            ResourcesManager.initMenuAssets(WIDTH, HEIGHT);
        } catch (Exception e) {
            System.err.println("Failed to load ResourcesManager MenuAssets: " + e);
        }

        App.last_tick = System.currentTimeMillis();
        App.last_frame = System.currentTimeMillis();

        startMenu(primaryStage);
    }

    public static void startMenu(Stage stage) {
        menu.setStage(stage, "Main Menu");
        menu.init(MENU_WIDTH, MENU_HEIGHT);

        AnimationTimer menu_anim = new AnimationTimer() {
            public void handle(long arg0) {
                int selectedMenu;

                selectedMenu = menu.getSelectedWindow();
                menu.setSelectedWindow(Window.STANDBY);
                switch (selectedMenu) {
                    case Window.MAIN_MENU:
                        if (game != null) {
                            game.clear();
                        }
                        if (load != null) {
                            load.clear();
                        }
                        menu.setStage(stage, "Main Menu");
                        menu.init(MENU_WIDTH, MENU_HEIGHT);
                        menu.show();
                        break;
                    case Window.GAME:
                        int nbPlayers = menu.getNbPlayers();
                        int nbPlanets = menu.getNbPlanets();

                        if (nbPlayers >= 1 && nbPlanets >= nbPlayers) {
                            menu.clear();
                            App.startGame(stage, menu.getNbPlayers(), menu.getNbPlanets());
                        }
                        break;
                    case Window.LOAD:
                        load = new Load(menu);
                        menu.clear();
                        load.clear();
                        load.setStage(stage, "Load save");
                        load.init(MENU_WIDTH, MENU_HEIGHT);
                        break;
                    case Window.LOADING:
                        try {
                            ResourcesManager.initGameAssets(WIDTH, HEIGHT);
                        } catch (Exception e) {
                            System.err.println("Failed to load ResourcesManager MenuAssets: " + e);
                        }

                        Game loaded = game;
                        App.startGame(stage, menu.getNbPlayers(), menu.getNbPlanets());
                        load.close();
                        App.game = loaded;
                        break;
                    case Window.QUIT:
                        System.exit(0);
                        break;
                }

            }
        };
        menu_anim.start();
    }

    public static void startGame(Stage stage, int nbPlayers, int nbPlanets) {
        try {
            ResourcesManager.initGameAssets(WIDTH, HEIGHT);
        } catch (Exception e) {
            System.err.println("Failed to load ResourcesManager GameAssets: " + e);
        }

        // Create Game and start it
        game = new Game();

        game.setStage(stage, "Planets");
        game.init(WIDTH, HEIGHT);
        game.initGame(nbPlayers, nbPlanets);

        App.ticks = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            App.ticks.add((long) 0);
        }

        AnimationTimer game_anim = new AnimationTimer() {
            public void handle(long arg0) {
                long now = System.currentTimeMillis();

                if (now - App.last_tick >= 1000 / App.TICKRATE) {
                    App.last_tick = now;
                    game.handle(arg0);
                    App.ticks.remove(0);
                    App.ticks.add(now);
                }

                if (now - App.last_think >= 1000 / App.AI_THINKRATE) {
                    App.last_think = now;
                    game.handleAI();
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
        };
        game_anim.start();
    }
}
