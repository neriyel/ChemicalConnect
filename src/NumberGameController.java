import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Random;

public class NumberGameController
{

    // Static variables
    private static final int NUM_BUTTONS = 20;

    // Instance variables (non-final)
    private ArrayList<Integer> validButtons;
    private int                rand;

    // FXML variables
    @FXML
    private Label                 mainLabel;
    @FXML
    private GridPane              gridPane;
    @FXML
    private UnclickedGridButton[] buttons;

    @FXML
    public void initialize()
    {
        // Initialize the game grid with 20 buttons
        buttons      = new UnclickedGridButton[NUM_BUTTONS];
        validButtons = new ArrayList<>();

        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            buttons[i] = new UnclickedGridButton();
            buttons[i].setMaxWidth(Double.MAX_VALUE);
            buttons[i].setMaxHeight(Double.MAX_VALUE);

            // Set button action
            int index = i;
            buttons[i].setOnAction(event -> handleButtonClick(index, event));

            // Add button to grid
            int row = i / 5;
            int col = i % 5;
            gridPane.add(buttons[i], col, row);
        }

        nextMove();
    }

    private void handleButtonClick(int index, ActionEvent event)
    {
        System.out.println("Button " + index + " clicked!");
        // Add logic for button click handling here
    }

    private void nextMove()
    {
        generateRandomInt();
        checkNextMove(rand);
    }

    private void generateRandomInt()
    {
        Random random;
        random = new Random();
        rand   = random.nextInt(1000) + 1;
    }

    private void checkNextMove(final int rand)
    {
        validButtons = listValidButtons(rand);

        if(validButtons.isEmpty())
        {
            mainLabel.setText("Impossible to place the next number: " + rand);
        }

        if(!validButtons.isEmpty())
        {
            mainLabel.setText("Select a slot for the next number: " + rand);
        }

        if(validButtons == null)
        {
            System.out.println("Should not be here.");
        }

    }

    private ArrayList<Integer> listValidButtons(final int rand)
    {
        // Clear valid buttons first
        validButtons.clear();

        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            if(previousButtonsValid(i) && postButtonsValid(i))
            {
                validButtons.add(i);
            }
        }

        return validButtons;
    }

    /**
     * Returns true if all previous buttons' values are less than the current rand value, otherwise false
     *
     * @param index
     *
     * @return
     */
    private boolean previousButtonsValid(final int index)
    {
        for(int i = 0; i < index; i++)
        {
            if(buttons[i].getValue() > rand)
            {
                return false;
            }
        }
        return true;

    }

    private boolean postButtonsValid(final int index)
    {
        for(int i = 0; i < index; i++)
        {
            if(buttons[i].getValue() < rand)
            {
                return false;
            }
        }
        return true;
    }

}
