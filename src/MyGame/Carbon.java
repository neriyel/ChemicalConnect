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

        //TODO: probably make below helper method
        this.setStroke(Color.DARKBLUE);
        this.setStrokeWidth(2);

        this.setOnMouseEntered(e -> this.setFill(Color.LIGHTGREEN));
        this.setOnMouseExited(e -> this.setFill(CARBON_COLOR));
    }
}
