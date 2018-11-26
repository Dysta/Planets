package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets.ResourcesManager;
import ship.Ship;

public class Galaxy {

	// Background : image

	public static double planetInfluenceZone;
	private double minimumPlanetSize;
	private double maximumPlanetSize;
	private double borderMargin;
	
	private double width, height;
	
	private ArrayList<Planet> planets;
	private ArrayList<Player> players;
	
	public Galaxy(double width, double height, int nbPlanets, int nbPlayers, double planetInfluenceZone, double minimumPlanetSize, double maximumPlanetSize, double borderMargin) {
		Random r = new Random();

		this.planets = new ArrayList<Planet>();
		this.players = new ArrayList<Player>();
		this.width = width;
		this.height = height;
		
		Galaxy.planetInfluenceZone = planetInfluenceZone;
		this.minimumPlanetSize = minimumPlanetSize;
		this.maximumPlanetSize = maximumPlanetSize;
		this.borderMargin = borderMargin;
		
		Planet n;
		for(int i = 0; i<nbPlanets;i++) {
			Player p = new Player();

			int tries = 0;
			
			do {
				n = new Planet(ResourcesManager.planet, p, this.width, this.height, this.minimumPlanetSize, this.maximumPlanetSize, this.borderMargin);
				tries++;
			} while ((tries < 100 && isColliding(n)));
			
			if(tries<100) {
				this.planets.add(n);
			} else {
				System.out.println("Could not put non colliding planet.");
			}
		}
		
		for(int i = 0; i < nbPlayers; i++) {
			Player p = new Player(Color.color(Math.random(), Math.random(), Math.random()));
			
			boolean found = false;
			Planet target = null;
			int tries = 0;
			int rInt = 0;
			while(tries < this.planets.size() * 10 && !found) {
				rInt = Galaxy.getRandomIntegerBetweenRange(0,this.planets.size()-1);
				target = this.planets.get(rInt);
				found = !target.getOwner().isActive();
				tries++;
			}
			
			if(found) {
				this.players.add(p);
				target.setOwner(p);
			} else {
				System.out.println("Could not find a free planet for the player "+i+".");
			}
			
		}
	}
	
	public static int getRandomIntegerBetweenRange(int min, int max){
	    int x = (int)((Math.random()*((max-min)+1))+min);
	    return x;
	}
	
	private boolean isColliding(Planet planet) {
		boolean colliding = false;
		for(Planet p: this.planets) {
			if(Math.sqrt(Math.pow(p.getPosXMiddle() - planet.getPosXMiddle(),2)+Math.pow(p.getPosYMiddle() - planet.getPosYMiddle(),2)) < (Galaxy.planetInfluenceZone+p.getSize()+planet.getSize())) {
				colliding = true;
			}
		}
		
		return colliding;
	}
	
	public ArrayList<Planet> getPlanets() {
		return this.planets;
	}
}
