package planets_extended.entities.ship;

/**
 * Just a basic Ship.
 * 
 * @author Adri
 */
public class MotherShip extends Ship {

    /**
     * Quick and cheap but weak
     * 
     * @param posX The top-left x position 
     * @param posY The top-left y position 
     */
    public MotherShip(double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration, power, shield, cost
        super(posX, posY, 20, 20, 6, 0.013, 4.5, 3, 2.5);
        this.affectSize(1.3);
    }
    
    /**
     * This ship's class name
     * @return its own class name
     */
    @Override
    public String assetReference() {
        return "MotherShip";
    }
}
