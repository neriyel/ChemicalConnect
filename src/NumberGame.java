import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NumberGame extends Application
{

    Label mainLabel;

    public NumberGame(final Label mainLabel)
    {
        this.mainLabel = mainLabel;
    }

    @Override
    public void start(final Stage stage) throws Exception
    {
        mainLabel = new Label("Test");
    }



    public static void main(String[] args)
    {
        Application.launch(args);
    }

}
