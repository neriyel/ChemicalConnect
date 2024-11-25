import java.util.ArrayList;

public interface Buttonable
{
    boolean validButton(final int index, final ArrayList<Integer> validButtons);

    int getValue();
}
