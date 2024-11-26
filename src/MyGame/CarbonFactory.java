package MyGame;

import javafx.scene.paint.Color;

public class CarbonFactory implements ElementFactory
{

    @Override
    public GameElement createElement()
    {
        GameElement carbon = new Carbon(200, 150);

        carbon.setStroke(Color.DARKBLUE);
        carbon.setStrokeWidth(2);
        
        return carbon;
    }

}
