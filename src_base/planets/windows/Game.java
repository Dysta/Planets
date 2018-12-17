package planets.windows;

import java.util.ArrayList;

import input.KeyEventHandler;
import input.MouseHandler;
import input.MouseReleaseHandler;
import input.ScrollEventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import planets.Planets;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Galaxy;
import planets.entities.planet.Planet;
import planets.entities.Player;
import planets.entities.Mission;
import planets.entities.Squad;
import ui.SelectPercentage;
import planets.entities.ship.Ship;

/**
 * The actual Game window, with every entity in it. It handles the game and events related to it.
 * 
 * @author Adri
 */
public class Game extends Window {

    
    /**
     * The galaxy containing entities.
     */
    public static Galaxy galaxy;
    /**
     * A static collection of the current missions.
     */
    public static ArrayList<Mission> missions;
    /**
     * A static reference to the main player.
     */
    public static Player mainPlayer;

    // States
    /**
     * Indicates whether the user has been holding mouse1.
     */
    public static boolean primaryHeld;
    /**
     * Indicates whether the user has been holding control.
     */
    public static boolean ctrlHeld;
    /**
     * If set to true, prevents the game from handling anything.
     */
    public static boolean freeze = false;
    /**
     * The currently selected planets.
     */
    public static ArrayList<Planet> selectedPlanets;
    /**
     * The currently selected Squads.
     */
    public static ArrayList<Squad> selectedSquads;
    /**
     * An array of ticks to compute tickrate.
     */
    public static int ticks;

    // UI Elements
    /**
     * The indicator showing the current percentage of selected ships.
     */
    public static SelectPercentage selectP;

    /**
     * Creates an empty game and defines input events.
     * 
     * @param WIDTH the width of the window
     * @param HEIGHT the height of the window
     */
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

    /**
     * Prepares every needed event.
     * @param scene the scene listening to event
     */
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

    /**
     * Creates a random game from given parameters
     * 
     * @param nbPlayers the maximum number of players
     * @param nbPlanets the maximum number of planets
     */
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

    /**
     * Update the current game to match given arguments
     * 
     * @param galaxy the new galaxy reference
     * @param missions the new missions reference
     */
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
    
    /**
     * Starts a new mission
     * 
     * @param origin The origin planet
     * @param destination The destination planet
     * @param effectives The amount of ships to send
     * @param squadSize The max squad size
     * @param mission The goal, @see Window.
     */
    public static void startMission(Planet origin, Planet destination, int effectives, int squadSize, String mission) {
        Mission r = new Mission(origin, destination, effectives, squadSize, mission);
        Game.missions.add(r);
    }

    /**
     * Changes a squad's mission while they wer eperforming another.
     * 
     * @param squads the squad in question
     * @param destination the new destination
     * @param newOrder the new goal
     */
    public static void changeMission(ArrayList<Squad> squads, Planet destination, String newOrder) {
        Mission r = new Mission(squads.get(0).getMission().getOriginPlanet(), destination, 0, 0, newOrder);

        for (Squad s : squads) {
            s.reaffectSquad(r);
            r.addSquad(s);
        }

        Game.missions.add(r);
    }

    /**
     * Sets any element selected in a matching list.
     * 
     * @param list the select list corresponding to the element
     * @param element the element
     * @param state whether it's selected or not
     */
    public static void setSelect(ArrayList list, Sprite element, boolean state) {
        if (state) {
            list.add(element);
        } else {
            list.remove(element);
        }
        element.setSelected(state);
    }

    /**
     * Removes every element from a selection list.
     * @param list 
     */
    public static void deselect(ArrayList list) {
        int c = 0;
        int t = list.size();
        while (c < t) {
            Game.setSelect(list, (Sprite) list.get(0), false);
            c++;
        }
    }

    /**
     * Change the game freeze status.
     * @param b new status
     */
    public static void setFreeze(boolean b) {
        Game.freeze = b;
    }

    /**
     * Reverts the current game freeze status.
     */
    public static void toggleFreeze() {
    	setFreeze(!Game.freeze);
    }

    /**
     * Toggles selection on a squad 
     * 
     * @param s the squad
     * @param state the new state
     */
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

    /**
     * Resets all selected squads.
     */
    public static void deselectSquads() {
        int c = 0;
        int t = Game.selectedSquads.size();
        while (c < t) {
            Game.setSelectSquad(Game.selectedSquads.get(0), false);
            c++;
        }
    }

    /**
     * Creates a convoy mission.
     * 
     * @param target The target planet
     */
    public static void startConvoy(Planet target) {
        for (Planet o : Game.selectedPlanets) {
            Game.startMission(o, target, (int) (o.getNbShip() * (o.getOwner().getEffectivesPercent() / 100)), o.getMaxLaunchShips(), Mission.CONVOY);
        }
        Game.deselect(Game.selectedPlanets);
    }


    /**
     * Creates an attack mission.
     * 
     * @param o The origin planet
     * @param target The target planet
     */
    public static void startAttack(Planet o, Planet target) {
        Game.startMission(o, target, (int) (o.getNbShip() * (o.getOwner().getEffectivesPercent() / 100)), o.getMaxLaunchShips(), Mission.ATTACK);
    }

    /**
     * Terminate all missions for a planet.
     * 
     * @param p the planet to clear
     */
    public static void endPlanetMissions(Planet p) {
        if(Game.missions != null) {
            for (Mission m : Game.missions) {
                if (m.getOriginPlanet() == p) {
                    m.clearQueue();
                }
            }
        }
    }

    /**
     * Handles every game tick's behavior.
     * 
     * @param arg0 unused but provided by Application
     */
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

    /**
     * Handles the refresh of the game's user interface
     */
    public void updateUI() {
        if (!Game.freeze) {
            for (Planet p : Galaxy.getPlanets()) {
                p.printStock(gc, root);
            }

            this.selectP.update();
        }
    }

    /**
     * Makes the AIs take their decisions
     */
    public void handleAI() {
        if (!Game.freeze) {
            for (Player p : Galaxy.getPlayers()) {
                p.handle();
            }
        }
    }
    
    /**
     * Clear every element from the Game window.
     */
    @Override
    public void clear() {
        Game.freeze = true;
        root.getChildren().clear();
    }

    /**
     * Checks whether a game is over or not.
     * @return true if the game is over.
     */
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
