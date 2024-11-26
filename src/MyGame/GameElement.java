package MyGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class GameElement extends Circle
{
    public GameElement(final double x, final double y, final double radius, final Color color)
    {
        super(x, y, radius, color);
    }
}
