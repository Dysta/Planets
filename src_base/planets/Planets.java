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

/**
 * 
 * @author Adri & Dysta
 */
public class Planets extends Application {

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

    public static Stage stage;
    public static Menu menu;
    public static Game game;
    public static Load load;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        menu = new Menu();
        load = new Load(menu);

        try {
            ResourcesManager.initMenuAssets(WIDTH, HEIGHT);
        } catch (Exception e) {
            System.err.println("Failed to load ResourcesManager MenuAssets: " + e);
        }

        Planets.last_tick = System.currentTimeMillis();
        Planets.last_frame = System.currentTimeMillis();

        startMenu();
    }

    public static void startMenu() {
        menu.setStage(stage, "Main Menu");
        menu.init(MENU_WIDTH, MENU_HEIGHT);

        AnimationTimer menu_anim = new AnimationTimer() {
            public void handle(long arg0) {
                int selectedMenu;

                selectedMenu = menu.getSelectedWindow();
                menu.setSelectedWindow(Window.STANDBY);
                switch (selectedMenu) {
                    case Window.MAIN_MENU:
                        menu.setStage(stage, "Main Menu");
                        menu.init(MENU_WIDTH, MENU_HEIGHT);
                        menu.stage.centerOnScreen();
                        menu.show();
                        break;
                    case Window.GAME:
                        int nbPlayers = menu.getNbPlayers();
                        int nbPlanets = menu.getNbPlanets();

                        if (nbPlayers >= 1 && nbPlanets >= nbPlayers) {
                            menu.clear();
                            Planets.startGame(menu.getNbPlayers(), menu.getNbPlanets());
                        }
                        break;
                    case Window.LOAD:
                        load = new Load(menu);
                        load.setStage(stage, "Load save");
                        load.init(MENU_WIDTH, MENU_HEIGHT);
                        load.stage.centerOnScreen();
                        break;
                    case Window.LOADING:
                        try {
                            ResourcesManager.initGameAssets(WIDTH, HEIGHT);
                        } catch (Exception e) {
                            System.err.println("Failed to load ResourcesManager MenuAssets: " + e);
                        }
                        Planets.startGame(menu.getNbPlayers(), menu.getNbPlanets());
                        break;
                    case Window.QUIT:
                        System.exit(0);
                        break;
                }

            }
        };
        menu_anim.start();
    }

    public static void startGame(int nbPlayers, int nbPlanets) {
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

        Planets.ticks = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            Planets.ticks.add((long) 0);
        }

        AnimationTimer game_anim = new AnimationTimer() {
            public void handle(long arg0) {
                long now = System.currentTimeMillis();

                if (now - Planets.last_tick >= 1000 / Planets.TICKRATE) {
                    Planets.last_tick = now;
                    game.handle(arg0);
                    Planets.ticks.remove(0);
                    Planets.ticks.add(now);
                }

                if (now - Planets.last_think >= 1000 / Planets.AI_THINKRATE) {
                    Planets.last_think = now;
                    game.handleAI();
                }

                if (now - Planets.last_frame >= 1000 / Planets.REFRESHRATE) {
                    Planets.last_frame = now;

                    if (Planets.DEBUG) {
                        System.out.println("-------------- Tick n°" + Game.ticks + " (tickrate: " + DebugUtils.tickRate(Planets.ticks) + ") --------------");
                        System.out.println("Nodes : " + DebugUtils.getAllNodes(root).size());
                    }
                    game.updateUI();
                }
            }
        };
        game_anim.start();
    }
}
