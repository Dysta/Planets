package planets.entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets.utils.GameUtils;
import planets.ResourcesManager;
import planets.entities.ship.Ship;

public class Galaxy {

    // Background : image
    public static double planetInfluenceZone;
    public static double planetSecurityZone;
    public static double minimumPlanetSize;
    public static double maximumPlanetSize;
    public static double borderMargin;

    private double width, height;

    private static ArrayList<Planet> planets;
    private static ArrayList<Player> players;

    public Galaxy(double width, double height, int nbPlanets, int nbPlayers, double borderMargin) {
        Random r = new Random();

        Galaxy.planets = new ArrayList<Planet>();
        Galaxy.players = new ArrayList<Player>();
        this.width = width;
        this.height = height;

        Galaxy.maximumPlanetSize = Math.sqrt(((width-2*borderMargin)*(height-2*borderMargin))/nbPlanets/Math.PI)/1.5;
        Galaxy.minimumPlanetSize = Galaxy.maximumPlanetSize * 0.7;
        Galaxy.planetInfluenceZone = Galaxy.maximumPlanetSize * 0.3;
        Galaxy.planetSecurityZone = Galaxy.maximumPlanetSize * 0.5;
        Galaxy.borderMargin = borderMargin;

        Planet n;
        for (int i = 0; i < nbPlanets; i++) {
            Player p = new Player();

            int tries = 0;

            do {
                n = new Planet(ResourcesManager.assets.get("planet"), p, this.width, this.height, Galaxy.minimumPlanetSize, Galaxy.maximumPlanetSize, Galaxy.borderMargin);
                tries++;
            } while ((tries < 100 && isColliding(n)));

            if (tries < 100) {
                Galaxy.planets.add(n);
            } else {
                System.out.println("Could not put non colliding planet.");
            }
        }

        boolean main = true;
        for (int i = 0; i < nbPlayers; i++) {
            Player p = new Player(Color.color(Math.random(), Math.random(), Math.random()));

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
                p.setMainPlayer(main);
                main = false;
                Galaxy.players.add(p);
                target.setOwner(p);
            } else {
                System.out.println("Could not find a free planet for the player " + i + ".");
            }

        }
    }



    private boolean isColliding(Planet planet) {
        boolean colliding = false;
        for (Planet p : Galaxy.planets) {
            if (Math.sqrt(Math.pow(p.getPosXMiddle() - planet.getPosXMiddle(), 2) + Math.pow(p.getPosYMiddle() - planet.getPosYMiddle(), 2)) < (Galaxy.planetSecurityZone + p.getSize() + planet.getSize())) {
                colliding = true;
            }
        }

        return colliding;
    }

    public static ArrayList<Planet> getPlanets() {
        return Galaxy.planets;
    }
}
