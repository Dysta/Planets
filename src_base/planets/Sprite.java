package planets;

import planets.windows.Game;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class should be extended by any Entity represented by an Image
 *
 * @author Adri
 */
public class Sprite {

    // Resources
    
    /**
     * Stores the untouched state of the base sprite.
     */
    private Image image;
    
    /**
     * Actually holds the image display, on top of handling repositionning
     * and applied effects.
     */
    private ImageView iv;
    
    /**
     * Stores the effects already applied to the ImageView, preventing different
     * effects from canceling each other.
     */
    private ColorAdjust ca;
    
    /**
     * The top-left x position of the Sprite in a 2D space.
     */
    private double x;
    
    /**
     * The top-left y position of the Sprite in a 2D space.
     */
    private double y;
    
    /**
     * The width of the sprite. Should correspond to the Image.
     */
    private double width;
    
    /**
     * The height of the sprite. Should correspond to the Image.
     */
    private double height;

    /**
     * 
     * This is the main constructor, it should only be called once for loading, 
     * then reused for reference when creating actual childs needing it.
     * 
     * @param path The path to a resources usable by an Image. @see javafx.scene.image.Image.
     * @param width The width of said image
     * @param height The height of said image
     */
    public Sprite(String path, double width, double height) {
        image = new Image(path, width, height, false, false);
        iv = new ImageView(this.image);
        this.width = width;
        this.height = height;
        this.ca = new ColorAdjust();
    }

    /**
     * 
     * Use this constructor to duplicate the Sprite.
     * Creates a new ImageView, allowing the display of another Entity while
     * reusing the previously loaded Image resource.
     * 
     * @param s The Sprite to duplicate
     */
    public Sprite(Sprite s) {
        image = s.image;
        iv = new ImageView(this.image);
        width = s.width;
        height = s.height;
        this.ca = new ColorAdjust();
    }

    /**
     * 
     * This method should be Overriden by any children in reference to their
     * indexed Image in ResourcesManager. @see ResourcesManager.
     * 
     * @return the index keyword
     */
    public String assetReference() {
        return "sprite";
    }

    /**
     * Fetch this Sprite's width
     * 
     * @return a double representing this Sprite's width
     */
    public double width() {
        return width;
    }

    /**
     * Fetch this Sprite's height
     * 
     * @return a double representing this Sprite's height
     */
    public double height() {
        return height;
    }

    /**
     * Changes the display position of this Sprite relative to its top-left. 
     * Will have an incidence on the actual display on the next frame.
     * 
     * @param x The new top-left X position
     * @param y The new top-left Y position
     */
    public final void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.iv.setX(this.x);
        this.iv.setY(this.y);
    }

    /**
     * Changes the display position of this Sprite relative to its middle. 
     * Will have an incidence on the actual display on the next frame.
     * 
     * @param x The new middle X position
     * @param y The new middle Y position
     */
    public final void setMiddlePosition(double x, double y) {
        this.setPosition(x - (this.width / 2), y - (this.height / 2));
    }

    /**
     * Should only be ran once, or after destroy(). 
     * Adds this Sprite's ImageView to the game's main Group, thus displaying it.
     */
    public void initRender() {
        this.iv.setX(this.x);
        this.iv.setY(this.y);
        this.iv.setFitHeight(this.height);
        this.iv.setFitWidth(this.width);
        Game.root.getChildren().add(this.iv);
    }

    /**
     * Removes this Sprite's ImageView from the game's main Group.
     */
    public void destroy() {
        Game.root.getChildren().remove(this.iv);
    }

    /**
     * Checks whether this sprite shares space with another. Doesn't take into
     * account the target sprite's image transparency.
     * 
     * @param s The sprite to test
     * @return the check result
     */
    public boolean intersects(Sprite s) {
        return ((x >= s.x && x <= s.x + s.width) || (s.x >= x && s.x <= x + width))
                && ((y >= s.y && y <= s.y + s.height) || (s.y >= y && s.y <= y + height));
    }

    /**
     * Checks whether the point represented by the X and Y parameters is in
     * this Sprite's space.
     * 
     * @param x The X coordinate
     * @param y The X coordinate
     * @return true if the point is in this Sprite's boundaries (non inclusive)
     */
    public boolean isOn(double x, double y) {
        boolean on = false;

        on = x > this.x && x < this.x + this.width;
        on = y > this.y && y < this.y + this.height;

        return on;
    }

    /**
     * Gives a string representation of this Sprite's state.
     * 
     * @return A string in the format of 'Sprite&lt;x,y&gt;'
     */
    public String toString() {
        return "Sprite<" + x + ", " + y + ">";
    }

    /**
     * Get the original Sprite image.
     * 
     * @return a reference to the originally loaded Image object
     */
    public final Image getImage() {
        return this.image;
    }

    /**
     * Get this Sprite's ImageView.
     * 
     * @return an ImageView object
     */
    public final ImageView getImageView() {
        return this.iv;
    }

    /**
     * Get this Sprite's top-left X position.
     * 
     * @return top-left X
     */
    public double getPosX() {
        return this.x;
    }

    /**
     * Get this Sprite's top-left Y position.
     * 
     * @return top-left Y
     */
    public double getPosY() {
        return this.y;
    }

    /**
     * Get this Sprite's middle X position.
     * 
     * @return middle X
     */
    public double getPosXMiddle() {
        return this.x + (this.width / 2);
    }

    /**
     * Get this Sprite's middle Y position.
     * 
     * @return middle Y
     */
    public double getPosYMiddle() {
        return this.y + (this.height / 2);
    }

    /**
     * Change this Sprite's Image dimensions, caution : this loads a new Image in memory and this doesn't update the ImageView.
     * 
     * @param path The path to the newly created Image
     * @param width The new width
     * @param height The new height
     */
    public void updateDimensions(String path, double width, double height) {
        this.width = width;
        this.height = height;
        this.image = new Image(path, width, height, false, false);
    }

    /**
     * Copies another Sprite's Image reference and dimensions, this doesn't update the ImageView.
     * 
     * @param s The Sprite containing the Image to reference
     */
    public void updateImage(Sprite s) {
        image = s.image;
        width = s.width;
        height = s.height;
    }

    /**
     * Applies a Highlight effect to this Sprite, hinting to the user that
     * it's selected.
     * 
     * @param s Whether this Sprite should be highlighted or not.
     */
    public void setSelected(boolean s) {
        if (s) {
            ResourcesManager.highlightImage(this, +0.2);
        } else {
            ResourcesManager.highlightImage(this, 0);
        }
    }

    /**
     * Returns the latest state of the ColorAdjust effect applied to the ImageView in ResourcesManager Used to stack effects instead on each other.
     * 
     * @return the latest ColorAdjust Effect applied to the ImageView
     */
    public ColorAdjust getColorAdjust() {
        return this.ca;
    }

}
