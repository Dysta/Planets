package planets.Entities;

import java.util.ArrayList;

import ship.Ship;

public class Route {
    
    private ArrayList<Ship> ships;
    
    private Planet origin;
    private Planet destination;
    
    public Route(Planet p1, Planet p2, ArrayList<Ship> ships) {
        this.ships = new ArrayList<Ship>();
        
        int c = 0;
        int t = ships.size();
        while (c < t) {
            this.ships.add(ships.get(0));
            ships.remove(0);
            c++;
        }
        
        this.origin = p1;
        this.destination = p2;
    }
    
    public void move_ships() {
        ArrayList<Ship> attackers = new ArrayList<Ship>();
        
        int c = 0;
        while (c < ships.size()) {
            Ship s = this.ships.get(c);
            // TODO: fixed speed
            double angle = Math.atan2(this.destination.getPosXMiddle() - s.getPosXMiddle(),this.destination.getPosYMiddle() - s.getPosYMiddle());
            s.gaz();
            s.move(Math.sin(angle) * s.getVelocity(), Math.cos(angle) * s.getVelocity());
            
            if (destination.inOrbit(s)) {
                attackers.add(s);
            }
            
            c++;
        }
        
        c = 0;
        while (c < attackers.size()) {
            this.ships.remove(attackers.get(c));
            c++;
        }
        this.destination.defend(attackers);
    }
    
    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
}
