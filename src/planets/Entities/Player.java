package planets.Entities;

import javafx.scene.paint.Color;
import planets.ResourcesManager;
import planets.Sprite;
import ship.Ship;

public class Player {

	private boolean mainPlayer;
	
	private Color color;
	private boolean active;
	private String shipType;
	
	public Player(Color color, boolean active) {
		this.color = color;
		this.active = active;
		this.shipType = "ship.Ship";
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
	
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	
	public String getShipType() {
		return this.shipType;
	}
	
	public void setMainPlayer(boolean t) {
		this.mainPlayer = t;
	}

}
