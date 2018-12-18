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
     * Create a basic planet
     * 
     * @param owner The planet's owner
     */
    public BasePlanet(Player owner) {
        super(owner, 900, 900, 20, 0.02, 50);
    }
    
    /**
     * Return its own classpath
     * 
     * @return its own classpath
     */
    @Override
    public String assetReference() {
        return "BasePlanet";
    }
    
}