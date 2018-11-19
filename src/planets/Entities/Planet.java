package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import planets.ResourcesManager;
import planets.Sprite;

public class Planet extends Sprite {
	
	private Sprite s;
	private Player owner;
	private double size;
	
	private double shipsPerTick;	
	private double productionProgression;
	private Sprite production;

	private ArrayList<Ship> ships;

	public Planet(Sprite s, Player owner, double posX, double posY, double size) {
		super(s);
		this.s = s;
		this.owner = owner;
		this.setPosition(posX, posY);
		this.updateDimensions(ResourcesManager.PLANET_PATH, size, size);
		this.size = size;
		this.owner = new Player();
		this.ships = new ArrayList<Ship>();
		this.productionProgression = 0;
		this.shipsPerTick = 0.03;
		this.production = owner.getShipType();
	}

	public Planet(Planet s) {
		this(s.getSprite(), s.getOwner(), s.getPosX(), s.getPosY(), s.getSize());
		this.ships = s.getShips();
	}
	
	private void produceShip(Sprite s) {
		double angle = Math.random()*Math.PI*2;
		double radius = (this.size+(Galaxy.planetInfluenceZone-this.size)/2);
		double x = (this.getPosX() + this.getSize()/2) + Math.cos(angle)*radius;
		double y = (this.getPosY() + this.getSize()/2) + Math.sin(angle)*radius;
		this.ships.add(new Ship(s,x,y));
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
	public ArrayList<Ship> getShips() {
		return this.ships;
	}
	
	public Sprite getSprite() {
		return this.s;
	}
	
	public void productionTick() {
		this.productionProgression += this.shipsPerTick;
		
		while(this.productionProgression >= 1) {
			this.produceShip(this.production);
			this.productionProgression -= 1;
		}
	}

}
