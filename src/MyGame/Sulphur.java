package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Sulphur extends GameElement
{

    // Static variables
    private static Color SULPHUR_COLOR        = Color.LIGHTGOLDENRODYELLOW;
    private static Color SULPHUR_BORDER_COLOR = Color.BROWN;
    private static int   SULPHUR_RADIUS       = 17;

    // Instance variables (final)

    public Sulphur(final String id, final double x, final double y)
    {
        super(id, x, y, SULPHUR_RADIUS, SULPHUR_COLOR);
        setCarbonMouseEventColors();
    }

    /**
     * Sets Carbon's mouse event colors (Border and onMouseExit color)
     */
    private void setCarbonMouseEventColors()
    {
        this.setStroke(SULPHUR_BORDER_COLOR);
        this.setOnMouseExited(e -> this.setFill(SULPHUR_COLOR));
    }

}
