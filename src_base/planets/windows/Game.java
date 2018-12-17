package planets.windows;

import java.util.ArrayList;

import input.KeyEventHandler;
import input.MouseHandler;
import input.MouseReleaseHandler;
import input.ScrollEventHandler;
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
import planets.Planets;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Galaxy;
import planets.entities.planet.Planet;
import planets.entities.Player;
import planets.entities.Mission;
import planets.entities.Squad;
import planets.utils.DebugUtils;
import ui.SelectPercentage;
import planets.entities.ship.Ship;

public class Game extends Window {

    // Entities
    public static Galaxy galaxy;
    public static ArrayList<Mission> missions;
    public static Player mainPlayer;

    // States
    public static boolean primaryHeld;
    public static boolean ctrlHeld;
    public static boolean freeze = false;
    public static ArrayList<Planet> selectedPlanets;
    public static ArrayList<Squad> selectedSquads;
    public static int ticks;

    // UI Elements
    public static SelectPercentage selectP;

    // Methods
    @Override
    public void init(double WIDTH, double HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        Game.root = new Group();
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

        stage.setScene(scene);
        this.initEvents(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void initEvents(Scene scene) {
        MouseHandler mouseHandler = new MouseHandler();
        MouseReleaseHandler mouseReleaseHandler = new MouseReleaseHandler();
        KeyEventHandler keyEventHandler = new KeyEventHandler();
        ScrollEventHandler scrollEventHandler = new ScrollEventHandler();

        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseReleaseHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnScroll(scrollEventHandler);
    }

    public void initGame(int nbPlayers, int nbPlanets) {
        double borderMargin = 50;

        Game.missions = new ArrayList<>();
        Game.galaxy = new Galaxy(this.WIDTH, this.HEIGHT, nbPlanets, nbPlayers, borderMargin);
        for (Planet p : Game.galaxy.getPlanets()) {
            if (p.getOwner().isMainPlayer()) {
                Game.mainPlayer = p.getOwner();
            }
        }
        Game.selectedPlanets = new ArrayList<>();
        Game.selectedSquads = new ArrayList<>();
        Game.primaryHeld = false;
        Game.ctrlHeld = false;

        // init UI
        Game.selectP = new SelectPercentage(this.WIDTH, this.HEIGHT);

        for (Planet p : Galaxy.getPlanets()) {
            p.initRender();
        }
    }

    public void load(Galaxy galaxy, ArrayList<Mission> missions) {
        Game.root.getChildren().clear();
        Game.galaxy = galaxy;
        Game.missions = missions;

        this.init(WIDTH, HEIGHT);

        for (Planet p : Galaxy.getPlanets()) {
            p.initRender();
        }

        for (Mission m : Game.missions) {
            for (Squad s : m.getSquads()) {
                for (Ship sh : s.getShips()) {
                    sh.initRender();
                    ResourcesManager.colorImage(sh, m.getOwner().getColor());
                }
            }
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

    public static void deselect(ArrayList list) {
        int c = 0;
        int t = list.size();
        while (c < t) {
            Game.setSelect(list, (Sprite) list.get(0), false);
            c++;
        }
    }

    public static void setFreeze(boolean b) {
        Game.freeze = b;
    }

    public static void toggleFreeze() {
    	setFreeze(!Game.freeze);
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

    public static void deselectSquads() {
        int c = 0;
        int t = Game.selectedSquads.size();
        while (c < t) {
            Game.setSelectSquad(Game.selectedSquads.get(0), false);
            c++;
        }
    }

    public static void startConvoy(Planet target) {
        for (Planet o : Game.selectedPlanets) {
            Game.startMission(o, target, (int) (o.getNbShip() * (o.getOwner().getEffectivesPercent() / 100)), o.getMaxLaunchShips(), Mission.CONVOY);
        }
        Game.deselect(Game.selectedPlanets);
    }

    public static void startAttack(Planet o, Planet target) {
        Game.startMission(o, target, (int) (o.getNbShip() * (o.getOwner().getEffectivesPercent() / 100)), o.getMaxLaunchShips(), Mission.ATTACK);
    }

    public static void endPlanetMissions(Planet p) {
        if(Game.missions != null) {
            for (Mission m : Game.missions) {
                if (m.getOriginPlanet() == p) {
                    m.clearQueue();
                }
            }
        }
    }

    public void handle(long arg0) {
        if (!Game.freeze) {
            if(isGameOver()) {
                Planets.menu.setSelectedWindow(Window.RESULT_SCREEN);
                Game.setFreeze(true);
            }
            
            Game.ticks++;

            for (Mission r : Game.missions) {
                r.handle();
            }

            for (Planet p : Galaxy.getPlanets()) {
                p.productionTick();
            }

            Game.missions.removeIf((Mission r) -> r.isEmpty());
        }
    }

    public void updateUI() {
        if (!Game.freeze) {
            for (Planet p : Galaxy.getPlanets()) {
                p.printStock(gc, root);
            }

            this.selectP.update();
        }
    }

    public void handleAI() {
        if (!Game.freeze) {
            for (Player p : Galaxy.getPlayers()) {
                p.handle();
            }
        }
    }
    
    @Override
    public void clear() {
        Game.freeze = true;
        root.getChildren().clear();
    }

    public boolean isGameOver() {
        boolean hasEnemy = false;
        boolean hasMainPlayer = false;
        
        if(!Game.missions.isEmpty()) return false;
        
        for(Planet p : Galaxy.getPlanets()) {
            if(p.getOwner().isAI()) {
                hasEnemy = true;
            } else {
                if(p.getOwner() == Game.mainPlayer) {
                    hasMainPlayer = true;
                }
            }
        }
        
        return !hasEnemy || !hasMainPlayer;
    }
    
}
