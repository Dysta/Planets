package planets;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import planets.Entities.Galaxy;
import planets.Entities.Planet;
import planets.Entities.Mission;
import planets.utils.DebugUtils;
import ship.Ship;

public class Game {

    // Attributes
    private Stage stage;
    private GraphicsContext gc;
    private Canvas canvas;
    // Entities
    public static Galaxy galaxy;
    public static ArrayList<Mission> missions;
    // Constants
    public static Group root;

    // States
    public static boolean primaryHeld;
    public static boolean ctrlHeld;
    public static ArrayList<Planet> selectedPlanets;
    public static int ticks;

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

        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);

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
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                double mX = e.getX();
                double mY = e.getY();

                if (e.isPrimaryButtonDown()) {
                    // Mouse1 pressed

                    if (!Game.primaryHeld) {
                        Game.primaryHeld = true;
                        // Mouse1 unique click

                        boolean clicked_a_planet = false;
                        for (Planet p : Game.galaxy.getPlanets()) {
                            if (p.isOn(mX, mY)) {
                                clicked_a_planet = true;
                                if (p.getOwner().isMainPlayer()) {
                                    if (!Game.selectedPlanets.contains(p)) {
                                        if (Game.selectedPlanets.isEmpty()) {
                                            // Select this planet for the next action
                                            Game.selectedPlanets.add(p);
                                        } else {
                                            // This is an friendly planet
                                            if (e.isControlDown()) {
                                                // Select this planet too
                                                Game.selectedPlanets.add(p);
                                            } else {
                                                // Start a convoy
                                                for (Planet o : Game.selectedPlanets) {
                                                    Game.startMission(o, p, o.getNbShip(), 10, Mission.CONVOY);
                                                }
                                                Game.selectedPlanets.clear();
                                            }
                                        }
                                    } else {
                                        if (Game.selectedPlanets.size() == 1) {
                                            // Deselect a planet if clicked and already selected
                                            Game.selectedPlanets.remove(0);
                                        } else {
                                            // Keep only this one selected if others were selected
                                            Game.selectedPlanets.removeIf((Planet o) -> !o.equals(p));
                                        }
                                    }
                                } else {
                                    // Enemy planet
                                    if (!Game.selectedPlanets.isEmpty()) {
                                        // Send ships from the selected planets
                                        for (Planet o : Game.selectedPlanets) {
                                            Game.startMission(o, p, o.getNbShip(), 10, Mission.ATTACK);
                                        }
                                        Game.selectedPlanets.clear();
                                    } else {
                                        // No planet selected but clicked on enemy planet
                                    }
                                }
                            }
                        }

                        if (!clicked_a_planet) {
                            Game.selectedPlanets.clear();
                        }
                    }
                }
            }
        };
        EventHandler<MouseEvent> mouseReleaseHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (!e.isPrimaryButtonDown()) {
                    Game.primaryHeld = false;
                }

            }
        };

        EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                // TODO : Keyboard actions
            }
        };

        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseReleaseHandler);
    }

    public void initGame(double width, double height) {
        // Galaxy(double width, double height, int nbPlanets, int nbPlayers, 
        // double planetInfluenceZone, double planetSecurityZone, 
        // double minimumPlanetSize, double maximumPlanetSize, double borderMargin)

        Game.galaxy = new Galaxy(width, height, 9, 2, 40, 50, 60, 110, 50);
        Game.missions = new ArrayList<>();
        Game.selectedPlanets = new ArrayList<>();
        Game.primaryHeld = false;
        Game.ctrlHeld = false;

        for (Planet p : Galaxy.getPlanets()) {
            p.initRender(Game.root);
        }
    }
    // Game instantiation 
    // Game behavior

    public static void startMission(Planet origin, Planet destination, int effectives, int squadSize, String mission) {
        Mission r = new Mission(origin, destination, effectives, squadSize, mission);
        Game.missions.add(r);
    }

    public void handle(long arg0) {
        Game.ticks++;
        int tShips = 0;

        for (Mission r : Game.missions) {
            r.handle();
        }
        for (Planet p : Galaxy.getPlanets()) {
            //p.render(this.gc);
            for (Ship s : p.getShips()) {
                //s.render(this.gc);
            }
            p.productionTick();
            p.printStock(gc, root);
            tShips += p.getNbShip();
        }

        Game.missions.removeIf((Mission r) -> r.isEmpty());

        if (App.DEBUG) {
            System.out.println("-------------- Tick n°" + Game.ticks + " --------------");
            System.out.println("Planets : " + Galaxy.getPlanets().size());
            System.out.println("Ships : " + tShips);
            System.out.println("Nodes : " + DebugUtils.getAllNodes(root).size());
        }
    }

}
