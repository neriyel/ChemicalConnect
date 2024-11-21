public class UnclickedGridButton extends GameButton
{

    public UnclickedGridButton(final String label)
    {
        this.setText(label);
    }

    @Override
    public boolean validButton(final String index)
    {
        return false;
    }
}
