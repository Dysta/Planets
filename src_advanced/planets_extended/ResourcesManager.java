package planets_extended;

import planets_extended.windows.Game;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ResourcesManager {

    // Common
    public static Image background;
    public static Image menuBackground;
    public static Image gameOverBackground;
    public static Image loadBackground;

    public final static String BG_PATH = "images/animated-background.gif";
    public final static String MENUBG_PATH = "images/menu-background.png";
    public final static String GAME_OVER_BG_PATH = "images/game-over.png";
    public final static String LOAD_BG_PATH = "images/load-background.png";

    // Common Sprites
    public static Map<String, Sprite> assets;

    // Planets
    public final static String PLANET_PATH = "images/planets/BasePlanet.png";

    // Ships        
    public final static String BASESHIP_PATH = "images/ships/BaseShip.png";

    /* =================== */
    public static String getRessourcePathByName(String name) {
        return Game.class.getResource('/' + name).toString();
    }

    /* ====== GAME ASSETS ====== */
    public static void initGlobalAssets(double width, double height) {
        ResourcesManager.menuBackground = new Image(getRessourcePathByName(MENUBG_PATH), width, height, false, false);
        ResourcesManager.gameOverBackground = new Image(getRessourcePathByName(GAME_OVER_BG_PATH), width, height, false, false);
        ResourcesManager.loadBackground = new Image(getRessourcePathByName(LOAD_BG_PATH), width, height, false, false);
    }

    /* ====== GAME ASSETS ====== */
    public static void initGameAssets(double width, double height) {
        ResourcesManager.assets = new HashMap<String, Sprite>();
        ResourcesManager.background = new Image(getRessourcePathByName(BG_PATH), width, height, false, false);

        ResourcesManager.loadSprite("basePlanet", PLANET_PATH, 1600, 1600);

        // Ships
        ResourcesManager.loadSprite("baseShip", BASESHIP_PATH, 20, 20);
    }

    private static void loadSprite(String name, String path, int width, int height) {
        ResourcesManager.assets.put(name, new Sprite(getRessourcePathByName(path), width, height));
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
