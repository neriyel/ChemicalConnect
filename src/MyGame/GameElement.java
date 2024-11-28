package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class GameElement extends Circle
{
    private final String elementID;

    public GameElement(final String id, final double x, final double y, final double radius, final Color color)
    {
        super(x, y, radius, color);
        this.elementID = id;
    }

    abstract public GameElement getElementAt(final MouseEvent x, final MouseEvent y, Pane pane);

    // TODO: update logic (example, carbon 1 of serine CS1, oh yeah, logic will be in individual concrete element classes)
    @Override
    public String toString()
    {
        return "TestElement";
    }
}
