/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.entities;

import java.awt.Polygon;
import java.util.ArrayList;
import planets.utils.Point;
import planets.entities.ship.Ship;

/**
 *
 * @author Adri
 */
public class Squad {

    private Mission parent;
    private Planet origin;
    private Planet destination;
    
    private ArrayList<Ship> ships;
    
    public Squad(Planet p1, Planet p2, ArrayList<Ship> ships, Mission parent) {
        this.parent = parent;
        this.origin = p1;
        this.destination = p2;
        this.ships = ships;
    }
    
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
            }

            c++;
        }
        
        for(Ship s : arrivers) {
            this.ships.remove(s);
        }
        
        return arrivers;
    }
    
    public boolean isGone() {
        boolean gone = true;
        for(Ship s: this.ships) {
            if(this.origin.inOrbit(s)) {
                gone = false;
            }
        }
        return gone;
    }
    
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
    
    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
    
    public Mission getMission() {
        return this.parent;
    }
    
    public ArrayList<Ship> getShips() {
        return this.ships;
    }
    
    public void reaffectSquad(Mission newMission) {
        this.destination = newMission.getDestinationPlanet();
        this.parent.cancelSquad(this);
        this.parent = newMission;
    }
}
