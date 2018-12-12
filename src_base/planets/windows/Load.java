/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.windows;

import java.awt.Event;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import planets.App;
import planets.IO.SaveManager;
import planets.ResourcesManager;
import ui.NumericField;
import ui.WindowSwitchButton;

/**
 *
 * @author Adri
 */
public class Load extends Window {

    private Menu parent;

    public Load(Menu parent) {
        this.parent = parent;
    }

    @Override
    public void init(double WIDTH, double HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        Group root = new Group();
        Scene scene = new Scene(root);
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);
        Menu.root = root;

        // Events
        gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        gc.setFill(Color.BISQUE);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);

        this.initLoad();
        gc.drawImage(ResourcesManager.menuBackground, 0, 0);

        stage.setScene(scene);
        stage.show();
    }

    public void initLoad() {

        // Container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(15);
        //grid.setGridLinesVisible(true);

        int v = 0;
        for (File save : SaveManager.getSaveFiles()) {
            System.out.println("Added button " + SaveManager.getSaveName(save));
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
                    SaveManager.load(null, label_text);
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

}
