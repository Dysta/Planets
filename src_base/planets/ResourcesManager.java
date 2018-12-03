package planets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ResourcesManager {

    // Common
    public static Image background;
    
    public final static String BG_PATH = "images/animated-background.gif";
    public final static String PLANET_PATH = "images/planet.png";
    
    // Common Sprites
    public static Map<String, Sprite> assets;

    // Ships        
    public final static String BASESHIP_PATH = "images/ships/BaseShip.png";

    /* =================== */
    public static String getRessourcePathByName(String name) {
        return Game.class.getResource('/' + name).toString();
    }

    /* ====== GAME ASSETS ====== */
    public static void initGameAssets(double width, double height) {
        ResourcesManager.assets = new HashMap<String, Sprite>();

        ResourcesManager.loadBackground(BG_PATH, width, height, false, false);
        ResourcesManager.loadSprite("planet", PLANET_PATH, 1600, 1600	, width, height);

        // Ships
        ResourcesManager.loadSprite("baseShip", BASESHIP_PATH, 20, 20, width, height);
    }

    private static void loadSprite(String name, String path, int width, int height, double maxX, double maxY) {
        System.out.println("Loaded "+path+"...");
        ResourcesManager.assets.put(name, new Sprite(getRessourcePathByName(path), width, height, maxX, maxY));
    }
    private static void loadBackground(String path, double width, double height, boolean preserveRatio, boolean smooth) {
        ResourcesManager.background = new Image(getRessourcePathByName(path), width, height, preserveRatio, smooth);
    }

    public static void colorImage(Sprite sprite, Color color) {
    	ImageView iv = sprite.getImageView();
        ColorAdjust effect = sprite.getColorAdjust();
        effect.setSaturation(1);
        double hue = color.getHue();
        if(hue>180) {
        	hue = -(360 - hue);
        }
        effect.setHue(hue/180);
        iv.setEffect(effect);
    }
    
    public static void highlightImage(Sprite sprite, double brightness) {
    	ImageView iv = sprite.getImageView();    	
    	ColorAdjust effect = sprite.getColorAdjust();
    	effect.setBrightness(brightness);
    	iv.setEffect(effect);
    }
}
