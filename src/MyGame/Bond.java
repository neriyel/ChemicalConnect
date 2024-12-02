package MyGame;

import javafx.scene.shape.Line;

/**
 * Bond represents a bond between two elements. Extends the Line class.
 */
public class Bond extends Line
{
    // instance variables
    private GameElement e1;
    private GameElement e2;

    /**
     * Constructor with no parameters since JavaFX Line object's parameters are not known immediately.
     */
    public Bond()
    {
        super();
    }

    /**
     * Constructor for cases when a Bond object is hardcoded ie, all parameters are known immediately.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param selectedElement
     * @param targetElement
     */
    public Bond(final double x1, final double y1, final double x2, final double y2, final GameElement selectedElement, final GameElement targetElement)
    {
        super(x1, y1, x2, y2);
        this.e1 = selectedElement;
        this.e2 = targetElement;
    }

    /**
     * Sets element 1
     *
     * @param e1
     */
    public final void setElement1(GameElement e1)
    {
        this.e1 = e1;
    }

    /**
     * Sets element 1
     *
     * @param e2
     */
    public final void setElement2(GameElement e2)
    {
        this.e2 = e2;
    }

    /**
     * Gets element 1
     *
     * @return
     */
    public final GameElement getElement1()
    {
        return e1;
    }

    /**
     * Gets element 2
     *
     * @return
     */
    public final GameElement getElement2()
    {
        return e2;
    }

    /**
     * To string representation of a bond.
     *
     * @return
     */
    @Override
    public String toString()
    {
        return String.format("Bond[x1= %.2f, y1= %.2f, x2= %.2f, y2= %.2f, element1= %s, element2= %s]", getStartX(), getStartY(), getEndX(), getEndY(),
                             e1 != null ? e1.toString() : "null",
                             e2 != null ? e2.toString() : "null");
    }

}


