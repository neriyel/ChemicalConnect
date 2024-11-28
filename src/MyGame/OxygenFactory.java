package MyGame;

import javafx.scene.paint.Color;

public class OxygenFactory implements ElementFactory
{

    @Override
    public GameElement createElement(final String id, final double x, final double y)
    {
        return new Oxygen(id, x, y);
    }

}
