package planets.windows;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import planets.ResourcesManager;
import planets.ui.NumericField;
import planets.ui.WindowSwitchButton;

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
     * Asks the user for a number of planets before generating the game.
     */
    private static NumericField planetsField;

    /**
     * Keeps track of what menu we need to switch to.
     */
    private int selectedMenu;

    /**
     * Adds the needed elements to the window.
     */
    @Override
    public void initAfter() {
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
     * Get the number of planets, input by the user.
     * @return number of planets input by the user.
     */
    public int getNbPlanets() {
        return Integer.valueOf(Menu.planetsField.getText());
    }

    @Override
    public void setBackground() {
        this.background = ResourcesManager.getImageAsset("mainMenuBackground", "images/menu-background.png", WIDTH, HEIGHT);
    }
}
