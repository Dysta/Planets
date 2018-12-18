package planets;

import planets.windows.Game;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ResourcesManager {
    // Common Sprites
    public static Map<String, Sprite> spriteAssets;
    public static Map<String, Image> imageAssets;

    /* =================== */
    public static String getRessourcePathByName(String name) {
        return Game.class.getResource('/' + name).toString();
    }

    /* ====== GAME ASSETS ====== */
    public static void initGlobalAssets(double width, double height) {
        ResourcesManager.imageAssets = new HashMap<>();
    }

    /* ====== GAME ASSETS ====== */
    public static void initGameAssets(double width, double height) {
        ResourcesManager.spriteAssets = new HashMap<>();
    }
    
    public static Image getImageAsset(String name, String path, double width, double height, boolean recalc) {
        Image ret = imageAssets.get(name);
        if(ret == null || recalc) {
            imageAssets.put(name, new Image(getRessourcePathByName(path), width, height, false, false));
            ret = imageAssets.get(name);
        }
        return ret;
    }
    
    public static Image getImageAsset(String name, String path, double width, double height) {
        return ResourcesManager.getImageAsset(name,path,width,height,false);
    }

    public static Sprite getSpriteAsset(String name, String path, int width, int height) {
        Sprite ret = spriteAssets.get(name);
        if(ret == null) {
            spriteAssets.put(name, new Sprite(getRessourcePathByName(path), width, height));
            ret = spriteAssets.get(name);
        }
        return ret;
    }

    public static void colorImage(Sprite sprite, Color color) {
        ImageView iv = sprite.getImageView();
        ColorAdjust effect = sprite.getColorAdjust();
        effect.setSaturation(1);
        double hue = color.getHue();
        if (hue > 180) {
            hue = -(360 - hue);
        }
        effect.setHue(hue / 180);
        iv.setEffect(effect);
    }

    public static void highlightImage(Sprite sprite, double brightness) {
        ImageView iv = sprite.getImageView();
        ColorAdjust effect = sprite.getColorAdjust();
        effect.setBrightness(brightness);
        iv.setEffect(effect);
    }
}
