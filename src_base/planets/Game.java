package planets;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import planets.Entities.Galaxy;
import planets.Entities.Planet;
import planets.Entities.Route;
import ship.Ship;

public class Game {

    // Attributes
    private Stage stage;
    private GraphicsContext gc;
    private Canvas canvas;
    // Entities
    public static Galaxy galaxy;
    public static ArrayList<Route> routes;
    // Constants
    public static Group root;

    // States
    public static boolean primaryHeld;
    public static Planet selectedPlanet;

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
                                    if (Game.selectedPlanet != p) {
                                        if (Game.selectedPlanet == null) {
                                            // Select this planet for the next action
                                            Game.selectedPlanet = p;
                                        } else {
                                            // This is an friendly planet, send ships from the selected planet
                                            Game.startMission(Game.selectedPlanet, p, Game.selectedPlanet.getNbShip(), Route.CONVOY);
                                            Game.selectedPlanet = null;
                                        }
                                    } else {
                                        // Deselect this planet
                                        Game.selectedPlanet = null;
                                    }
                                } else {
                                    if (Game.selectedPlanet != null) {
                                        // This is an enemy planet, send ships from the selected planet
                                            Game.startMission(Game.selectedPlanet, p, Game.selectedPlanet.getNbShip(), Route.ATTACK);
                                            Game.selectedPlanet = null;
                                    } else {
                                        // No planet selected but clicked on enemy planet
                                    }
                                }
                            }
                        }

                        if (!clicked_a_planet) {
                            Game.selectedPlanet = null;
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

        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseReleaseHandler);
    }

    public void initGame(double width, double height) {
        // Galaxy(double width, double height, int nbPlanets, int nbPlayers, 
        // double planetInfluenceZone, double planetSecurityZone, 
        // double minimumPlanetSize, double maximumPlanetSize, double borderMargin)

        Galaxy galaxy = new Galaxy(width, height, 9, 2, 40, 50, 60, 110, 50);
        Game.routes = new ArrayList<>();
        Game.galaxy = galaxy;
        Game.primaryHeld = false;

        for (Planet p : Galaxy.getPlanets()) {
            p.initRender(Game.root);
        }
    }
    // Game instantiation 
    // Game behavior

    public static void startMission(Planet origin, Planet destination, int effectives, String mission) {
        ArrayList<Ship> ships = origin.flyShips(effectives);

        Route r = new Route(origin, destination, ships, mission);
        Game.routes.add(r);
    }

    public void handle(long arg0) {
        for (Planet p : Game.galaxy.getPlanets()) {
            //p.render(this.gc);
            for (Ship s : p.getShips()) {
                //s.render(this.gc);
                s.getImageView().setVisible(false);
            }
            p.productionTick();
            p.printStock(gc, root);
        }
        for (Route r : Game.routes) {
            r.move_ships();
        }
        Game.routes.removeIf((Route r) -> r.isEmpty());
    }

}
