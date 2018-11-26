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
	
	public final static String BG_PATH = "images/background.jpg";
	public final static String SHIP_PATH = "images/ship.png";	
	public final static String PLANET_PATH = "images/planet.png";
	
	public static Sprite ship;
	public static Sprite planet;
	public static Image background;	
	
	/* =================== */
	
	public static String getRessourcePathByName(String name) {
		return Game.class.getResource('/' + name).toString();
	}
	
	/* ====== GAME ASSETS ====== */

	public static void initGameAssets(double width, double height) {
		ResourcesManager.loadBackground(BG_PATH, width, height, false, false);
		ResourcesManager.loadPlanet(PLANET_PATH, 435, 435, width, height);
		ResourcesManager.loadShip(SHIP_PATH, 20, 35, width, height);
	}
	
	private static void loadBackground(String path, double width, double height, boolean preserveRatio, boolean smooth) {
		ResourcesManager.background = new Image(getRessourcePathByName(path), width, height, preserveRatio, smooth);
	}
	
	private static void loadShip(String path, int width, int height, double maxX, double maxY) {
		ResourcesManager.ship = new Sprite(getRessourcePathByName(path), width, height, maxX, maxY);
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
	
	
