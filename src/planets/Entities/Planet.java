package planets.Entities;


import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets.Game;
import planets.ResourcesManager;
import planets.Sprite;
import ship.Ship;

public class Planet extends Sprite {
	
	private Sprite s;
	private Player owner;
	private double size;
	private Text text;
	
	
	private double shipsPerTick;	
	private double productionProgression;
	private String production;
	
	private boolean printedStock;

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
		this.shipsPerTick = 0.3;
		this.production = owner.getShipType();
		
		this.text = new Text();
		this.printedStock = false;
	}

	public Planet(Sprite s, Player owner, double boundaryX, double boundaryY, double minSize, double maxSize, double borderMargin) {
		this(s,owner, 0, 0, 0);
		
		Random r = new Random();
		double size = minSize + (maxSize - minSize) * r.nextDouble();
		double posX = (boundaryX - 2 * borderMargin - size) * r.nextDouble() + borderMargin;
		double posY = (boundaryY - 2 * borderMargin - size) * r.nextDouble() + borderMargin;

		this.setPosition(posX, posY);
		this.updateDimensions(ResourcesManager.PLANET_PATH, size, size);
		this.size = size;
	}

	public Planet(Planet s) {
		this(s.getSprite(), s.getOwner(), s.getPosX(), s.getPosY(), s.getSize());
		this.ships = s.getShips();
	}
	
	private void produceShip(String s) {
		double angle = Math.random()*Math.PI*2;
		double radius = (this.size+(Galaxy.planetInfluenceZone-this.size)/2);
		double x = ((this.getPosX() + this.getSize()/2) + Math.cos(angle)*radius);
		double y = (this.getPosY() + this.getSize()/2) + Math.sin(angle)*radius;
		try {
			Ship ship = (Ship) Class.forName(this.production).getConstructor(Sprite.class, double.class, double.class).newInstance(ResourcesManager.ship,x,y);
			ship.changeOwner(this.owner);
			ship.setPosition(ship.getPosX()-ship.width()/2, ship.getPosY()-ship.height()/2);
			ship.initRender(Game.root);
			this.ships.add(ship);
		} catch (Exception e) {
			System.err.println("Error during ship initialization : "+e);
		}
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
		ResourcesManager.colorImage(this.getImageView(), owner.getColor());
                
                if(this.owner.isActive()) {
                    this.shipsPerTick *= 2;
                } else {
                    this.shipsPerTick /= 2;
                }
	}
	
	public void printStock(GraphicsContext gc, Group root) {
		this.text.setFont(gc.getFont());
		this.text.setFill(this.owner.getColor());
		this.text.setText("" + this.getNbShip());
		
		TextFlow tf = new TextFlow();
		tf.setLayoutX(this.getPosXMiddle() - this.text.getLayoutBounds().getWidth() / 2);
		tf.setLayoutY(this.getPosYMiddle() - this.text.getLayoutBounds().getHeight() / 2);
		
		if (!this.printedStock) {
			tf.getChildren().add(this.text);
			root.getChildren().add(tf);
			this.printedStock = true;
		} else {
			tf.getChildren().remove(this.text);
			root.getChildren().remove(tf);
			this.printedStock = false;
		}
	}
	
	public int getNbShip() {
		return this.ships.size();
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
	
	public boolean isOn(double x, double y) {
		boolean on = false;
		
		on = Math.pow((x - this.getPosXMiddle()),2) + Math.pow(y - this.getPosYMiddle(),2) < Math.pow(this.size/2,2);
		
		return on;
	}
	
	public boolean inOrbit(Ship s) {
		boolean on = false;
		
		on = Math.pow((s.getPosXMiddle() - this.getPosXMiddle()),2) + Math.pow(s.getPosYMiddle() - this.getPosYMiddle(),2) < Math.pow((this.size/2)+Galaxy.planetInfluenceZone,2);
		
		return on;
	}

	public boolean defend(ArrayList<Ship> attackers) {
		boolean defeat = false;
		
		int c = 0;
		int a = attackers.size();
		int d = this.ships.size();
		while(c<a && c <d) {
                        this.ships.get(0).die();
			this.ships.remove(0);
                        attackers.get(0).die();
			attackers.remove(0);
                        
			c++;
		}
		defeat = c >= d;
		
		return defeat;
	}
	
}
