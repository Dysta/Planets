package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets.ResourcesManager;
import ship.Ship;

public class Galaxy {

    // Background : image
    public static double planetInfluenceZone;
    public static double planetSecurityZone;
    private double minimumPlanetSize;
    private double maximumPlanetSize;
    private double borderMargin;

    private double width, height;

    private static ArrayList<Planet> planets;
    private static ArrayList<Player> players;

    public Galaxy(double width, double height, int nbPlanets, int nbPlayers, double planetInfluenceZone, double planetSecurityZone, double minimumPlanetSize, double maximumPlanetSize, double borderMargin) {
        Random r = new Random();

        this.planets = new ArrayList<Planet>();
        this.players = new ArrayList<Player>();
        this.width = width;
        this.height = height;

        Galaxy.planetInfluenceZone = planetInfluenceZone;
        Galaxy.planetSecurityZone = planetSecurityZone;
        this.minimumPlanetSize = minimumPlanetSize;
        this.maximumPlanetSize = maximumPlanetSize;
        this.borderMargin = borderMargin;

        Planet n;
        for (int i = 0; i < nbPlanets; i++) {
            Player p = new Player();

            int tries = 0;

            do {
                n = new Planet(ResourcesManager.planet, p, this.width, this.height, this.minimumPlanetSize, this.maximumPlanetSize, this.borderMargin);
                tries++;
            } while ((tries < 100 && isColliding(n)));

            if (tries < 100) {
                this.planets.add(n);
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
            while (tries < this.planets.size() * 10 && !found) {
                rInt = Galaxy.getRandomIntegerBetweenRange(0, this.planets.size() - 1);
                target = this.planets.get(rInt);
                found = !target.getOwner().isActive();
                tries++;
            }

            if (found) {
                p.setMainPlayer(main);
                if(main) {
                    p.setShipType("Destroyer");
                }
                main = false;
                this.players.add(p);
                target.setOwner(p);
            } else {
                System.out.println("Could not find a free planet for the player " + i + ".");
            }

        }
    }

    public static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) ((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    private boolean isColliding(Planet planet) {
        boolean colliding = false;
        for (Planet p : this.planets) {
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
