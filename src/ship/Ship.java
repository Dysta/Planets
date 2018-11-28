package ship;

import planets.ResourcesManager;
import planets.Sprite;
import planets.Entities.Player;

public abstract class Ship extends Sprite {

    private Player owner;
    protected double currentSpeed;
    protected double acceleration;
    protected double speedCap;

    public Ship(Sprite s, double posX, double posY, double speedCap, double acceleration) {
        super(s);
        this.setPosition(posX, posY);
        this.speedCap = speedCap;
        this.acceleration = acceleration;
        this.currentSpeed = 0;
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

    public void gaz() {
        if(this.currentSpeed < this.speedCap) {
            this.currentSpeed += this.acceleration;
        }
    }

    public void slowdown() {
        this.currentSpeed -= this.acceleration;
    }
}
