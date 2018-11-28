package planets.Entities;

import java.util.ArrayList;

import ship.Ship;

public class Route {

    private ArrayList<Ship> ships;

    private final double viewDistance;
    private int blindForward;

    private Planet origin;
    private Planet destination;

    public Route(Planet p1, Planet p2, ArrayList<Ship> ships) {
        this.ships = new ArrayList<Ship>();

        int c = 0;
        int t = ships.size();
        while (c < t) {
            Ship s = ships.get(0);
            this.ships.add(s);
            ships.remove(s);
            
            s.getImageView().setVisible(true);
            c++;
        }

        this.origin = p1;
        this.destination = p2;
        this.viewDistance = Galaxy.planetSecurityZone;
    }

    public void move_ships() {
        ArrayList<Ship> attackers = new ArrayList<Ship>();

        int c = 0;
        while (c < ships.size()) {
            Ship s = this.ships.get(c);
            // TODO: fixed speed
            double angle = Math.atan2(this.destination.getPosXMiddle() - s.getPosXMiddle(), this.destination.getPosYMiddle() - s.getPosYMiddle());
            s.gaz();

            double dir_x = Math.sin(angle);
            double dir_y = Math.cos(angle);

            double sec_angle = angle;
            double sec_dir_x = Math.sin(sec_angle);
            double sec_dir_y = Math.cos(sec_angle);
            
            for (Planet p : Galaxy.getPlanets()) {
                if (p != this.destination) {
                    while (p.inOrbit(s.getPosX() + dir_x * this.viewDistance, s.getPosY() + dir_y * this.viewDistance)
                            && p.inOrbit(s.getPosX() + sec_dir_x * this.viewDistance, s.getPosY() + sec_dir_y * this.viewDistance)) {
                        angle += 0.1;
                        sec_angle -= 0.1;

                        dir_x = Math.sin(angle);
                        dir_y = Math.cos(angle);
                        sec_dir_x = Math.sin(sec_angle);
                        sec_dir_y = Math.cos(sec_angle);
                    }

                    if (p.inOrbit(s.getPosX() + dir_x * this.viewDistance, s.getPosY() + dir_y * this.viewDistance)) {
                        dir_x = sec_dir_x;
                        dir_y = sec_dir_y;
                    }
                }
            }

            s.move(dir_x * s.getVelocity(), dir_y * s.getVelocity());

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
        
        if(this.destination.defend(attackers)) {
            this.destination.setOwner(this.origin.getOwner());
            this.destination.addShips(this.ships);
            this.ships.clear();
        }
    }

    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
}
