package ship;

import planets.ResourcesManager;
import planets.Sprite;
import planets.Entities.Player;

public class Ship extends Sprite {
	
	private Player owner;

	
	public Ship(Sprite s, double posX, double posY) {
		super(s);
		this.setPosition(posX, posY);
	}
	
	
	public void changeOwner(Player owner) {
		this.owner = owner;
	}
	
	public void move(double x, double y) {
		this.setPosition(this.getPosX() + x, this.getPosY() + y);
	}
}
