import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Singleton class to manage global state
class GlobalState {
    private static GlobalState instance; // Singleton instance
    private final StringProperty labelText; // Observable property for label text

    private GlobalState() {
        labelText = new SimpleStringProperty("Initial Text");
    }

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public void setLabelText(String newText) {
        labelText.set(newText);
    }

    public String getLabelText() {
        return labelText.get();
    }
}

public class GlobalLabelApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Access the singleton instance
        GlobalState state = GlobalState.getInstance();

        // Create a label and bind it to the global state
        Label globalLabel = new Label();
        globalLabel.textProperty().bind(state.labelTextProperty());

        // Create buttons to change the label's text
        Button changeTextButton = new Button("Change Text");
        changeTextButton.setOnAction(e -> state.setLabelText("Text Changed!"));

        Button resetTextButton = new Button("Reset Text");
        resetTextButton.setOnAction(e -> state.setLabelText("Initial Text"));

        // Add everything to a layout
        VBox layout = new VBox(10, globalLabel, changeTextButton, resetTextButton);
        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Global Label Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
