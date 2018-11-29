package ship;

import planets.Entities.Galaxy;
import planets.Entities.Planet;
import planets.ResourcesManager;
import planets.Sprite;
import planets.Entities.Player;

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
        double dir = Math.atan2(y,x);
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
        if(this.currentSpeed < this.speedCap) {
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
}