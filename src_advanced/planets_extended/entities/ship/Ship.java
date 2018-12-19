package planets_extended.entities.ship;

import java.util.ArrayList;
import java.util.Random;
import planets_extended.entities.Galaxy;
import planets_extended.entities.planet.Planet;
import planets_extended.ResourcesManager;
import planets_extended.Sprite;
import planets_extended.entities.Player;
import planets_extended.utils.GameUtils;
import planets_extended.utils.Point;

/**
 * An entity capable of traveling between planets_extended and destroying other Ships alike
 * 
 * @author Adri
 */
public abstract class Ship extends Sprite {

    /**
     * Used to give a unique incremental ship ID.
     */
    private static int last_id = 0;
    /**
     * This ship's ID.
     */
    private int id;

    /**
     * The owner of this ship, used for coloring.
     */
    private Player owner;
    
    /**
     * The current velocity of the ship.
     */
    protected double currentSpeed;
    
    /**
     * The acceleration metric of this ship.
     */
    protected double acceleration;
    
    /**
     * The maximum velocity it can handle.
     */
    protected double speedCap;
    
    /**
     * The amount of damage per round.
     */
    protected double power;
    
    /**
     * The amount of damage it can hold.
     */
    protected double shield;
    
    /**
     * The amount of time it takes to produce this ship.
     */
    protected double complexity;

    /**
     * The number of times it won't check again before going forward when moving.
     */
    private double blindForward;
    
    /**
     * The last known direction it was pointing to.
     */
    private double lastDir;
    
    /**
     * Whether this ship can go in a straight line to go to its destination.
     */
    private boolean straightLine;

    /**
     * Instanciate this ship and its characteristics.
     * 
     * @param posX The top-left x position
     * @param posY The top-left y position
     * @param width The image width
     * @param height The image height
     * @param speedCap The maximum velocity allowed for this ship
     * @param acceleration The amount of velocity it gains when speeding up
     * @param power The power characteristc
     * @param shield The shield characteristc
     */
    public Ship(double posX, double posY, int width, int height, double speedCap, double acceleration, double power, double shield, double complexity) {
        super(ResourcesManager.getSpriteAsset("BaseShip", "images/ships/BaseShip.png",width,height));
        Ship.last_id++;
        this.id = Ship.last_id;
        this.setPosition(posX, posY);
        this.speedCap = speedCap;
        this.acceleration = acceleration;
        this.power = power;
        this.shield = shield;
        this.complexity = complexity;
        this.currentSpeed = 0;
        this.blindForward = 0;
        this.lastDir = 0;
        this.straightLine = false;
        this.getImageView().setImage(ResourcesManager.getSpriteAsset(assetReference(),getImagePath(),width,height).getImage());
    }

    /**
     * Returns its own class name.
     * @return its own class name
     */
    @Override
    abstract public String assetReference();
    
    
    /**
     * Returns this Sprite's image path
     * @return this Sprite's image path
     */
    public String getImagePath() {
        return "images/ships/"+assetReference()+".png";
    }

    /**
     * Switch this ship's owner.
     * @param owner new owner
     */
    public void changeOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Move the ship in a direction and rotate the sprite accordingly.
     * @param x the amount of x to add
     * @param y the amount of y to add
     */
    public void move(double x, double y) {
        double dir = Math.atan2(y, x);
        this.getImageView().setRotate(90 + Math.toDegrees(dir));
        this.setPosition(this.getPosX() + x, this.getPosY() + y);
    }

    /**
     * just dies.
     */
    public void die() {
        this.destroy();
    }

    /**
     * Makes the ship visible.
     * @param origin The starting planet
     */
    public void start(Planet origin) {
        this.initRender();
        this.getImageView().setVisible(true);
        ResourcesManager.colorImage(this, origin.getOwner().getColor());
    }

    /**
     * Returns the current ship's velocity.
     * @return the current ship's velocity
     */
    public double getVelocity() {
        return this.currentSpeed;
    }

    /**
     * Completely removes the velocity of the ship.
     */
    public void stop() {
        this.currentSpeed = 0;
    }

    /**
     * Makes the ship go quicker according to its abilities.
     */
    public void gaz() {
        if (this.currentSpeed < this.speedCap) {
            this.currentSpeed += this.acceleration;
        }
    }

    /**
     * Reverse gaz();
     */
    public void slowdown() {

        if (this.currentSpeed > 0) {
            this.currentSpeed -= this.acceleration;
        }

        if (this.currentSpeed < 0) {
            this.currentSpeed = 0;
        }
    }

    /**
     * Get the remaining amount of ticks before reprocessing the collisions.
     * @return the remaining amount of ticks before reprocessing the collisions.
     */
    public double getBlindForward() {
        return this.blindForward;
    }

    /**
     * set the remaining amount of ticks before reprocessing the collisions.
     * @param n the next amount
     */
    public void setBlindForward(double n) {
        this.blindForward = n;
    }

    /**
     * Updates the last facing direction.
     * @param d the last facing direction
     */
    public void setLastDir(double d) {
        this.lastDir = d;
    }

    /**
     * Returns the last facing direction.
     * @return the last facing direction.
     */
    public double getLastDir() {
        return this.lastDir;
    }

    /**
     * Whether this ship can go in a straight line without collision to arive to its destination. Skips collision processing.
     * @return true if it can
     */
    public boolean goesInStraightLine() {
        return this.straightLine;
    }

    /**
     * Indicates to the ship if he can go in a straight line
     * @param t the state
     */
    public void setStraightLine(boolean t) {
        this.straightLine = t;
    }
    
    /**
     * Removes shield depending on damage.
     * @param d the damage to take
     */
    public void takeDamage(double d) {
        this.shield -= d;
    }
    
    /**
     * Whether this ship is dead or not.
     * @return true if dead
     */
    public boolean isDead() {
        return this.shield <= 0;
    }
    
    /**
     * Inflict damage to another ship.
     * @param s the ship to aim at
     */
    public void attack(Ship s) {
        s.takeDamage(this.power);
    }
    

    /**
     * Allows the a sublass to change the ship's size. Does not take into
     * account the Galaxy's rules on purpose.
     *
     * @param percentage reduce by 50% = 0.5
     */
    protected void affectSize(double percentage) {
        if (percentage >= 0) {
            this.width *= percentage;
            this.height *= percentage;
            updateDimensions(getImagePath(), this.width, this.height);
        }
    }

    /**
     * Reprocesses the best direction to avoid collisions while going to a destination planet
     * @param destination The destination planet
     * @param dir The direction to output
     * @param angle The angle at which the destination is at
     */
    public void correctTrajectory(Planet destination, Point dir, double angle) {
        double sec_angle = angle;
        double sec_dir_x = dir.x;
        double sec_dir_y = dir.y;

        ArrayList<Planet> intersections = new ArrayList<Planet>();
        for (Planet p : Galaxy.getPlanets()) {
            if (p != destination && GameUtils.lineCrossingCircle(this.getPosXMiddle(), this.getPosYMiddle(), p.getPosXMiddle(), p.getPosYMiddle(), p.getPosXMiddle(), p.getPosYMiddle(), Galaxy.planetInfluenceZone)) {
                intersections.add(p);
            }
        }

        if (intersections.size() > 0) {
            if (this.getBlindForward() <= 0) {
                for (Planet p : intersections) {
                    if (p != destination) {
                        double FoV = Galaxy.planetSecurityZone;
                        boolean found = false;
                        while (!found) {
                            int tries = 0;
                            while (p.inOrbit(this.getPosXMiddle() + dir.x * FoV, this.getPosYMiddle() + dir.y * FoV)
                                    && p.inOrbit(this.getPosXMiddle() + sec_dir_x * FoV, this.getPosYMiddle() + sec_dir_y * FoV) && tries < 200) {
                                angle += 0.1;
                                sec_angle -= 0.1;

                                dir.x = Math.sin(angle);
                                dir.y = Math.cos(angle);
                                sec_dir_x = Math.sin(sec_angle);
                                sec_dir_y = Math.cos(sec_angle);

                                tries++;
                            }

                            if (p.inOrbit(this.getPosXMiddle() + dir.x * FoV, this.getPosYMiddle() + dir.y * FoV)) {
                                dir.x = sec_dir_x;
                                dir.y = sec_dir_y;
                                angle = sec_angle;
                            }

                            if (!p.inOrbit(this.getPosXMiddle() + dir.x * FoV, this.getPosYMiddle() + dir.y * FoV)) {
                                found = true;
                            } else {
                                FoV += 10;
                            }
                        }
                        this.setLastDir(angle);
                    }
                }
                //this.setBlindForward(Galaxy.planetSecurityZone / 4 + 1);
            } else {
                angle = this.getLastDir();
                dir.x = Math.sin(angle);
                dir.y = Math.cos(angle);
            }
            this.setBlindForward(this.getBlindForward() - 1);
        } else {
            this.setStraightLine(true);
        }

    }
    
    /**
     * Returns the current velocity of the ship.
     * @return the current velocity of the ship.
     */
    public double getCurrentSpeed() {
        return this.currentSpeed;
    }
    
    /**
     * Changes the current velocity.
     * @param s the new velocity
     */
    public void setCurrentSpeed(double s) {
        this.currentSpeed = s;
    }
    
    /**
     * Returns the current power of the ship.
     * @return the current power of the ship.
     */
    public double getPower() {
        return this.power;
    }
    
    /**
     * Returns the current shield of the ship.
     * @return the current shield of the ship.
     */
    public double getShield() {
        return this.shield;
    }
    
    /**
     * Returns the amount of production this ship needs to be produced
     * @return the complexity, or cost
     */
    public double getCost() {
        return this.complexity;
    }
    
    /**
     * Returns its own asset reference.
     * @return its own asset reference
     */
    @Override
    public String toString() {
        return this.assetReference();
    }
    
    /**
     * Returns its unique ID.
     * @return its unique ID
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Get a random ship type
     * @return a random ship asset reference
     */
    public static String getRandomShipType() {
        Random r = new Random();
        ArrayList<String> shipTypes = new ArrayList<>();
        
        shipTypes.add("BaseShip");
        shipTypes.add("BurstShip");
        shipTypes.add("MotherShip");
        
        String found = shipTypes.get(r.nextInt(shipTypes.size()));
        System.out.println(found);
        return found;
    }
}
