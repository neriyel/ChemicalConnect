package MyGame;

/**
 * Factory for Nitrogen (factory design pattern)
 */
public class NitrogenFactory implements ElementFactory
{
    @Override
    public GameElement createElement(final String id, final double x, final double y)
    {
        return new Nitrogen(id, x, y);
    }
}
