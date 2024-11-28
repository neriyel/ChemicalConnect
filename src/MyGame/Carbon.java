package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.print.DocFlavor;

public class Carbon extends GameElement
{

    // Static variables
    private static Color CARBON_COLOR        = Color.BLUE;
    private static Color CARBON_BORDER_COLOR = Color.FIREBRICK;
    private static int   CARBON_RADIUS       = 10;

    // Instance variables (final)

    public Carbon(final String id, final double x, final double y)
    {
        super(id, x, y, CARBON_RADIUS, CARBON_COLOR);
        setCarbonMouseEventColors();
    }

    /**
     * Sets Carbon's mouse event colors (Border and onMouseExit color)
     */
    private void setCarbonMouseEventColors()
    {
        this.setStroke(CARBON_BORDER_COLOR);
        this.setOnMouseExited(e -> this.setFill(CARBON_COLOR));
    }

    @Override
    public GameElement getElementAt(final MouseEvent x, final MouseEvent y, final Pane pane)
    {
        return null;
    }
}
