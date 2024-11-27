package MyGame;

import javafx.scene.shape.Line;

public class Bond extends Line
{
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

    // Setters
    public void setElement1(GameElement e1)
    {
        this.e1 = e1;
    }

    public void setElement2(GameElement e2)
    {
        this.e2 = e2;
    }

    // Getters
    public GameElement getElement1()
    {
        return e1;
    }

    public GameElement getElement2()
    {
        return e2;
    }

    @Override
    public String toString()
    {
        return String.format("Bond[x1= %.2f, y1= %.2f, x2= %.2f, y2= %.2f, element1= %s, element2= %s]", getStartX(), getStartY(), getEndX(), getEndY(),
                             e1 != null ? e1.toString() : "null",
                             e2 != null ? e2.toString() : "null");
    }

}


