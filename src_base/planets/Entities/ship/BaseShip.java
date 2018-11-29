package ship;

import planets.ResourcesManager;
import planets.Sprite;

public class BaseShip extends Ship {

    public BaseShip(Sprite s, double posX, double posY) {
        // Ship , posX, posY, capSpeed, acceleration
        super(s, posX, posY, 0.7, 0.004);
        this.getImageView().setImage(ResourcesManager.baseShip.getImage());
    }
}
