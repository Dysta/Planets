package planets;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Sprite {

    private Image image;
    private ImageView iv;
    private double x;
    private double y;
    private double xSpeed;
    private double ySpeed;
    private double width;
    private double height;
    private double maxX;
    private double maxY;
    

    public Sprite(String path, double width, double height, double maxX, double maxY) {
        image = new Image(path, width, height, false, false);
        iv = new ImageView(this.image);
        this.width = width;
        this.height = height;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Sprite(Sprite s) {
        image = s.image;
        iv = new ImageView(this.image);
        width = s.width;
        height = s.height;
        maxX = s.maxX;
        maxY = s.maxY;
    }
    
    public String assetReference() {
        return "sprite";
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public void validatePosition() {
        if (x + width >= maxX) {
            x = maxX - width;
            xSpeed *= -1;
        } else if (x < 0) {
            x = 0;
            xSpeed *= -1;
        }

        if (y + height >= maxY) {
            y = maxY - height;
            ySpeed *= -1;
        } else if (y < 0) {
            y = 0;
            ySpeed *= -1;
        }
    }

    public final void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        validatePosition();
        this.iv.setX(this.x);
        this.iv.setY(this.y);
    }

    public final void setMiddlePosition(double x, double y) {
        this.setPosition(x - (this.width / 2), y - (this.height / 2));
    }

    public void setSpeed(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void changeSpeed(KeyCode code) {
        switch (code) {
            case LEFT:
                xSpeed--;
                break;
            case RIGHT:
                xSpeed++;
                break;
            case UP:
                ySpeed--;
                break;
            case DOWN:
                ySpeed++;
                break;
            case SPACE:
                ySpeed = xSpeed = 0;
                break;
            default:
        }
    }

    public void updatePosition() {
        x += xSpeed;
        y += ySpeed;
        validatePosition();
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public void initRender(Group root) {
        this.iv.setX(this.x);
        this.iv.setY(this.y);
        this.iv.setFitHeight(this.height);
        this.iv.setFitWidth(this.width);
        root.getChildren().add(iv);
    }

    public boolean intersects(Sprite s) {
        return ((x >= s.x && x <= s.x + s.width) || (s.x >= x && s.x <= x + width))
                && ((y >= s.y && y <= s.y + s.height) || (s.y >= y && s.y <= y + height));
    }

    public boolean isOn(double x, double y) {
        boolean on = false;

        on = x > this.x && x < this.x + this.width;
        on = y > this.y && y < this.y + this.height;

        return on;
    }

    public String toString() {
        return "Sprite<" + x + ", " + y + ">";
    }

    public final Image getImage() {
        return this.image;
    }

    public final ImageView getImageView() {
        return this.iv;
    }

    public double getPosX() {
        return this.x;
    }

    public double getPosY() {
        return this.y;
    }

    public double getPosXMiddle() {
        return this.x + (this.width / 2);
    }

    public double getPosYMiddle() {
        return this.y + (this.height / 2);
    }

    public void updateDimensions(String path, double width, double height) {
        this.width = width;
        this.height = height;
        this.image = new Image(path, width, height, false, false);
    }

    public void updateImage(Sprite s) {
        image = s.image;
        width = s.width;
        height = s.height;
        maxX = s.maxX;
        maxY = s.maxY;
    }
    
    public void setSelected(boolean s) {
        if(s) {
            System.out.println(this.assetReference()+"_outline");
            this.getImageView().setImage(ResourcesManager.assets.get(this.assetReference()+"_outline").getImage());
        } else {
            this.getImageView().setImage(ResourcesManager.assets.get(this.assetReference()).getImage());
        }
    }

}
