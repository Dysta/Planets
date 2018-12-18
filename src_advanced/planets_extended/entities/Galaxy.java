package planets_extended.entities;

import planets_extended.entities.planet.Planet;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets_extended.utils.GameUtils;
import planets_extended.ResourcesManager;
import planets_extended.entities.AIs.BaseAI;
import planets_extended.entities.planet.AdvancedPlanet;
import planets_extended.entities.planet.BasePlanet;

/**
 * The Galaxy contains the Game's entities and defines basic physics rules.
 * 
 * @author Adri
 */
public class Galaxy {

    /**
     * Any entity within this diameter is within orbit range.
     */
    public static double planetInfluenceZone;
    
    /**
     * No enemy entity can enter this diameter.
     */
    public static double planetSecurityZone;
    
    /**
     * The minimum diameter for a planet.
     */
    public static double minimumPlanetSize;
    
    /**
     * The maximum diameter for a planet.
     */
    public static double maximumPlanetSize;
    
    /**
     * No entity can spawn within this margin from the Galaxy's borders.
     */
    public static double borderMargin;

    /**
     * The Galaxy width in pixels.
     */
    public static double width;

    /**
     * The Galaxy height in pixels.
     */
    public static double height;

    /**
     * The planets_extended contained by the Galaxy.
     */
    private static ArrayList<Planet> planets;

    /**
     * The players who are part of this Galaxy.
     */
    private static ArrayList<Player> players;

    /**
     * Generates a random Galaxy with its entities according to the parameters.
     * 
     * @param width The Galaxy width in pixels.
     * @param height The Galaxy height in pixels.
     * @param nbPlanets The maximum number of planets_extended to generate
     * @param nbPlayers The maximum number of players
     * @param borderMargin The margin in which no planet can spawn
     */
    public Galaxy(double width, double height, int nbPlanets, int nbPlayers, double borderMargin) {
        Random r = new Random();

        Galaxy.planets = new ArrayList<Planet>();
        Galaxy.players = new ArrayList<Player>();
        Galaxy.width = width;
        Galaxy.height = height;

        Galaxy.maximumPlanetSize = Math.sqrt(((width-2*borderMargin)*(height-2*borderMargin))/nbPlanets/Math.PI)/1.5;
        Galaxy.minimumPlanetSize = Galaxy.maximumPlanetSize * 0.7;
        Galaxy.planetInfluenceZone = Galaxy.maximumPlanetSize * 0.3;
        Galaxy.planetSecurityZone = Galaxy.maximumPlanetSize * 0.5;
        Galaxy.borderMargin = borderMargin;

        Planet n;
        for (int i = 0; i < nbPlanets; i++) {
            Player p = new Player();
            Galaxy.players.add(p);

            int tries = 0;

            do {
                n = new BasePlanet(p);
                tries++;
            } while ((tries < 10 && isColliding(n)));

            if (tries < 10) {
                Galaxy.planets.add(n);
            }
        }

        boolean main = true;
        for (int i = 0; i < nbPlayers; i++) {
            
            Player p;
            if(main) {
                p = new Player(Color.color(Math.random(), Math.random(), Math.random()), true);
            } else {
                p = new BaseAI(Color.color(Math.random(), Math.random(), Math.random()));
            }

            boolean found = false;
            Planet target = null;
            int tries = 0;
            int rInt = 0;
            while (tries < Galaxy.planets.size() * 10 && !found) {
                rInt = GameUtils.getRandomIntegerBetweenRange(0, this.planets.size() - 1);
                target = Galaxy.planets.get(rInt);
                found = !target.getOwner().isActive();
                tries++;
            }

            if (found) {
                if(main) {
                    p.setMainPlayer(main);
                    p.setShipType("BurstShip");
                    main = false;
                }
                Galaxy.players.add(p);
                Galaxy.players.remove(target.getOwner());
                Planet nPlanet = new AdvancedPlanet(p);
                nPlanet.setPosition(target.getPosX(), target.getPosY());
                nPlanet.setOwner(p);
                Galaxy.planets.remove(target);
                Galaxy.planets.add(nPlanet);
            } else {
                System.out.println("Could not find a free planet for the player " + i + ".");
            }
        }
    }
    
    /**
     * Creates a Galaxy object with already defined Entities
     * 
     * @param width The Galaxy width in pixels.
     * @param height The Galaxy height in pixels.
     * @param planets_extended A collection of planets_extended that will replace the current one
     * @param players A collection of players that will replace the current one
     * @param borderMargin The margin in which no planet can spawn
     */
    public Galaxy(double width, double height, ArrayList<Planet> planets_extended, ArrayList<Player> players, double borderMargin) {
        Galaxy.planets = planets_extended;
        Galaxy.players = players;
        this.width = width;
        this.height = height;

        Galaxy.maximumPlanetSize = Math.sqrt(((width-2*borderMargin)*(height-2*borderMargin))/planets_extended.size()/Math.PI)/1.5;
        Galaxy.minimumPlanetSize = Galaxy.maximumPlanetSize * 0.7;
        Galaxy.planetInfluenceZone = Galaxy.maximumPlanetSize * 0.3;
        Galaxy.planetSecurityZone = Galaxy.maximumPlanetSize * 0.5;
        Galaxy.borderMargin = borderMargin;
    }

    /**
     * Checks whether a planet collides with any existing one.
     * 
     * @param planet The planet to check
     * @return whether the planet is colliding with another one or not
     */
    public boolean isColliding(Planet planet) {
        boolean colliding = false;
        for (Planet p : Galaxy.planets) {
            if (Math.sqrt(Math.pow(p.getPosXMiddle() - planet.getPosXMiddle(), 2) + Math.pow(p.getPosYMiddle() - planet.getPosYMiddle(), 2)) < (Galaxy.planetSecurityZone + p.getSize() + planet.getSize())) {
                colliding = true;
            }
        }

        return colliding;
    }
    
    /**
     * Fetch this Galaxy's planets_extended
     * 
     * @return a reference to this Galaxy's planets_extended collection
     */
    public static ArrayList<Planet> getPlanets() {
        return Galaxy.planets;
    }

    
    /**
     * Fetch this Galaxy's players
     * 
     * @return a reference to this Galaxy's players collection
     */
    public static ArrayList<Player> getPlayers() {
        return Galaxy.players;
    }

    /**
     * Replaces ths current planets_extended collection by the one given in parameter
     * 
     * @param planets_extended The collection replacing the current one
     */
    public static void setPlanets(ArrayList<Planet> planets_extended) {
        Galaxy.planets = planets_extended;
    }

    /**
     * Replaces ths current players collection by the one given in parameter
     * 
     * @param players The collection replacing the current one
     */
    public static void setPlayers(ArrayList<Player> players) {
        Galaxy.players = players;
    }
}
