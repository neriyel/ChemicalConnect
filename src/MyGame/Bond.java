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

    @Override
    public boolean equals(final Object that)
    {
        if(that == null)
        {
            return false;
        }

        if(that instanceof Bond)
        {
            // this Bond's coordinates
            //            final double thisBondX1;
            //            final double thisBondY1;
            //            final double thisBondX2;
            //            final double thisBondY2;
            //
            //            // that Bond's coordinates
            //            final double thatBondX1;
            //            final double thatBondY1;
            //            final double thatBondX2;
            //            final double thatBondY2;
            //
            //            thisBondX1 = this.getStartX();
            //            thisBondY1 = this.getStartY();
            //            thisBondX2 = this.getEndX();
            //            thisBondY2 = this.getEndY();
            //
            //            thatBondX1 = this.getStartX();
            //            thatBondY1 = this.getStartY();
            //            thatBondX2 = this.getEndX();
            //            thatBondY2 = this.getEndY();

            final String thisElement1;
            final String thisElement2;
            final String thatElement1;
            final String  thatElement2;
            final boolean result;

            thisElement1 = this.getElement1().toString();
            thisElement2 = this.getElement2().toString();
            thatElement1 = this.getElement1().toString();
            thatElement2 = this.getElement2().toString();

            // Returns equality in any order: Ie [A,B] == [B,A]
            result = thisElement1.equals(thatElement1) && thisElement2.equals(thatElement2) ||
                    thisElement1.equals(thatElement2) && thatElement2.equals(thatElement1);

            return result;
        }

        // that is not instanceOf Bond
        return false;
    }

}


