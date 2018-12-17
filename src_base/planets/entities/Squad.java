package planets.entities;

import planets.entities.planet.Planet;
import java.awt.Polygon;
import java.util.ArrayList;
import planets.utils.Point;
import planets.entities.ship.Ship;

/**
 *  A squadron of ships with a goal to reach and a mission to perform.
 * 
 * @author Adri
 */
public class Squad {

    /**
     * The mission these ships have to perform.
     */
    private Mission parent;
    
    /**
     * The planet these ships come from.
     */
    private final Planet origin;
    
    /**
     * The destination they have to reach.
     */
    private Planet destination;
    
    /**
     * The ships for this squadron.
     */
    private ArrayList<Ship> ships;
    
    /**
     * Creates a squadron from the given parameters
     * 
     * @param p1 The origin planet
     * @param p2 The destination planet
     * @param ships The collection of ships to use
     * @param parent The mission to perform
     */
    public Squad(Planet p1, Planet p2, ArrayList<Ship> ships, Mission parent) {
        this.parent = parent;
        this.origin = p1;
        this.destination = p2;
        this.ships = ships;
    }
    
    /**
     * Make all ships advance one step close to their destination, as well as determining those who finally reached it.
     * 
     * @return The ships who have reached the destination planet.
     */
    public ArrayList<Ship> progress() {
        ArrayList<Ship> arrivers = new ArrayList<>();
        
        int c = 0;
        while (c < ships.size()) {
            Ship s = this.ships.get(c);

            double angle = Math.atan2(this.destination.getPosXMiddle() - s.getPosXMiddle(), this.destination.getPosYMiddle() - s.getPosYMiddle());

            Point p = new Point(Math.sin(angle), Math.cos(angle));
            
            if (!s.goesInStraightLine()) {
                s.correctTrajectory(s, this.destination, p, angle);
            }

            s.gaz();
            s.move(p.x * s.getVelocity(), p.y * s.getVelocity());

            if (destination.isOn(s.getPosXMiddle(),s.getPosYMiddle())) {
                arrivers.add(s);
                this.ships.remove(s);
            }

            c++;
        }
        
        return arrivers;
    }
    
    /**
     * Checks whether this squad is still in the influence zone of a planet.
     * 
     * @return true if at least one ship is still in the influence zone.
     */
    public boolean isGone() {
        boolean gone = true;
        for(Ship s: this.ships) {
            if(this.origin.inOrbit(s)) {
                gone = false;
            }
        }
        return gone;
    }
    
    /**
     * Checks whether a point is in a polygone drawn by all ships positions
     * 
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return true if the point is indeed in between this squad's ships
     */
    public boolean isOn(double x, double y) {
        int[] x_ = new int[this.ships.size()];
        int[] y_ = new int[this.ships.size()];
        
        int i = 0;
        for(Ship s: this.ships) {
            x_[i] = (int) s.getPosXMiddle();
            y_[i] = (int) s.getPosYMiddle();
            i++;
        }
        return new Polygon(x_,y_,ships.size()).contains(x, y);
    }
    
    /**
     * Checks if this squad doesn't have any ship.
     * 
     * @return true if it is empty.
     */
    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
    
    /**
     * Returns a reference to this squad's current mission.
     * 
     * @return a Mission object
     */
    public Mission getMission() {
        return this.parent;
    }
    
    /**
     * Return a reference to this squad's ships collection.
     * 
     * @return a ship collection
     */
    public ArrayList<Ship> getShips() {
        return this.ships;
    }
    
    /**
     * Change the mission of this squad
     * 
     * @param newMission The new mission to perform
     */
    public void reaffectSquad(Mission newMission) {
        this.destination = newMission.getDestinationPlanet();
        this.parent.cancelSquad(this);
        this.parent = newMission;
    }
}
