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
     * Duplicator constructor with a custom Image for this class
     * 
     * @param s The Sprite object for a planet
     * @param owner The planet's owner
     */
    public BasePlanet(Sprite s, Player owner) {
        super(s, owner, 20, 0.02, 50);
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
