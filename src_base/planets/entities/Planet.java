package planets.entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import planets.Game;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.ship.Ship;

public class Planet extends Sprite {

    private Sprite s;
    private Player owner;
    private double size;
    private Text text;
    private TextFlow tf;

    private int shipCapacity;
    private double shipsPerTick;
    private double productionProgression;

    private ArrayList<Ship> ships;

    public Planet(Sprite s, Player owner, double posX, double posY, double size) {
        super(s);
        this.s = s;
        this.owner = owner;
        this.setPosition(posX, posY);
        this.updateDimensions(ResourcesManager.PLANET_PATH, size, size);
        this.size = size;
        this.owner = new Player();
        this.ships = new ArrayList<Ship>();
        this.productionProgression = 20; // Base ships on any planet (will produce those ships instantly)
        this.shipsPerTick = 0.02; // Minimum production
        this.shipCapacity = 50; // Minimum storage

        // Components displaying current ships capacity
        this.text = new Text();
        this.tf = new TextFlow();
    }

    public Planet(Sprite s, Player owner, double boundaryX, double boundaryY, double minSize, double maxSize, double borderMargin) {
        this(s, owner, 0, 0, 0);

        Random r = new Random();
        double size = minSize + (maxSize - minSize) * r.nextDouble();
        double posX = (boundaryX - 2 * borderMargin - size) * r.nextDouble() + borderMargin;
        double posY = (boundaryY - 2 * borderMargin - size) * r.nextDouble() + borderMargin;
        
        this.shipsPerTick *= 1 + (size / (Galaxy.maximumPlanetSize * 2)); // The biggest planets can produce up to 50% more then smallest ones
        this.shipCapacity *= 1 + (size / (Galaxy.maximumPlanetSize * 1.2)); // The biggest planets can store up to 83% more then smallest ones

        this.setPosition(posX, posY);
        this.updateDimensions(ResourcesManager.PLANET_PATH, size, size);
        this.size = size;
    }

    public Planet(Planet s) {
        this(s.getSprite(), s.getOwner(), s.getPosX(), s.getPosY(), s.getSize());
        this.ships = s.getShips();
    }

    private void produceShip(String s) {
        double angle = Math.random() * Math.PI * 2;
        double radius = (this.size + (Galaxy.planetInfluenceZone - this.size) / 2);
        double x = ((this.getPosX() + this.getSize() / 2) + Math.cos(angle) * radius);
        double y = (this.getPosY() + this.getSize() / 2) + Math.sin(angle) * radius;
        try {
            Ship ship = (Ship) Class.forName(s).getConstructor(Sprite.class, double.class, double.class).newInstance(ResourcesManager.baseShip, x, y);
            ship.changeOwner(this.owner);
            ship.setPosition(ship.getPosX() - ship.width() / 2, ship.getPosY() - ship.height() / 2);
            ship.initRender(Game.root);
            ship.getImageView().setVisible(false);
            this.ships.add(ship);
        } catch (Exception e) {
            System.err.println("Error during ship initialization : " + e);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        ResourcesManager.colorImage(this.getImageView(), owner.getColor());
    }

    public void printStock(GraphicsContext gc, Group root) {
        this.text.setFont(gc.getFont());
        this.text.setFill(this.owner.getColor());
        this.text.setText("" + this.getNbShip());

        tf.setLayoutX(this.getPosXMiddle() - this.text.getLayoutBounds().getWidth() / 2);
        tf.setLayoutY(this.getPosYMiddle() - this.text.getLayoutBounds().getHeight() / 2);

        tf.getChildren().remove(this.text);
        root.getChildren().remove(tf);
        tf.getChildren().add(this.text);
        root.getChildren().add(tf);
    }

    public int getNbShip() {
        return this.ships.size();
    }
    
    public int getMaxLaunchShips() {
        if(this.ships.isEmpty()) {
            return 0;
        }
        return (int) ((this.size)*Math.PI / this.ships.get(0).width());
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public ArrayList<Ship> getShips() {
        return this.ships;
    }

    public Sprite getSprite() {
        return this.s;
    }

    public void productionTick() {
        if (this.owner.isActive() && this.shipCapacity > this.getNbShip()) {
            this.productionProgression += this.shipsPerTick;
        }

        while (this.productionProgression >= 1) {
            this.produceShip(this.owner.getShipType());
            this.productionProgression -= 1;
        }
    }

    public boolean isOn(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow(this.size / 2, 2);
    }

    public boolean inOrbit(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow((this.size / 2) + Galaxy.planetInfluenceZone, 2);
    }

    public boolean inOrbit(Ship s) {
        return this.inOrbit(s.getPosXMiddle(), s.getPosYMiddle());
    }

    public boolean inSecurityZone(double x, double y) {
        return Math.pow((x - this.getPosXMiddle()), 2) + Math.pow(y - this.getPosYMiddle(), 2) < Math.pow((this.size / 2) + Galaxy.planetSecurityZone, 2);
    }

    public boolean defend(ArrayList<Ship> attackers) {
        int c = 0;
        int a = attackers.size();
        int d = this.ships.size();
        while (c < a && c < d) {
            this.ships.get(0).die();
            this.ships.remove(0);
            attackers.get(0).die();
            attackers.remove(0);

            c++;
        }
        return c >= d;
    }
    
    public boolean freeToLaunch() {
        boolean free = true;
        for(Mission m : Game.missions) {
            if(m.getOriginPlanet() == this) {
                for(Squad s : m.getSquads()) {
                    if(!s.isGone()) {
                        free = false;
                    }
                }
            }
        }
        return free;
    }

    public void landShips(ArrayList<Ship> ships) {
        for (Ship s : ships) {
            if (this.getNbShip() < this.shipCapacity) {
                double angle = Math.random() * Math.PI * 2;
                double radius = (this.size + (Galaxy.planetInfluenceZone - this.size) / 2);
                double x = ((this.getPosX() + this.getSize() / 2) + Math.cos(angle) * radius);
                double y = (this.getPosY() + this.getSize() / 2) + Math.sin(angle) * radius;
                try {
                    s.stop();
                    s.setPosition(x - s.width() / 2, y - s.height() / 2);
                    s.getImageView().setVisible(false);
                    this.ships.add(s);
                } catch (Exception e) {
                    System.err.println("Error during ship transfer : " + e);
                }
            } else {
                s.die();
            }
        }
    }

    public ArrayList<Ship> flyShips(int effectives) {
        if (effectives > this.getNbShip()) {
            effectives = this.getNbShip();
        }

        ArrayList<Ship> mobilized = new ArrayList<>();
        for (int i = 0; i < effectives; i++) {
            Ship calledShip = this.ships.get(0);
            mobilized.add(calledShip);
            this.ships.remove(calledShip);

            double x = this.getPosXMiddle() + (Math.cos(((((double) 2 / effectives) * i) - 1) * Math.PI) * (this.size / 2));
            double y = this.getPosYMiddle() + (Math.sin(((((double) 2 / effectives) * i) - 1) * Math.PI) * (this.size / 2));

            calledShip.setMiddlePosition(x, y);
            calledShip.getImageView().setVisible(true);
        }

        return mobilized;
    }

}
