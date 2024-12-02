package MyGame;

import javafx.scene.paint.Color;

/**
 * Represents a Nitrogen element.
 */
public class Nitrogen extends GameElement
{

    // Static variables
    private static Color NITROGEN_COLOR        = Color.MEDIUMPURPLE;
    private static Color NITROGEN_BORDER_COLOR = Color.PURPLE;
    private static int   NITROGEN_RADIUS       = 12;

    // Instance variables

    /**
     * Constructor for Nitrogen
     * @param id
     * @param x
     * @param y
     */
    public Nitrogen(final String id, final double x, final double y)
    {
        super(id, x, y, NITROGEN_RADIUS, NITROGEN_COLOR);
        setNitrogenMouseEventColors();
    }

    /**
     * Helper method for setting colors
     */
    private void setNitrogenMouseEventColors()
    {
        this.setStroke(NITROGEN_BORDER_COLOR);
        this.setOnMouseExited(e -> this.setFill(NITROGEN_COLOR));
    }

}