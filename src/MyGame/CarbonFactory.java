package MyGame;

public class CarbonFactory implements ElementFactory
{

    @Override
    public GameElement createElement(final String id, final double x, final double y)
    {
        return new Carbon(id, x, y);
    }

}
