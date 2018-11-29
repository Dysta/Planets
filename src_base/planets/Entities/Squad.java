/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.Entities;

import java.util.ArrayList;
import planets.utils.Point;
import planets.utils.Polygon;
import ship.Ship;

/**
 *
 * @author Adri
 */
public class Squad {

    private Planet origin;
    private Planet destination;
    
    private ArrayList<Ship> ships;
    
    public Squad(Planet p1, Planet p2, ArrayList<Ship> ships) {
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
            //System.out.println("Dir x : "+p.x+" | Velocity : "+s.getVelocity());
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
        ArrayList<Point> points = new ArrayList<>();
        
        for(Ship s: this.ships) {
            points.add(new Point(s.getPosXMiddle(),s.getPosYMiddle()));
        }
        
        return new Polygon(points).contains(new Point(x,y));
    }
    
    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
}
