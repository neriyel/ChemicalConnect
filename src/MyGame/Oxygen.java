package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Oxygen extends GameElement
{

    // Static variables
    private static Color OXYGEN_COLOR        = Color.DEEPPINK;
    private static Color OXYGEN_BORDER_COLOR = Color.HONEYDEW;
    private static int   OXYGEN_RADIUS       = 15;

    // Instance variables (final)

    public Oxygen(final String id, final double x, final double y)
    {
        super(id, x, y, OXYGEN_RADIUS, OXYGEN_COLOR);
        setCarbonMouseEventColors();
    }

    /**
     * Sets Carbon's mouse event colors (Border and onMouseExit color)
     */
    private void setCarbonMouseEventColors()
    {
        this.setStroke(OXYGEN_BORDER_COLOR);
        this.setOnMouseExited(e -> this.setFill(OXYGEN_COLOR));
    }

    @Override
    public GameElement getElementAt(final MouseEvent x, final MouseEvent y, final Pane pane)
    {
        return null;
    }
}
