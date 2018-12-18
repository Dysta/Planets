package planets_extended.entities.planet;

import planets_extended.ResourcesManager;
import planets_extended.Sprite;
import planets_extended.entities.Player;

/**
 *  Just a basic planet
 * 
 * @author Adri
 */
public class AdvancedPlanet extends Planet {
    
    /**
     * Usual constructor with a custom Image for this class
     * 
     * @param owner The planet's owner
     */
    public AdvancedPlanet(Player owner) {
        super(owner, 25, 0.03, 70);
        this.getImageView().setImage(ResourcesManager.assets.get("advancedPlanet").getImage());
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
