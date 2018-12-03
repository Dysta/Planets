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
    
    public final static String BG_PATH = "images/background.jpg";
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
        ResourcesManager.loadSprite("planet", PLANET_PATH, 435, 435, width, height, true);

        // Ships
        ResourcesManager.loadSprite("baseShip", BASESHIP_PATH, 20, 20, width, height, true);
    }

    private static void loadSprite(String name, String path, int width, int height, double maxX, double maxY, boolean outlined) {
        System.out.println("Loaded "+path+"...");
        ResourcesManager.assets.put(name, new Sprite(getRessourcePathByName(path), width, height, maxX, maxY));
        if(outlined) {
            ResourcesManager.assets.put(name+"_outline", new Sprite(getRessourcePathByName(path.substring(0,path.length()-4)+"_outline.png"), width, height, maxX, maxY));
            System.out.println("Loaded "+path.substring(0,path.length()-4)+"_outline.png"+"...");
        }
    }
    
    private static void loadSprite(String name, String path, int width, int height, double maxX, double maxY) {
        ResourcesManager.loadSprite(name, path, width, height, maxX, maxY, false);
    }

    private static void loadBackground(String path, double width, double height, boolean preserveRatio, boolean smooth) {
        ResourcesManager.background = new Image(getRessourcePathByName(path), width, height, preserveRatio, smooth);
    }

    public static void colorImage(ImageView iv, Color color) {
        ColorAdjust effect = new ColorAdjust();
        effect.setHue(color.getHue());
        iv.setEffect(effect);
    }
    
    public static void highlightImage(ImageView iv, double brightness) {
    	ColorAdjust effect = new ColorAdjust();
    	effect.setBrightness(brightness);
    	iv.setEffect(effect);
    }
}
