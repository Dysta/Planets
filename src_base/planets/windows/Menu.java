package planets.windows;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import planets.ResourcesManager;
import ui.NumericField;
import ui.WindowSwitchButton;

/**
 *
 * @author Adri
 */
public class Menu extends Window {

    private static NumericField playersField;
    private static NumericField planetsField;

    private int selectedMenu;

    // GraphicsContext is defined by the game
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

        this.initMenu();
        gc.drawImage(ResourcesManager.menuBackground, 0, 0);

        stage.setScene(scene);
        stage.show();
    }

    public void initMenu() {
        this.selectedMenu = Window.STANDBY;
        // Container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(15);

        playersField = addNumericTextField(grid, "Max. Players", 0, 0, 2);
        planetsField = addNumericTextField(grid, "Max. Planets", 0, 1, 7);
        

        // Play
        WindowSwitchButton play = new WindowSwitchButton("Start", this, Window.GAME);
        GridPane.setConstraints(play, 5, 3);
        grid.getChildren().add(play);
        GridPane.setHalignment(play, HPos.RIGHT);

        // Load
        WindowSwitchButton load = new WindowSwitchButton("Load", this, Window.LOAD);
        GridPane.setConstraints(load, 2, 3);
        grid.getChildren().add(load);

        // Quit
        WindowSwitchButton quit = new WindowSwitchButton("Quit", this, Window.QUIT);
        GridPane.setConstraints(quit, 0, 3);
        grid.getChildren().add(quit);
        
        grid.setMaxWidth(GridPane.USE_PREF_SIZE);
        grid.setMaxHeight(GridPane.USE_PREF_SIZE);
        grid.setLayoutX((WIDTH/3.3));
        grid.setLayoutY((HEIGHT/4.5));
        Menu.root.getChildren().add(grid);
    }

    public NumericField addNumericTextField(GridPane grid, String label_text, int h, int v, int baseValue) {
        Label label = new Label();
        label.setTextFill(Color.WHITE);
        GridPane.setConstraints(label, h, v);
        GridPane.setColumnSpan(label, 2);
        label.setText(label_text);
        grid.getChildren().add(label);

        NumericField field = new NumericField(baseValue);
        field.setPrefColumnCount(3);
        field.getText();
        GridPane.setConstraints(field, h + 5, v);
        grid.getChildren().add(field);

        return field;
    }

    public void setSelectedWindow(int s) {
        this.selectedMenu = s;
    }

    public int getSelectedWindow() {
        return this.selectedMenu;
    }

    public int getNbPlayers() {
        return Integer.valueOf(Menu.playersField.getText());
    }

    public int getNbPlanets() {
        return Integer.valueOf(Menu.planetsField.getText());
    }
}
