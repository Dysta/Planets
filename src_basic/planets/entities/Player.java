package planets.entities;

import javafx.scene.paint.Color;

/**
 * The player is a possessor of Planets and Missions and can interact within its Galaxy.
 * 
 * @author Adri
 */
public class Player {

    /**
     * Used to give a unique incremental player ID.
     */
    private static int last_id = 0;
    /**
     * This player's ID.
     */
    private int id;

    /**
     * Whether this player is the performer of Inputs.
     */
    private boolean mainPlayer;
    
    /**
     * This player's color, entities attached to him will be colored this way.
     */
    private Color color;
    
    /**
     * Whether this player is 'active', a.k.a producing ships and apt to start missions.
     */
    private boolean active;
    
    /**
     * The preferred type of Ships for this player.
     * @see planets.entities.ship.
     */
    private String shipType;

    /**
     * The amount (in %) of ships to add to a new mission's Queue for this player.
     */
    private double effectivesPercent;

    /**
     * Create a simple Player
     * 
     * @param color This player's color
     * @param active Produces ships or not
     */
    public Player(Color color, boolean active) {
        Player.last_id++;
        this.id = Player.last_id;
        this.color = color;
        this.active = active;
        this.effectivesPercent = 100;
        this.setShipType("BaseShip");
    }

    /**
     * Create a simple active Player
     * 
     * @param color This player's color
     */
    public Player(Color color) {
        this(color, true);
    }

    /**
     * Create a simple inactive Player of GREY color.
     */
    public Player() {
        this(Color.GREY, false);
    }

    /**
     * Create any player
     * 
     * @param shiptype The preffered type of ship
     * @param mainPlayer Whether this is the main player
     * @param effectivesPercent The base value for his percentage for ship mission deployement
     * @param color The color for this player
     * @param active Whether he is an active player
     */
    public Player(String shiptype, boolean mainPlayer, double effectivesPercent, Color color, boolean active) {
        this(color,active);
        this.shipType = shiptype;
        this.mainPlayer = mainPlayer;
        this.effectivesPercent = effectivesPercent;
    }

    /**
     * Checks if this player is active.
     * 
     * @return the state of this player's activity
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns this player's color
     * 
     * @return a color object
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the classpath of this player's preffered ship type.
     * 
     * @param shipType a ship class name from planets.entities.ship.
     */
    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    /**
     * Returns the full classpath for this player's preffered ship type.
     * 
     * @return a ship classpath from planets.entities.ship.
     */
    public String getShipType() {
        return this.shipType;
    }

    /**
     * Changes the mainPlayer state for Player
     * 
     * @param t the new state
     */
    public void setMainPlayer(boolean t) {
        this.mainPlayer = t;
    }

    /**
     * Checks whether this players is the main player
     * @return the current state
     */
    public boolean isMainPlayer() {
        return this.mainPlayer;
    }

    /**
     * Returns the amount (in %) of ships to send for a new mission
     * @return a percentage
     */
    public double getEffectivesPercent() {
        return this.effectivesPercent;
    }

    /**
     * Sets the new percentage of maximum ships to send, from 0 to 100.
     * 
     * @param s the new percentage
     */
    public void setEffectivesPercent(double s) {
        if (s > 100) {
            s = 100;
        }
        if (s < 0) {
            s = 0;
        }
        this.effectivesPercent = s;
    }

    /**
     * Actions performed each game tick. Used for AIs.
     */
    public void handle() {

    }
    
    /**
     * Whether this player is an AI or not.
     * @return false because this is a player, but AI type Players will redefine it to return true.
     */
    public boolean isAI() {
        return false;
    }
    
    /**
     * Get this Player's unique ID.
     * @return this player's id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Changes this player's ID.
     * @param id the new ID
     */
    public void setId(int id) {
        this.id = id;
    }

}
