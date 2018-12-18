package planets_extended.entities.ship;

import planets_extended.ResourcesManager;
import planets_extended.Sprite;

/**
 * Just a basic Ship.
 * 
 * @author Adri
 */
public class BaseShip extends Ship {

    /**
     * Weak but quick ship
     * 
     * @param posX The top-left x position 
     * @param posY The top-left y position 
     */
    public BaseShip(double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration, power, shield
        super(posX, posY, 20, 20, 6, 0.03, 1, 1, 0.7);
    }
    
    /**
     * This ship's class name
     * @return its own class name
     */
    @Override
    public String assetReference() {
        return "BaseShip";
    }
}
