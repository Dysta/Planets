package planets.Entities;

import planets.ResourcesManager;
import planets.Sprite;

public class Ship extends Sprite {
	
	private Player owner;
	

	public Ship(Sprite s, double posX, double posY) {
		super(s);
		this.setPosition(posX, posY);
		this.updateDimensions(ResourcesManager.SHIP_PATH, 22, 44);
	}

}
