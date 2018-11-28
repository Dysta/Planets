package ship;

import planets.ResourcesManager;
import planets.Sprite;

public class Destroyer extends Ship {
    
    public Destroyer(Sprite s, double posX, double posY) {
        // Ship , posX, posY, speedCap, acceleration
        super(s, posX, posY, 15, 0.01);
        this.getImageView().setImage(ResourcesManager.destroyer.getImage());
    }
}
