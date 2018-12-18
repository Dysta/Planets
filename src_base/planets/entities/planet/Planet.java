package planets.entities.planet;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets.windows.Game;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Galaxy;
import planets.entities.Mission;
import planets.entities.Player;
import planets.entities.Squad;
import planets.entities.ship.Ship;

/**
 * An entity holding and producting ships, owned by a Player.
 *
 * @author Adri
 */
public abstract class Planet extends Sprite {

    /**
     * Used to give a unique incremental Planet ID.
     */
    private static int last_id = 0;
    /**
     * This planet's ID.
     */
    private int id;

    /**
     * The original sprite associated with the planet.
     */
    private Sprite s;

    /**
     * The player owning this planet
     */
    private Player owner;

    /**
     * The diameter of this planet.
     */
    private double size;

    /**
     * Used to display the current ships stock.
     */
    private Text text;

    /**
     * Used to apply an effect on the current ships stock text.
     */
    private TextFlow tf;

    /**
     * The maximum amount of ships for this planet.
     */
    protected int shipCapacity;

    /**
     * The amount of ships produced at every game tick.
     */
    protected double shipsPerTick;

    /**
     * The number of ships that should be produced on this tick.
     */
    protected double productionProgression;

    /**
     * The type of ship to produce.
     */
    private String shipType;

    /**
     * A collection of ships ready to defend or be deployed
     */
    private ArrayList<Ship> ships;

    /**
     * Sets the base stats of any planet.
     *
     * @param owner The owner of this planet
     * @param width The image width
     * @param height The image height
     * @param productionProgression This planets base amount of ships
     * @param shipsPerTick This planets production speed
     * @param shipCapacity This planets maximum amount of ships
     */
    public Planet(Player owner,int width, int height, double productionProgression, double shipsPerTick, int shipCapacity) {
        super(ResourcesManager.getSpriteAsset("BasePlanet","images/planets/BasePlanet.png",width,height));
        Planet.last_id++;
        this.id = Planet.last_id;
        this.s = s;
        this.owner = owner;
        Random r = new Random();
        size = Galaxy.minimumPlanetSize + (Galaxy.maximumPlanetSize - Galaxy.minimumPlanetSize) * r.nextDouble();
        double posX = (Galaxy.width - 2 * Galaxy.borderMargin - size) * r.nextDouble() + Galaxy.borderMargin;
        double posY = (Galaxy.height - 2 * Galaxy.borderMargin - size) * r.nextDouble() + Galaxy.borderMargin;
        this.setPosition(posX, posY);
        this.updateDimensions(getImagePath(), size, size);
        this.ships = new ArrayList<>();
        this.productionProgression = productionProgression; // Base ships on any planet (will produce those ships instantly)
        this.shipsPerTick = shipsPerTick; // Minimum production
        this.shipCapacity = shipCapacity; // Minimum storage
        
        this.getImageView().setImage(ResourcesManager.getSpriteAsset(assetReference(),getImagePath(),width,height).getImage());
        
        initPlanet();

        // Components displaying current ships capacity
        this.text = new Text();
        this.tf = new TextFlow();
    }

    /**
     * Creates a planet according to its characteristics.
     */
    public final void initPlanet() {

        this.shipsPerTick *= 1 + (size / (Galaxy.maximumPlanetSize * 2)); // The biggest planets can produce up to 50% more than smallest ones
        this.shipCapacity *= 1 + (size / (Galaxy.maximumPlanetSize * 1.2)); // The biggest planets can store up to 83% more than smallest ones

        this.shipType = this.owner.getShipType();
        this.updateDimensions(getImagePath(), size, size);
    }

    /**
     * Updates this planet with given parameters
     *
     * @param shipType The type of ships to produce
     * @param shipPerTick The number of ships produced each game tick
     * @param shipsCount The number of ships previously on this planet
     * @param shipCapacity The amount of ships this planet can handle at a time
     * @param productionProgression The current production status
     */
    public void loadPlanet(double x, double y,String shipType, double shipPerTick, int shipsCount, int shipCapacity, double productionProgression, double size) {
        this.setPosition(x, y);
        this.shipType = shipType;
        this.shipsPerTick = shipPerTick;
        this.shipCapacity = shipCapacity;
        this.productionProgression = productionProgression + shipsCount;
        this.size = size;
        this.updateDimensions(getImagePath(), size, size);
    }

    /**
     * Returns its own class name
     *
     * @return its own class name
     */
    @Override
    abstract public String assetReference();

    /**
     * Returns this Sprite's image path
     * @return this Sprite's image path
     */
    public String getImagePath() {
        return "images/planets/"+assetReference()+".png";
    }
    
    /**
     * Get the type of the produced ships
     *
     * @param type a ship class name
     */
    public void setShipType(String type) {
        this.shipType = type;
    }

    /**
     * Creates a new ship according to shipType and gives it a random position
     * in orbit (hidden).
     */
    private void produceShip() {
        String s = "planets.entities.ship." + Character.toUpperCase(this.shipType.charAt(0)) + this.shipType.substring(1);
        double angle = Math.random() * Math.PI * 2;
        double radius = (this.size + (Galaxy.planetInfluenceZone - this.size) / 2);
        double x = ((this.getPosX() + this.getSize() / 2) + Math.cos(angle) * radius);
        double y = (this.getPosY() + this.getSize() / 2) + Math.sin(angle) * radius;
        try {
            Ship ship = (Ship) Class.forName(s).getConstructor(double.class, double.class).newInstance(x, y);
            ship.changeOwner(this.owner);
            ship.setPosition(ship.getPosX() - ship.width() / 2, ship.getPosY() - ship.height() / 2);
            this.ships.add(ship);
        } catch (Exception e) {
            System.err.println("Error during ship (" + Character.toLowerCase(s.charAt(0)) + s.substring(1) + ") initialization : " + e);
        }
    }

    /**
     * Returns the owner of this planet
     *
     * @return a Player reference
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Changes the owner of this planet, and updating the images in consequence.
     *
     * @param owner The new owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
        Game.endPlanetMissions(this);
        ResourcesManager.colorImage(this, owner.getColor());
    }

    /**
     * Displays the current amount of ships stored in the planet
     *
     * @param gc The context to get style rules from
     * @param root The root in which to add the elements
     */
    public void printStock(GraphicsContext gc, Group root) {
        this.text.setFont(gc.getFont());
        this.text.setFill(this.owner.getColor());
        this.text.setStroke(Color.BLACK);
        this.text.setText("" + this.getNbShip());

        tf.setLayoutX(this.getPosXMiddle() - this.text.getLayoutBounds().getWidth() / 2);
        tf.setLayoutY(this.getPosYMiddle() - this.text.getLayoutBounds().getHeight() / 2);

        tf.getChildren().remove(this.text);
        root.getChildren().remove(tf);
        tf.getChildren().add(this.text);
        root.getChildren().add(tf);
    }

    /**
     * Counts the number of ships in stock.
     *
     * @return the number of ships
     */
    public int getNbShip() {
        return this.ships.size();
    }

    /**
     * Computes the maximum number of shipsthat can be sent with each squad.
     *
     * @return the amoung of ships to send to fill a squad
     */
    public int getMaxLaunchShips() {
        if (this.ships.isEmpty()) {
            return 0;
        }
        return (int) ((this.size) * Math.PI / this.ships.get(0).width());
    }

    /**
     * Get this planet's diameter.
     *
     * @return this planet's diameter
     */
    public double getSize() {
        return size;
    }

    /**
     * Change the size of this planet.
     *
     * @param size the new size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Get a collection of the currently stored ships in this planet.
     *
     * @return a ship collection reference
     */
    public ArrayList<Ship> getShips() {
        return this.ships;
    }

    /**
     * Get the reference sprite for this object.
     *
     * @return a Sprite object
     */
    public Sprite getSprite() {
        return this.s;
    }

    /**
     * Handles the production of this planet at each game tick.
     */
    public void productionTick() {
        if (this.owner.isActive() && this.shipCapacity > this.getNbShip()) {
            this.productionProgression += this.shipsPerTick;
        }

        while (this.productionProgression >= 1) {
            this.produceShip();
            this.productionProgression -= 1;
        }
    }

    /**
     * Checks whether a point is on this planet.
     *
     * @param x the point's x coordinate
     * @param y the point's y coordinate
     * @return true if the point is on this planet
     */
    @Override
    public boolean isOn(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow(this.size / 2, 2);
    }

    /**
     * Checks whether a point is in the orbit zone of this planet
     *
     * @param x the point's x coordinate
     * @param y the point's y coordinate
     * @return true if the point is on this planet's orbit zone
     */
    public boolean inOrbit(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow((this.size / 2) + Galaxy.planetInfluenceZone, 2);
    }

    /**
     * Checks whether a Ship is in the orbit zone of this planet
     *
     * @param s the Ship
     * @return true if the ship is on this planet's orbit zone
     */
    public boolean inOrbit(Ship s) {
        return this.inOrbit(s.getPosXMiddle(), s.getPosYMiddle());
    }

    /**
     * Checks whether a point is in the security zone of this planet
     *
     * @param x the point's x coordinate
     * @param y the point's y coordinate
     * @return true if the point is on this planet's security zone
     */
    public boolean inSecurityZone(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow((this.size / 2) + Galaxy.planetSecurityZone, 2);
    }

    /**
     * Defends itself against a collection of attacker Ships, according each
     * Ship's characteristics.
     *
     * @param attackers the attacking ships
     * @return true if the defense failed
     */
    public boolean defend(ArrayList<Ship> attackers) {
        while (attackers.size() > 0 && this.ships.size() > 0) {
            int c = 0;
            int a = attackers.size();
            int d = this.ships.size();
            while (c < a && c < d) {
                Ship defender = this.ships.get(0);
                Ship attacker = attackers.get(0);

                attacker.attack(defender);
                defender.attack(attacker);

                if (defender.isDead()) {
                    defender.die();
                    this.ships.remove(defender);
                }
                if (attacker.isDead()) {
                    attacker.die();
                    attackers.remove(attacker);
                }

                c++;
            }
        }

        if (attackers.size() > 0) {
            this.landShips(attackers);
        }

        return attackers.size() > 0;
    }

    /**
     * Get the total power of this planet's ships.
     *
     * @return a sum of the ships' power
     */
    public int getPower() {
        int t = 0;
        for (Ship s : this.ships) {
            t += s.getPower();
        }
        return t;
    }

    /**
     * Checks whether it is possible to send a new squad.
     *
     * @return true if nothing is on the way
     */
    public boolean freeToLaunch() {
        boolean free = true;
        for (Mission m : Game.missions) {
            if (m.getOriginPlanet() == this) {
                for (Squad sq : m.getSquads()) {
                    if (!sq.isGone()) {
                        free = false;
                    }
                }
            }
        }
        return free;
    }

    /**
     * Receives a collection of ships and stores them in its ships stock.
     *
     * @param ships the ships to welcome
     */
    public void landShips(ArrayList<Ship> ships) {
        for (Ship s : ships) {
            if (this.getNbShip() < this.shipCapacity) {
                try {
                    s.stop();
                    s.setSelected(false);
                    s.destroy();
                    this.ships.add(s);
                } catch (Exception e) {
                    System.err.println("Error during ship transfer : " + e);
                }
            } else {
                s.die();
            }
        }
    }

    /**
     * Removes a number of ships from its stock and returns them so they can be
     * handled by anything. If there are not enough ships, the maximum will be
     * sent.
     *
     * @param effectives The number of ships to fly
     * @return The ships that are ready
     */
    public ArrayList<Ship> flyShips(int effectives) {
        if (effectives > this.getNbShip()) {
            effectives = this.getNbShip();
        }

        ArrayList<Ship> mobilized = new ArrayList<>();
        for (int i = 0; i < effectives; i++) {
            Ship calledShip = this.ships.get(0);
            mobilized.add(calledShip);
            this.ships.remove(calledShip);

            double x = this.getPosXMiddle() + (Math.cos(((((double) 2 / effectives) * i) - 1) * Math.PI) * (this.size / 2));
            double y = this.getPosYMiddle() + (Math.sin(((((double) 2 / effectives) * i) - 1) * Math.PI) * (this.size / 2));

            calledShip.setMiddlePosition(x, y);
            calledShip.start(this);
        }

        return mobilized;
    }

    /**
     * Get the number of ships stored in this planet.
     *
     * @return the number of ships stored in this planet
     */
    public int getShipCapacity() {
        return this.shipCapacity;
    }

    /**
     * Get the number of ships produced every game tick.
     *
     * @return the number of ships produced every game tick
     */
    public double getShipsPerTick() {
        return this.shipsPerTick;
    }

    /**
     * Get the current progresstion of this planet's ship production.
     *
     * @return the current progresstion of this planet's ship production
     */
    public double getProductionProgression() {
        return this.productionProgression;
    }

    /**
     * Get the type of ships produced by this planet.
     *
     * @return the type of ships produced by this planet
     */
    public String getShipType() {
        return this.shipType;
    }

    /**
     * Get this planet's unique id.
     *
     * @return a unique id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Changes this planet's ID
     *
     * @param id the new ID
     */
    public void setId(int id) {
        this.id = id;
    }

}
