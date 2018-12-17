package planets.entities.planet;

import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Player;

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
     * @param posX Its x top-left position
     * @param posY Its y top-left position
     * @param size Its diameter
     */
    public BasePlanet(Sprite s, Player owner, double posX, double posY, double size) {
        super(s, owner, posX, posY, size);
        this.getImageView().setImage(ResourcesManager.assets.get("basePlanet").getImage());
    }

    /**
     * Exactly the same as superclass for a random planet.
     * 
     * @param get A Sprite object for a planet
     * @param p The planet's owner
     * @param width The sprite's width
     * @param height The sprite's height
     * @param minimumPlanetSize The minimum size to generate
     * @param maximumPlanetSize The maximum size to generate
     * @param borderMargin The margin to generate this planet correctly
     */
    public BasePlanet(Sprite get, Player p, double width, double height, double minimumPlanetSize, double maximumPlanetSize, double borderMargin) {
        super(get,p,width,height,minimumPlanetSize,maximumPlanetSize,borderMargin);
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
