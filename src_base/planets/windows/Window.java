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
 *  Handles the display of itself
 * 
 * @author Adri
 */
abstract public class Window {
    
    /**
     * The windows' width
     */
    public double WIDTH = 1280;
    /**
     * The windows' height
     */
    public double HEIGHT = 720;
    
    /**
     * This state means nothing has to be done at the moment
     */
    public final static int STANDBY = -1;
    /**
     * This state means we need to display the main menu
     */
    public final static int MAIN_MENU = 0;
    /**
     * This state means we need to display the game
     */
    public final static int GAME = 1;
    /**
     * This state means we need to quit the application
     */
    public final static int QUIT = 2;
    /**
     * This state means we need to wait for the game to load
     */
    public final static int LOAD = 3;
    /**
     * This state means we need to display the save loading choice menu
     */
    public final static int LOADING = 4;
    /**
     * This state means we need to display the game over menu
     */
    public final static int RESULT_SCREEN = 5;

    /**
     * The windows' stage
     */
    public Stage stage;
    /**
     * The windows' graphics context
     */
    public GraphicsContext gc;
    /**
     * The windows' canvas for drawing the background
     */
    public Canvas canvas;
    
    /**
     * Regroups all the displayed imageviews
     */
    public static Group root;
    
    /**
     * Sets the current stage as well as some information about the window for the user
     * 
     * @param stage_p the stage to set and modify
     * @param title the window title for the user
     */
    public void setStage(Stage stage_p, String title) {
        this.stage = stage_p;
        stage.setTitle(title);
        stage.setResizable(false);
    }
    
    /**
     * Should be implemented by all windows even though the behavior can be similar : it is not in most cases.
     * @param WIDTH
     * @param HEIGHT 
     */
    abstract public void init(double WIDTH, double HEIGHT);
    
    
    /**
     * Do not display this window but keep its status.
     */
    public void hide() {
        stage.hide();
    }
    /**
     * Show this window to the user.
     */
    public void show() {
        stage.show();
    }
    /**
     * Terminates this windows' stage.
     */
    public void close() {
        stage.close();
    }
    /**
     * Removes every element from the root Group.
     */
    public void clear() {
        root.getChildren().clear();
    }
}
