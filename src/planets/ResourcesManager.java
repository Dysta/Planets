package planets;


import alien.Game;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ResourcesManager {
	
        // Common
	public final static String BG_PATH = "images/background.jpg";
	public final static String PLANET_PATH = "images/planet.png";
        // Common Sprites
	public static Sprite planet;
	public static Image background;	
        
        // Ships        
	public final static String BASESHIP_PATH = "images/ships/BaseShip.png";	
	public final static String DESTROYER_PATH = "images/ships/Destroyer.png";
        // Ships Sprites
	public static Sprite baseShip;
	public static Sprite destroyer;	
	
	
	/* =================== */
	
	public static String getRessourcePathByName(String name) {
		return Game.class.getResource('/' + name).toString();
	}
	
	/* ====== GAME ASSETS ====== */

	public static void initGameAssets(double width, double height) {
		ResourcesManager.loadBackground(BG_PATH, width, height, false, false);
		ResourcesManager.loadPlanet(PLANET_PATH, 435, 435, width, height);
                
                // Ships
		ResourcesManager.loadBaseShip(BASESHIP_PATH, 20, 20, width, height);
		ResourcesManager.loadDestroyer(DESTROYER_PATH, 20, 20, width, height);
	}
	
	private static void loadBackground(String path, double width, double height, boolean preserveRatio, boolean smooth) {
		ResourcesManager.background = new Image(getRessourcePathByName(path), width, height, preserveRatio, smooth);
	}
	
	private static void loadBaseShip(String path, int width, int height, double maxX, double maxY) {
		ResourcesManager.baseShip = new Sprite(getRessourcePathByName(path), width, height, maxX, maxY);
	}
	
	private static void loadDestroyer(String path, int width, int height, double maxX, double maxY) {
		ResourcesManager.destroyer = new Sprite(getRessourcePathByName(path), width, height, maxX, maxY);
	}
	
	private static void loadPlanet(String path, int width, int height, double maxX, double maxY) {
		ResourcesManager.planet = new Sprite(getRessourcePathByName(path), width, height, maxX, maxY);
	}
	
	public static void colorImage(ImageView iv, Color color) {
		ColorAdjust effect = new ColorAdjust();
		
		System.out.println("Color: "+color.toString() + " | Hue: "+color.getHue()+ " | Brightness: "+ color.getBrightness()+ " | Saturation: "+ color.getSaturation());
		iv.setEffect(effect);
	}
}
	
	
