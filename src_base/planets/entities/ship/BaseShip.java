package planets.entities.ship;

import planets.ResourcesManager;
import planets.Sprite;

/**
 * Just a basic Ship.
 * 
 * @author Adri
 */
public class BaseShip extends Ship {

    /**
     * Weak but quick ship
     * 
     * @param s The reference Sprite
     * @param posX The top-left x position 
     * @param posY The top-left y position 
     */
    public BaseShip(Sprite s, double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration, power, shield
        super(s, posX, posY, 7, 0.04, 1, 1);
        this.getImageView().setImage(ResourcesManager.assets.get("baseShip").getImage());
    }
    
    /**
     * This ship's class name
     * @return its own class name
     */
    @Override
    public String assetReference() {
        return "baseShip";
    }
}
