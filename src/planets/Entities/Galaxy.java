package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets.ResourcesManager;

public class Galaxy {

	// Background : image

	public static final double planetInfluenceZone = 40;
	private final double minimumPlanetSize = 10;
	private final double maximumPlanetSize = 35;
	
	private double width, height;
	
	private ArrayList<Planet> planets;
	private ArrayList<Player> players;
	
	public Galaxy(double width, double height, int nbPlanets, int nbPlayers) {
		Random r = new Random();

		this.planets = new ArrayList<Planet>();
		this.players = new ArrayList<Player>();
		this.width = width;
		this.height = height;
		
		Planet n;
		for(int i = 0; i<nbPlanets;i++) {
			n = new Planet(ResourcesManager.planet, new Player(), 
					this.width * r.nextDouble(), 
					this.height * r.nextDouble(), 
					this.minimumPlanetSize + (this.maximumPlanetSize - this.minimumPlanetSize) * r.nextDouble()); 
			
			int tries = 0;
			while(tries < 10 && isColliding(n)) {
				n = new Planet(ResourcesManager.planet, new Player(),
						this.width * r.nextDouble(), 
						this.height * r.nextDouble(), 
						this.minimumPlanetSize + (this.maximumPlanetSize - this.minimumPlanetSize) * r.nextDouble());
				tries++;
			}
			
			if(tries<10) {
				this.planets.add(n);
				System.out.println("New planet. x: "+n.getPosX()+" y: "+n.getPosY()+" size: "+n.getSize());
			} else {
				System.out.println("Could not put non colliding planet.");
			}
		}
		
		for(int i = 0; i < nbPlayers; i++) {
			System.out.println("New player.");
			Player p = new Player(Color.color(Math.random(), Math.random(), Math.random()));
			
			boolean found = false;
			Planet target = null;
			int tries = 0;
			int rInt = 0;
			while(tries < this.planets.size() * 10 && !found) {
				System.out.println("Size : "+this.planets.size()+".");
				rInt = Galaxy.getRandomIntegerBetweenRange(0,this.planets.size());
				System.out.println("Trying to give him planet "+rInt+".");
				target = this.planets.get(rInt);
				found = !target.getOwner().isActive();
				tries++;
			}
			
			if(found) {
				this.players.add(p);
				target.setOwner(p);
				System.out.println("Gave him planet "+rInt+".");
			} else {
				System.out.println("Could not find a free planet for the player.");
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
			if(Math.sqrt(Math.pow(p.getPosX() - planet.getPosX(),2)+Math.pow(p.getPosY() - planet.getPosY(),2)) < (this.planetInfluenceZone+p.getSize()+planet.getSize())) {
				colliding = true;
			}
		}
		
		return colliding;
	}
}
