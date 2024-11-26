package MyGame;

public class CarbonFactory implements ElementFactory
{
    // TODO: why did it default return type to MyGame.Element? this caused an error
    @Override
    public Element createElement()
    {
        return null;
    }
}
