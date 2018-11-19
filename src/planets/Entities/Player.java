package planets.Entities;

import javafx.scene.paint.Color;

public class Player {

	private Color color;
	private boolean active;
	
	public Player(Color color, boolean active) {
		this.color = color;
		this.active = active;
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
}
