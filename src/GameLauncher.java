import javafx.application.Application;

public class GameLauncher
{
    public static void launchGame(Class<? extends Application> gameClass)
    {
        Thread javafxThread = new Thread(() -> Application.launch(gameClass));
        javafxThread.setDaemon(true); // Ensures the JVM shuts down when the main thread exits
        javafxThread.start();
    }
}
