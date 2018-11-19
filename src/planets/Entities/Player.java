package planets.Entities;

import javafx.scene.paint.Color;
import planets.ResourcesManager;
import planets.Sprite;

public class Player {

	private Color color;
	private boolean active;
	private Sprite ship;
	
	public Player(Color color, boolean active) {
		this.color = color;
		this.active = active;
		this.ship = ResourcesManager.ship;
	}
	
	public Player(Color color) {
		this(color, true);
	}
	
	public Player() {
		this(Color.GREY, false);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Sprite getShipType() {
		return this.ship;
	}

}
