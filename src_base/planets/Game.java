package planets;

import java.util.ArrayList;

import input.MouseHandler;
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
import planets.entities.Galaxy;
import planets.entities.Planet;
import planets.entities.Mission;
import planets.entities.Squad;
import planets.utils.DebugUtils;
import planets.entities.ship.Ship;

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
    public static ArrayList<Squad> selectedSquads;
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
    	MouseHandler mouseHandler = new MouseHandler();
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
        double planetInfluenceZone = 40;
        double planetSecurityZone = 50;
        double minimumPlanetSize = 50;
        double maximumPlanetSize = 150;
        double borderMargin = 50;
        int nbPlanets = 10;
        int nbPlayers = 2;

        Game.galaxy = new Galaxy(width, height, nbPlanets, nbPlayers, planetInfluenceZone, planetSecurityZone, minimumPlanetSize, maximumPlanetSize, borderMargin);
        Game.missions = new ArrayList<>();
        Game.selectedPlanets = new ArrayList<>();
        Game.selectedSquads = new ArrayList<>();
        Game.primaryHeld = false;
        Game.ctrlHeld = false;

        for (Planet p : Galaxy.getPlanets()) {
            p.initRender();
        }
    }
    // Game instantiation 
    // Game behavior

    public static void startMission(Planet origin, Planet destination, int effectives, int squadSize, String mission) {
        Mission r = new Mission(origin, destination, effectives, squadSize, mission);
        Game.missions.add(r);
    }

    public static void changeMission(ArrayList<Squad> squads, Planet destination, String newOrder) {
        Mission r = new Mission(squads.get(0).getMission().getOriginPlanet(), destination, 0, 0, newOrder);

        for (Squad s : squads) {
            s.reaffectSquad(r);
            r.addQuad(s);
        }

        Game.missions.add(r);
    }

    public static void setSelect(ArrayList list, Sprite element, boolean state) {
        if (state) {
            list.add(element);
        } else {
            list.remove(element);
        }
        element.setSelected(state);
    }

    public static void setSelectSquad(Squad s, boolean state) {
        if (state) {
            Game.selectedSquads.add(s);
        } else {
            Game.selectedSquads.remove(s);
        }

        for (Ship sq : s.getShips()) {
            sq.setSelected(state);
        }
    }

    public void handle(long arg0) {
        Game.ticks++;

        for (Mission r : Game.missions) {
            r.handle();
        }
        for (Planet p : Galaxy.getPlanets()) {
            //p.render(this.gc);
            for (Ship s : p.getShips()) {
                //s.render(this.gc);
            }
            p.productionTick();
        }

        Game.missions.removeIf((Mission r) -> r.isEmpty());
    }
    
    public void updateUI() {
        for (Planet p : Galaxy.getPlanets()) {
            p.printStock(gc, root);
        }        
    }

}
