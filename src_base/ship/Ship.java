package ship;

import java.util.ArrayList;
import planets.Entities.Galaxy;
import planets.Entities.Planet;
import planets.ResourcesManager;
import planets.Sprite;
import planets.Entities.Player;
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

    public void changeOwner(Player owner) {
        this.owner = owner;
    }

    public void move(double x, double y) {
        double dir = Math.atan2(y, x);
        this.getImageView().setRotate(90 + Math.toDegrees(dir));
        this.setPosition(this.getPosX() + x, this.getPosY() + y);
    }

    public void die() {
        this.getImageView().setImage(null);
    }

    public double getVelocity() {
        return this.currentSpeed;
    }

    public void stop() {
        this.currentSpeed = 0;
    }

    public void gaz() {
        if (this.currentSpeed < this.speedCap) {
            System.out.println(this.currentSpeed);
            this.currentSpeed += this.acceleration;
        }
    }

    public void slowdown() {
        this.currentSpeed -= this.acceleration;
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
                        int tries = 0;
                        while (p.inOrbit(s.getPosX() + dir.x * Galaxy.planetSecurityZone, s.getPosY() + dir.y * Galaxy.planetSecurityZone)
                                && p.inOrbit(s.getPosX() + sec_dir_x * Galaxy.planetSecurityZone, s.getPosY() + sec_dir_y * Galaxy.planetSecurityZone) && tries < 200) {
                            angle += 0.1;
                            sec_angle -= 0.1;

                            dir.x = Math.sin(angle);
                            dir.y = Math.cos(angle);
                            sec_dir_x = Math.sin(sec_angle);
                            sec_dir_y = Math.cos(sec_angle);

                            tries++;
                        }

                        if (p.inOrbit(s.getPosX() + dir.x * Galaxy.planetSecurityZone, s.getPosY() + dir.y * Galaxy.planetSecurityZone)) {
                            dir.x = sec_dir_x;
                            dir.y = sec_dir_y;
                            angle = sec_angle;
                        }
                        s.setLastDir(angle);
                    }
                }
                s.setBlindForward(Galaxy.planetSecurityZone / 4 + 1);
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
