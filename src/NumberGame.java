import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Random;

public class NumberGame extends Application
{
    private static       int                   NUM_BUTTONS = 20;
    private static final UnclickedGridButton[] buttons     = new UnclickedGridButton[20];

    private int rand;

    public static class GlobalNodes
    {
        public static final Label mainLabel = new Label();
        public static       Scene mainScene = null;

        static
        {
            // Create 20 buttons
            for(int i = 0; i < NUM_BUTTONS; i++)
            {
                buttons[i] = new UnclickedGridButton("[]");
            }
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        generateRandomInt();
        setScene();

        primaryStage.setTitle("Number Game");
        primaryStage.setScene(GlobalNodes.mainScene);
        primaryStage.show();
    }

    public int generateRandomInt()
    {
        Random random = new Random();
        rand = random.nextInt(1000) + 1;

        return rand;
    }

    public void setScene()
    {
        GlobalNodes.mainLabel.setText("Select a slot for the next number: " + rand);

        VBox     layout = new VBox(10);
        GridPane grid   = new GridPane();

        // Set alignment
        layout.setAlignment(Pos.CENTER);

        // Set GridPane to fill the whole screen
        grid.setHgap(10);
        grid.setVgap(10);

        // Add all 20 buttons to the grid
        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            int                 row    = i / 5;
            int                 col    = i % 5;
            UnclickedGridButton button = buttons[i];

            button.setMaxWidth(Double.MAX_VALUE);  // Make button expand horizontally
            button.setMaxHeight(Double.MAX_VALUE); // Make button expand vertically
            grid.add(button, col, row);  // Add button at calculated column and row
        }

        // Set Column Constraints (equally divided)
        for(int i = 0; i < 5; i++)
        { // 5 columns for 20 buttons
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / 5);  // Set each column to take 20% of the width
            grid.getColumnConstraints().add(colConstraints);
        }

        // Set Row Constraints (equally divided)
        for(int i = 0; i < 4; i++)
        { // 4 rows for 20 buttons
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 4);  // Set each row to take 25% of the height
            grid.getRowConstraints().add(rowConstraints);
        }

        // Add nodes to layout
        layout.getChildren().addAll(GlobalNodes.mainLabel, grid);

        // Make the VBox fill the entire window and adjust with resizing
        VBox.setVgrow(grid, Priority.ALWAYS);  // Ensure the grid fills all available vertical space
        layout.setFillWidth(true);             // Ensure VBox fills all available width

        GlobalNodes.mainScene = new Scene(layout, 696, 420);

    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }

}
