package planets.tests;

import planets.entities.Galaxy;
import planets.entities.planet.Planet;

/**
 * Test for checking the Colliding Test for planets works as expected
 *
 * @author Adri
 */
public class NoPlanetCollidingTest extends Test {

    public NoPlanetCollidingTest(String name) {
        super(name);
    }

    @Override
    public boolean test() {
        Galaxy g;
        g = new Galaxy(Tests.WIDTH,Tests.HEIGHT,15,10,10);
        
        boolean colliding = false;
        for(Planet p : Galaxy.getPlanets()) {
            if(g.isColliding(p)) {
                colliding = true;
            }
        }
        
        return colliding;
    }
    
}
