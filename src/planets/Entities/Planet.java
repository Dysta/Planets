package planets.Entities;

import java.util.ArrayList;
import java.util.Random;

import planets.Sprite;

public class Planet extends Sprite {
	
	private Player owner;
	private double size;
	
	private float shipsPerTick;	
	private Ship production;

	private ArrayList<Ship> ships;

	public Planet(Sprite s, Player owner, double posX, double posY, double size) {
		super(s);
		this.owner = owner;
		this.setPosition(posX, posY);
		this.size = size;
		this.owner = new Player();
		this.ships = new ArrayList<Ship>();
	}
	
	private void produceShip(Sprite s) {
		double angle = Math.random()*Math.PI*2;
		double radius = (this.size+(Galaxy.planetInfluenceZone-this.size)/2);
		double x = Math.cos(angle)*radius;
		double y = Math.sin(angle)*radius;
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
	
	

}
