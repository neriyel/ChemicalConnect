public class UnclickedGridButton extends GameButton
{
    private final static int UNCLICKED_VALUE = 0;

    private final int value;

    public UnclickedGridButton()
    {
        this.setText("[]");
        this.value = UNCLICKED_VALUE;
    }

    /**
     * @@@@@@@@@@ NEED TO REFACTOR EVENTUALLY @@@@
     * @param index
     * @return
     */
    @Override
    public boolean validButton(final String index)
    {
        return false;
    }

    @Override
    public int getValue()
    {
        return value;
    }
}
