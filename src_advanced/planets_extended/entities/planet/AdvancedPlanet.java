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
     * Create an Advanced Planet
     * 
     * @param owner The planet's owner
     */
    public AdvancedPlanet(Player owner) {
        super(owner, 900, 900, 25, 0.03, 70);
    }
    
    /**
     * Return its own classpath
     * 
     * @return its own classpath
     */
    @Override
    public String assetReference() {
        return "AdvancedPlanet";
    }
    
}
