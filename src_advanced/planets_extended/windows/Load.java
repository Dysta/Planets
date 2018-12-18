/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets_extended.windows;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import planets_extended.IO.SaveManager;
import planets_extended.ResourcesManager;
import planets_extended.ui.WindowSwitchButton;

/**
 *  The Load window, displaying playable savegames.
 * 
 * @author Adri
 */
public class Load extends Window {

    /**
     * Allows to return to the Menu
     */
    private final Menu parent;

    /**
     * Basic constructor
     * @param parent the Menu
     */
    public Load(Menu parent) {
        this.parent = parent;
    }

    /**
     * Creates the actual menu of saves
     */
    @Override
    public void initAfter() {

        // Container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(15);
        //grid.setGridLinesVisible(true);

        int v = 0;
        for (File save : SaveManager.getSaveFiles()) {
            addLoadButton(grid, SaveManager.getSaveName(save), 0, v);
            v++;
        }

        // Back
        WindowSwitchButton back = new WindowSwitchButton("Back", this.parent, Window.MAIN_MENU);
        GridPane.setConstraints(back, 2, v);
        grid.getChildren().add(back);

        grid.setMaxWidth(GridPane.USE_PREF_SIZE);
        grid.setMaxHeight(GridPane.USE_PREF_SIZE);
        grid.setLayoutX((WIDTH / 3));
        grid.setLayoutY((HEIGHT / 4.5));
        Load.root.getChildren().add(grid);
    }

    /**
     * Adds a load button to a grid at a position
     * 
     * @param grid the grid to add to
     * @param label_text the save file name
     * @param h The horizontal position
     * @param v The vertical position
     * @return a new WindowSwitchButton
     */
    public WindowSwitchButton addLoadButton(GridPane grid, String label_text, int h, int v) {
        Label label = new Label();
        label.setTextFill(Color.WHITE);
        GridPane.setConstraints(label, h, v);
        GridPane.setColumnSpan(label, 2);
        label.setText(label_text);
        grid.getChildren().add(label);

        WindowSwitchButton button = new WindowSwitchButton("Play", this.parent, Window.LOAD);
        button.getText();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    ResourcesManager.initGameAssets(WIDTH, HEIGHT);
                } catch (Exception e) {
                    System.err.println("Failed to load ResourcesManager MenuAssets: " + e);
                }
                try {
                    parent.setSelectedWindow(Window.LOADING);
                    SaveManager.load(label_text);
                } catch (Exception ex) {
                    Logger.getLogger(Load.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        );
        GridPane.setConstraints(button, h + 3, v);
        grid.getChildren().add(button);

        return button;
    }

    @Override
    public void setBackground() {
        this.background = ResourcesManager.getImageAsset("gameBackground", "images/animated-background.gif", WIDTH, HEIGHT, true);
    }

}

