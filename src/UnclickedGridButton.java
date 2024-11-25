import java.util.ArrayList;

public class UnclickedGridButton extends GridButton
{
    private final static int UNCLICKED_VALUE = 0;

    private final int value;

    public UnclickedGridButton()
    {
        this.setText("[]");
        this.value = UNCLICKED_VALUE;
    }


    @Override
    public boolean validButton(final int index, final ArrayList<Integer> validButtons)
    {
        return validButtons.contains(index);
    }

    @Override
    public int getValue()
    {
        return value;
    }
}
