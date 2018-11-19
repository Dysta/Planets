package planets;

import alien.Game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import planets.Entities.Planet;

public class ResourcesManager {
	
	public final static String BG_PATH = "images/background.png";
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
		ResourcesManager.loadShip(SHIP_PATH, 343, 383, width, height);
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
	
	public static Planet colorPlanet(Planet s, Color color) {
		Planet cpy = new Planet(s);
		PixelReader pr = s.getImage().getPixelReader();
		WritableImage pw = new WritableImage(cpy.getImage().getPixelReader(), (int) cpy.width(), (int) cpy.height());
		

		for (int x = 0; x < (int) s.width(); x++) {
			for (int y = 0; y < (int) s.height(); y++) { 
		        Color couleur = pr.getColor(x, y); 
		        double b = couleur.getBlue();  
		        double g = couleur.getGreen();  
		        double r = couleur.getRed(); 
		        if (b == 1.0 && g == 1.0 && r == 1.0){ 
		          //si couleur noir => ne rien faire 
		        } 
				else
				{
					pw.getPixelWriter().setColor(x, y, color);
				}
			}
		}
		
		return cpy;
		
		
	}
	
	
