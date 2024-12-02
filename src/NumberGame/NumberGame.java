package NumberGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NumberGame extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        final FXMLLoader loader;
        final Scene      scene;

        loader = new FXMLLoader(getClass().getResource("NumberGame.fxml"));
        scene  = new Scene(loader.load(), 696, 420);

        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
