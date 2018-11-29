package planets.Entities;

import java.util.ArrayList;
import planets.utils.GameUtils;
import planets.utils.Point;

import ship.Ship;

public class Route {
    
    public static final String ATTACK = "ATTACK";
    public static final String CONVOY = "CONVOY";

    private ArrayList<Ship> ships;

    private final double viewDistance;

    private Planet origin;
    private Planet destination;

    private String mission;

    public Route(Planet p1, Planet p2, ArrayList<Ship> ships, String mission) {
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
        this.mission = mission;
    }

    public void move_ships() {
        ArrayList<Ship> arrivers = new ArrayList<Ship>();

        int c = 0;
        while (c < ships.size()) {
            Ship s = this.ships.get(c);

            double angle = Math.atan2(this.destination.getPosXMiddle() - s.getPosXMiddle(), this.destination.getPosYMiddle() - s.getPosYMiddle());
            s.gaz();

            Point p = new Point(Math.sin(angle), Math.cos(angle));
            
            if (!s.goesInStraightLine()) {
                this.correctTrajectory(s,p, angle);
            }

            s.move(p.x * s.getVelocity(), p.y * s.getVelocity());

            if (destination.inOrbit(s)) {
                arrivers.add(s);
                this.ships.remove(s);
            }

            c++;
        }

        switch (this.mission) {
            case Route.ATTACK:
                if (this.origin.getOwner() == this.destination.getOwner()) {
                    // The planet will gladly receive the ships, 
                    // as it's been conquered since the order to attack was given
                    this.mission = Route.CONVOY;
                }
                break;
            case Route.CONVOY:
                if (this.origin.getOwner() != this.destination.getOwner()) {
                    // Abort escort mission ! Units will have to fight
                    this.mission = Route.ATTACK;
                }
                break;
        }

        switch (this.mission) {
            case Route.ATTACK:
                if (this.destination.defend(arrivers)) {
                    this.destination.setOwner(this.origin.getOwner());
                }
                break;
            case Route.CONVOY:
                this.destination.landShips(arrivers);
                arrivers.clear();
                break;
        }
    }

    private void correctTrajectory(Ship s, Point dir, double angle) {
        double sec_angle = angle;
        double sec_dir_x = dir.x;
        double sec_dir_y = dir.y;

        ArrayList<Planet> intersections = new ArrayList<Planet>();
        for (Planet p : Galaxy.getPlanets()) {
            if (p != this.destination && GameUtils.lineCrossingCircle(s.getPosX(), s.getPosY(), p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY(), Galaxy.planetInfluenceZone)) {
                intersections.add(p);
            }
        }

        if (intersections.size() > 0) {
            if (s.getBlindForward() <= 0) {
                for (Planet p : intersections) {
                    if (p != this.destination) {
                        while (p.inOrbit(s.getPosX() + dir.x * this.viewDistance, s.getPosY() + dir.y * this.viewDistance)
                                && p.inOrbit(s.getPosX() + sec_dir_x * this.viewDistance, s.getPosY() + sec_dir_y * this.viewDistance)) {
                            angle += 0.1;
                            sec_angle -= 0.1;

                            dir.x = Math.sin(angle);
                            dir.y = Math.cos(angle);
                            sec_dir_x = Math.sin(sec_angle);
                            sec_dir_y = Math.cos(sec_angle);
                        }

                        if (p.inOrbit(s.getPosX() + dir.x * this.viewDistance, s.getPosY() + dir.y * this.viewDistance)) {
                            dir.x = sec_dir_x;
                            dir.y = sec_dir_y;
                            angle = sec_angle;
                        }
                        s.setLastDir(angle);
                    }
                }
                s.setBlindForward(this.viewDistance / 2 + 1);
            } else {
                angle = s.getLastDir();
                dir.x = Math.sin(angle);
                dir.y = Math.cos(angle);
            }
            s.setBlindForward(s.getBlindForward() - 1);
        } else {
            s.setStraightLine(true);
        }

    }

    public boolean isEmpty() {
        return this.ships.size() <= 0;
    }
}
