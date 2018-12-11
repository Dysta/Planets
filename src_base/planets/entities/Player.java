package planets.entities;

import javafx.scene.paint.Color;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.ship.Ship;

public class Player {
    
    private static int last_id = 0;
    private int id;

    private boolean mainPlayer;

    private Color color;
    private boolean active;
    private String shipType;

    private double effectivesPercent;

    public Player(Color color, boolean active) {
        Player.last_id++;
        this.id = Player.last_id;
        this.color = color;
        this.active = active;
        this.effectivesPercent = 100;
        this.setShipType("BaseShip");
    }

    public Player(Color color) {
        this(color, true);
    }

    public Player() {
        this(Color.GREY, false);
    }

    public Player(String shiptype, boolean mainPlayer, double effectivesPercent, Color color, boolean active) {
        this(color,active);
        this.shipType = shiptype;
        this.mainPlayer = mainPlayer;
        this.effectivesPercent = effectivesPercent;
    }

    public boolean isActive() {
        return active;
    }

    public Color getColor() {
        return color;
    }

    public void setShipType(String shipType) {
        this.shipType = "planets.entities.ship." + shipType;
    }

    public String getShipType() {
        return this.shipType;
    }

    public void setMainPlayer(boolean t) {
        this.mainPlayer = t;
    }

    public boolean isMainPlayer() {
        return this.mainPlayer;
    }

    public double getEffectivesPercent() {
        return this.effectivesPercent;
    }

    public void setEffectivesPercent(double s) {
        if (s > 100) {
            s = 100;
        }
        if (s < 0) {
            s = 0;
        }
        this.effectivesPercent = s;
    }

    public void handle() {

    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

}
