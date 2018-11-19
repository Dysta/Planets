package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import planets.ResourcesManager;

public class Galaxy {

	// Background : image

	private final double minimumPlanetSeparation = 40;
	private final double minimumPlanetSize = 10;
	private final double maximumPlanetSize = 35;
	
	private double width, height;
	
	private ArrayList<Planet> planets;
	private ArrayList<Player> players;
	
	public Galaxy(double width, double height, int nbPlanets, int nbPlayers) {
		Random r = new Random();
		
		for(int i = 0; i<nbPlanets;i++) {
			Planet n = new Planet(ResourcesManager.planet, new Player(), 
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
			}
		}
		
		for(int i = 0; i < nbPlayers; i++) {
			Player p = new Player(Color.color(Math.random(), Math.random(), Math.random()));
			this.players.add(p);
			
			boolean found = false;
			Planet target = null;
			while(!found) {
				target = this.planets.get(r.nextInt((this.planets.size()) + 1) + this.planets.size());
				found = !target.getOwner().isActive();
			}
			
			target.setOwner(p);
			
		}
	}
	
	private boolean isColliding(Planet planet) {
		boolean colliding = false;
		for(Planet p: this.planets) {
			if(Math.sqrt(Math.pow(p.getPosX() - planet.getPosX(),2)+Math.pow(p.getPosY() - planet.getPosY(),2)) < (this.minimumPlanetSeparation+p.getSize()+planet.getSize())) {
				colliding = true;
			}
		}
		
		return colliding;
	}
}
