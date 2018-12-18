package planets_extended.entities.ship;

/**
 * Just a basic Ship.
 * 
 * @author Adri
 */
public class BurstShip extends Ship {

    /**
     * Quick and cheap but weak
     * 
     * @param posX The top-left x position 
     * @param posY The top-left y position 
     */
    public BurstShip(double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration, power, shield, cost
        super(posX, posY, 20, 20, 25, 0.07, 0.4, 1, 0.2);
        this.affectSize(0.8);
    }
    
    /**
     * This ship's class name
     * @return its own class name
     */
    @Override
    public String assetReference() {
        return "BurstShip";
    }
}
