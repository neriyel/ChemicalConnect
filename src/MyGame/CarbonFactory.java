package MyGame;

import javafx.scene.paint.Color;

public class CarbonFactory implements ElementFactory
{

    @Override
    public GameElement createElement(final double x, final double y)
    {
        GameElement carbon = new Carbon(x, y);

        carbon.setStroke(Color.DARKBLUE);
        carbon.setStrokeWidth(2);
        
        return carbon;
    }

}
