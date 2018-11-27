package ship;

import planets.ResourcesManager;
import planets.Sprite;
import planets.Entities.Player;

public class Ship extends Sprite {

    private Player owner;
    protected double baseSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double speedCap;

    public Ship(Sprite s, double posX, double posY) {
        super(s);
        this.setPosition(posX, posY);
        this.baseSpeed = 0.5;
        this.speedCap = 10;
        this.currentSpeed = 0;
        this.acceleration = 0.15;
    }

    public void changeOwner(Player owner) {
        this.owner = owner;
    }

    public void move(double x, double y) {
        this.setPosition(this.getPosX() + x, this.getPosY() + y);
    }

    public void die() {
        this.getImageView().setImage(null);
    }

    public double getVelocity() {
        return this.currentSpeed;
    }

    public void gaz() {
        if(this.currentSpeed >= this.baseSpeed) {
            this.currentSpeed += this.acceleration;
        } else {
            this.currentSpeed = this.baseSpeed;
        }
        if(this.currentSpeed >= this.speedCap) {
            this.currentSpeed = this.speedCap;
        }
    }

    public void slowdown() {
        this.currentSpeed -= this.acceleration;
    }
}
