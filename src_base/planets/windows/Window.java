/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.windows;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 *
 * @author Adri
 */
abstract public class Window {
    
    public double WIDTH = 1280;
    public double HEIGHT = 720;
    public final static int STANDBY = -1;
    public final static int MAIN_MENU = 0;
    public final static int GAME = 1;
    public final static int QUIT = 2;
    public final static int LOAD = 3;
    public final static int LOADING = 4;

    // Attributes
    protected Stage stage;
    protected GraphicsContext gc;
    protected Canvas canvas;
    
    public static Group root;
    
    public void setStage(Stage stage_p, String title) {
        this.stage = stage_p;
        stage.setTitle(title);
        stage.setResizable(false);
    }
    abstract public void init(double WIDTH, double HEIGHT);
    
    
    public void hide() {
        stage.hide();
    }
    public void show() {
        stage.show();
    }
    public void clear() {
        root.getChildren().clear();
    }
}
