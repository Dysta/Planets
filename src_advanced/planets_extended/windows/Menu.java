package planets_extended.windows;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import planets_extended.ResourcesManager;
import planets_extended.ui.NumericField;
import planets_extended.ui.WindowSwitchButton;

/**
 *  The main menu window
 * 
 * @author Adri
 */
public class Menu extends Window {

    /**
     * Asks the user for a number of players before generating the game.
     */
    private static NumericField playersField;
    /**
     * Asks the user for a number of planets_extended before generating the game.
     */
    private static NumericField planets_extendedField;

    /**
     * Keeps track of what menu we need to switch to.
     */
    private int selectedMenu;

    /**
     * Initializes the application window 
     * 
     * @param WIDTH the width of the window
     * @param HEIGHT the height of the window
     */
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

    /**
     * Adds the needed elements to the window.
     */
    public void initMenu() {
        this.selectedMenu = Window.STANDBY;
        // Container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(15);

        playersField = addNumericTextField(grid, "Max. Players", 0, 0, 2);
        planets_extendedField = addNumericTextField(grid, "Max. Planets", 0, 1, 7);
        

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

    /**
     * Correctly adds a NumericField to a grid
     * @param grid the grid to add to
     * @param label_text the text on the Label
     * @param h the horizontal start position
     * @param v the vertical position
     * @param baseValue the default valued in the numeric field
     * @return a new NumericField
     */
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

    /**
     * Changes the selected window, directly causing the code in the Planets timer for windows switching to be executed.
     * @param s the new window
     */
    public void setSelectedWindow(int s) {
        this.selectedMenu = s;
    }

    /**
     * Returns the currently selected menu.
     * @return the currently selected menu.
     */
    public int getSelectedWindow() {
        return this.selectedMenu;
    }

    /**
     * Get the number of players, input by the user.
     * @return number of players input by the user.
     */
    public int getNbPlayers() {
        return Integer.valueOf(Menu.playersField.getText());
    }

    /**
     * Get the number of planets_extended, input by the user.
     * @return number of planets_extended input by the user.
     */
    public int getNbPlanets() {
        return Integer.valueOf(Menu.planets_extendedField.getText());
    }
}