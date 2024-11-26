package NumberGame;

import java.util.ArrayList;

/**
 * Once clicked, should be labelled as the value at its index (instead of default label [])
 */
public class ClickedGridButton extends GridButton
{
        private final int value;


        public ClickedGridButton(final int label)
        {
                this.setText(String.valueOf(label));
                this.value = label;
                this.setDisable(true);
        }

        @Override
        public boolean validButton(final int index, final ArrayList<Integer> validButtons)
        {
                return false;
        }

        @Override
        public int getValue()
        {
                return value;
        }
}
