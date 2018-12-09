/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.entities.planet;

import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Player;

/**
 *
 * @author Adri
 */
public class BasePlanet extends Planet {
    
    public BasePlanet(Sprite s, Player owner, double posX, double posY, double size) {
        super(s, owner, posX, posY, size);
        this.getImageView().setImage(ResourcesManager.assets.get("basePlanet").getImage());
    }

    public BasePlanet(Sprite get, Player p, double width, double height, double minimumPlanetSize, double maximumPlanetSize, double borderMargin) {
        super(get,p,width,height,minimumPlanetSize,maximumPlanetSize,borderMargin);
    }
    
    public BasePlanet(Planet s) {
        super(s);
    }
    
    @Override
    public String assetReference() {
        return "basePlanet";
    }
    
}
