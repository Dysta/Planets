package ship;

import planets.ResourcesManager;
import planets.Sprite;

public class BaseShip extends Ship {

    public BaseShip(Sprite s, double posX, double posY) {
        // Ship , posX, posY, acceleration, capSpeed
        super(s, posX, posY, 0.02, 10);
        this.getImageView().setImage(ResourcesManager.baseShip.getImage());
    }
}
