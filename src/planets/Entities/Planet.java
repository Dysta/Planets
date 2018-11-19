package planets.Entities;

import java.util.ArrayList;

import planets.Sprite;

public class Planet extends Sprite {
	
	private Player owner;
	private double posX, posY, size;
	
	private float shipsPerTick;	
	private Ship production;

	private ArrayList<Ship> ships;

	public Planet(Sprite s, Player owner, double posX, double posY, double size) {
		super(s);
		this.owner = owner;
		this.posX = posX;
		this.posY = posY;
		this.size = size;
		this.owner = new Player();
	}
	
	private void produceShip(Sprite s) {
		this.ships.add(new Ship(s));
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
	
	
	

}
