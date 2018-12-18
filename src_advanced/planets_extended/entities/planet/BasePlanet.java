package planets_extended.entities.planet;

import planets_extended.ResourcesManager;
import planets_extended.Sprite;
import planets_extended.entities.Player;

/**
 *  Just a basic planet
 * 
 * @author Adri
 */
public class BasePlanet extends Planet {
    
    /**
     * Usual constructor with a custom Image for this class
     * 
     * @param owner The planet's owner
     */
    public BasePlanet(Player owner) {
        super(owner, 20, 0.02, 50);
        this.getImageView().setImage(ResourcesManager.assets.get("basePlanet").getImage());
    }
    
    /**
     * Return its own classpath
     * 
     * @return its own classpath
     */
    @Override
    public String assetReference() {
        return "basePlanet";
    }
    
}
