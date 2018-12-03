package planets.entities.ship;

import planets.ResourcesManager;
import planets.Sprite;

public class BaseShip extends Ship {

    public BaseShip(Sprite s, double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration
        super(s, posX, posY, 7, 0.04);
        this.getImageView().setImage(ResourcesManager.assets.get("baseShip").getImage());
    }
    
    @Override
    public String assetReference() {
        return "baseShip";
    }
}
