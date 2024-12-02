package MyGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * GameElement represents an element.
 */
public abstract class GameElement extends Circle
{
    private static int   GAME_ELEMENT_STROKEWIDTH   = 3;
    private static Color GAME_ELEMENT_ENTERED_COLOR = Color.GREY;

    private final String elementID;

    /**
     * Constructor for GameElement
     *
     * @param id     is the element id
     * @param x      is the x coord
     * @param y      is the y coord
     * @param radius is the radius
     * @param color  is the color
     */
    public GameElement(final String id, final double x, final double y, final double radius, final Color color)
    {
        super(x, y, radius, color);
        this.elementID = id;
        this.setStrokeWidth(GAME_ELEMENT_STROKEWIDTH);
        this.setOnMouseEntered(e -> this.setFill(GAME_ELEMENT_ENTERED_COLOR));
    }

    /**
     * Returns elementID as a string
     *
     * @return
     */
    public String getElementID()
    {
        return elementID;
    }

}
