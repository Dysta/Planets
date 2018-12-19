package planets;

import planets.windows.Menu;
import planets.windows.Game;
import planets.windows.Window;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import static planets.windows.Game.root;

import planets.utils.DebugUtils;
import planets.windows.Load;
import planets.windows.ResultScreen;

/**
 * The main class for the application.
 * Handles screens and periodic calls
 * 
 * @author Adri and Dysta
 */
public class Planets extends Application {

    /**
     * Can only be changed in the source code, enables more logs.
     */
    public final static boolean DEBUG = false;

    /**
     * The allowed width for this application.
     */
    private final static double WIDTH = 1280;

    /**
     * The allowed height for this application.
     */
    private final static double HEIGHT = 720;

    /**
     * The preffered width for this application.
     */
    private final static double MENU_WIDTH = 600;

    /**
     * The preffered height for this application.
     */
    private final static double MENU_HEIGHT = 400;

    /**
     * How many frames should be processed every second.
     */
    private final static int REFRESHRATE = 60;

    /**
     * How many game ticks should be processed every second.
     */
    private final static int TICKRATE = 60;

    /**
     * How many times per second are AIs able to "think".
     */
    private final static int AI_THINKRATE = 2;

    /**
     * Stores the last game tick, used for respecting TICKRATE.
     */
    private static long last_tick;

    /**
     * Stores the last processed frame, used for respecting REFRESHRATE.
     */
    private static long last_frame;

    /**
     * Stores the last AIs process turn, used for respecting AI_THINKRATE.
     */
    private static long last_think;
    
    /**
     * Stores ticks to compute the tickrate, used in logs.
     */
    private static ArrayList<Long> ticks;

    /**
     * The main Stage, containing displayed elements.
     */
    public static Stage stage;

    /**
     * The Menu window : Offers to start a new game, see the Load menu, and quit.
     */
    public static Menu menu;

    /**
     * The Game window.
     */
    public static Game game;

    /**
     * The Load window : Lists the savegames to load.
     */
    public static Load load;

    /**
     * The ResultScreen window : Printed when the game has ended.
     */
    public static ResultScreen resultScreen;

    /**
     * Starts the application.
     * 
     * @param args Default console arguments passed to Application.launch
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates the initial game state and initializes basic assets.
     * 
     * @param primaryStage This argument is provided by the Application superclass
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        menu = new Menu();
        load = new Load(menu);

        try {
            ResourcesManager.initGlobalAssets(MENU_WIDTH, MENU_HEIGHT);
        } catch (Exception e) {
            System.err.println("Failed to load ResourcesManager GlobalAssets: " + e);
        }

        Planets.last_tick = System.currentTimeMillis();
        Planets.last_frame = System.currentTimeMillis();

        startMenu();
    }

    /**
     * Creates the Main Menu and starts a timer whose goal is to handle Window switching.
     */
    public static void startMenu() {
        menu.setStage(stage, "Main Menu");
        menu.init(MENU_WIDTH, MENU_HEIGHT);

        AnimationTimer menu_anim = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                int selectedMenu;

                selectedMenu = menu.getSelectedWindow();
                menu.setSelectedWindow(Window.STANDBY);
                switch (selectedMenu) {
                    case Window.MAIN_MENU:
                        if(game!=null) {
                        	game.clear();
                        }
                        menu.setStage(stage, "Main Menu");
                        menu.init(MENU_WIDTH, MENU_HEIGHT);
                        menu.stage.centerOnScreen();
                        menu.show();
                        break;
                    case Window.GAME:
                        int nbPlayers = menu.getNbPlayers();
                        int nbPlanets = menu.getNbPlanets();

                        if (nbPlayers >= 1 && nbPlayers <= 10 && nbPlanets >= nbPlayers && nbPlanets <= 60) {
                            menu.clear();
                            Planets.startGame(menu.getNbPlayers(), menu.getNbPlanets());
                            Game.setFreeze(false);
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
                        break;
                    case Window.RESULT_SCREEN:
                        if(game!=null) {
                        	game.clear();
                        }
                        resultScreen = new ResultScreen();
                        resultScreen.setStage(stage, "Game Over !");
                        resultScreen.init(MENU_WIDTH, MENU_HEIGHT);
                        resultScreen.stage.centerOnScreen();
                        resultScreen.show();
                        
                        break;
                    case Window.QUIT:
                        System.exit(0);
                        break;
                }

            }
        };
        menu_anim.start();
    }

    /**
     * Creates a random game and starts the global Game handler.
     * 
     * @param nbPlayers The maximum amount of players to assign a planet too
     * @param nbPlanets The maximum amount of planets to generate
     */
    public static void startGame(int nbPlayers, int nbPlanets) {
        try {
            ResourcesManager.initGameAssets(WIDTH, HEIGHT);
        } catch (Exception e) {
            System.err.println("Failed to load ResourcesManager GameAssets: " + e);
        }

        // Create Game and start it
        game = new Game(nbPlayers, nbPlanets);

        game.setStage(stage, "Planets");
        game.init(WIDTH, HEIGHT);

        Planets.ticks = new ArrayList<>();
        for (int i = 60; i > 0; i--) {
            Planets.ticks.add((long) 0);
        }

        AnimationTimer game_anim = new AnimationTimer() {
            @Override
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
