package planets.entities.ship;

import java.util.ArrayList;
import planets.entities.Galaxy;
import planets.entities.Planet;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.Player;
import planets.utils.GameUtils;
import planets.utils.Point;

public abstract class Ship extends Sprite {

    private Player owner;
    protected double currentSpeed;
    protected double acceleration;
    protected double speedCap;

    private double blindForward;
    private double lastDir;
    private boolean straightLine;

    public Ship(Sprite s, double posX, double posY, double speedCap, double acceleration) {
        super(s);
        this.setPosition(posX, posY);
        this.speedCap = speedCap;
        this.acceleration = acceleration;
        this.currentSpeed = 0;
        this.blindForward = 0;
        this.lastDir = 0;
        this.straightLine = false;
    }
    
    @Override
    public String assetReference() {
        return "baseShip";
    }

    public void changeOwner(Player owner) {
        this.owner = owner;
    }

    public void move(double x, double y) {
        double dir = Math.atan2(y, x);
        this.getImageView().setRotate(90 + Math.toDegrees(dir));
        this.setPosition(this.getPosX() + x, this.getPosY() + y);
    }

    public void die() {
        this.destroy();
    }

    public double getVelocity() {
        return this.currentSpeed;
    }

    public void stop() {
        this.currentSpeed = 0;
    }

    public void gaz() {
        if (this.currentSpeed < this.speedCap) {
            this.currentSpeed += this.acceleration;
        }
    }

    public void slowdown() {

        if (this.currentSpeed > 0) {
            this.currentSpeed -= this.acceleration;
        }

        if (this.currentSpeed < 0) {
            this.currentSpeed = 0;
        }
    }

    public double getBlindForward() {
        return this.blindForward;
    }

    public void setBlindForward(double n) {
        this.blindForward = n;
    }

    public void setLastDir(double d) {
        this.lastDir = d;
    }

    public double getLastDir() {
        return this.lastDir;
    }

    public boolean goesInStraightLine() {
        return this.straightLine;
    }

    public void setStraightLine(boolean t) {
        this.straightLine = t;
    }

    public void correctTrajectory(Ship s, Planet destination, Point dir, double angle) {
        double sec_angle = angle;
        double sec_dir_x = dir.x;
        double sec_dir_y = dir.y;

        ArrayList<Planet> intersections = new ArrayList<Planet>();
        for (Planet p : Galaxy.getPlanets()) {
            if (p != destination && GameUtils.lineCrossingCircle(s.getPosX(), s.getPosY(), p.getPosX(), p.getPosY(), p.getPosX(), p.getPosY(), Galaxy.planetInfluenceZone)) {
                intersections.add(p);
            }
        }

        if (intersections.size() > 0) {
            if (s.getBlindForward() <= 0) {
                for (Planet p : intersections) {
                    if (p != destination) {
                        double FoV = Galaxy.planetSecurityZone;
                        boolean found = false;
                        while (!found) {
                            int tries = 0;
                            while (p.inOrbit(s.getPosX() + dir.x * FoV, s.getPosY() + dir.y * FoV)
                                    && p.inOrbit(s.getPosX() + sec_dir_x * FoV, s.getPosY() + sec_dir_y * FoV) && tries < 200) {
                                angle += 0.1;
                                sec_angle -= 0.1;

                                dir.x = Math.sin(angle);
                                dir.y = Math.cos(angle);
                                sec_dir_x = Math.sin(sec_angle);
                                sec_dir_y = Math.cos(sec_angle);

                                tries++;
                            }

                            if (p.inOrbit(s.getPosX() + dir.x * FoV, s.getPosY() + dir.y * FoV)) {
                                dir.x = sec_dir_x;
                                dir.y = sec_dir_y;
                                angle = sec_angle;
                            }
                            
                            if (!p.inOrbit(s.getPosX() + dir.x * FoV, s.getPosY() + dir.y * FoV)) {
                                found = true;
                            } else {
                                FoV += 10;
                            }
                        }
                        s.setLastDir(angle);
                    }
                }
                //s.setBlindForward(Galaxy.planetSecurityZone / 4 + 1);
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
}
