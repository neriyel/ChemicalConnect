import MyGame.MyGameController;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MyGameTest {
    private MyGameController controller;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize JavaFX runtime
        new JFXPanel();

        // Load FXML and initialize controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyGame.fxml"));
        loader.load();
        controller = loader.getController();
    }

    @Test
    void testInitializeMethod() {
        // Ensure no exception is thrown when initialize() is called
        assertDoesNotThrow(() -> controller.initialize());
    }
}
