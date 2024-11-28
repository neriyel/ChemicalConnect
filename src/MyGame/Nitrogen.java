package MyGame;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Nitrogen extends GameElement
{

    // Static variables
    private static Color NITROGEN_COLOR        = Color.MEDIUMPURPLE;
    private static Color NITROGEN_BORDER_COLOR = Color.PURPLE;
    private static int   NITROGEN_RADIUS       = 12;

    // Instance variables

    public Nitrogen(final String id, final double x, final double y)
    {
        super(id, x, y, NITROGEN_RADIUS, NITROGEN_COLOR);
        setNitrogenMouseEventColors();
    }

    private void setNitrogenMouseEventColors()
    {
        this.setStroke(NITROGEN_BORDER_COLOR);
        this.setOnMouseExited(e -> this.setFill(NITROGEN_COLOR));
    }

    @Override
    public GameElement getElementAt(final MouseEvent x, final MouseEvent y, final Pane pane)
    {
        return null;
    }
}