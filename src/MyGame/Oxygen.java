package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Oxygen extends GameElement
{

    // Static variables
    private static final Color OXYGEN_COLOR;
    private static final Color OXYGEN_BORDER_COLOR;
    private static final int   OXYGEN_RADIUS;

    // Static initializer block
    static
    {
        OXYGEN_COLOR        = Color.DEEPPINK;
        OXYGEN_BORDER_COLOR = Color.PURPLE;
        OXYGEN_RADIUS       = 15;
    }

    // Constructor
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
}
