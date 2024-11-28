package MyGame;

public class SulphurFactory implements ElementFactory
{

    @Override
    public GameElement createElement(final String id, final double x, final double y)
    {
        return new Sulphur(id, x, y);
    }

}
