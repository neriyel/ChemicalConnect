package MyGame;

import javafx.scene.paint.Color;

public class Carbon extends GameElement
{

    // Static variables
    private static Color CARBON_COLOR  = Color.BLUE;
    private static int   CARBON_RADIUS = 10;

    // Instance variables (final)

    public Carbon(final double x, final double y)
    {
        super(x, y, CARBON_RADIUS, CARBON_COLOR);

    }

    // TODO: add more methods (like color?) to make this a useful abstract class
}
