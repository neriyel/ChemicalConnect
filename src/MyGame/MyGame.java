package MyGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyGame extends Application
{

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader loader;
        Scene      scene;

        loader = new FXMLLoader(getClass().getResource("MyGame.fxml"));
        scene  = new Scene(loader.load(), 420, 696);
        primaryStage.setResizable(false);
        primaryStage.setTitle("My Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
