import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Random;

public class NumberGameController
{

    // Static variables
    private static final int NUM_BUTTONS      = 20;
    private static final int LENGTH_CORRECTOR = 1;
    private static final int NUM_ROWS         = 5;
    private static final int NUM_COL          = 5;

    // Instance variables (non-final)
    private ArrayList<Integer> validButtons;
    private int                rand;
    private int                totalGamesPlayed = 0;
    private int                totalGamesWon    = 0;
    private int                placements       = 0;

    // FXML variables
    @FXML
    private Label        mainLabel;
    @FXML
    private GridPane     gridPane;
    @FXML
    private GridButton[] buttons;

    @FXML
    public void initialize()
    {
        // Initialize the game grid with 20 buttons
        buttons      = new GridButton[NUM_BUTTONS];
        validButtons = new ArrayList<>();

        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            buttons[i] = new UnclickedGridButton();
            buttons[i].setMaxWidth(Double.MAX_VALUE);
            buttons[i].setMaxHeight(Double.MAX_VALUE);

            // Set button action
            int index = i;
            buttons[i].setOnAction(event -> buttonClicked(index, event));

            // Add button to grid
            int row = i / NUM_ROWS;
            int col = i % NUM_COL;
            gridPane.add(buttons[i], col, row);
        }

        nextMove();
    }

    /**
     * On button click, check if button index is part of validButtons array.
     * If true, cast UnclickedGridButton -> ClickedGridButton. Set the value to rand. Set label to rand.
     *
     * @param index
     * @param event
     */
    private void buttonClicked(int index, ActionEvent event)
    {
        if(buttons[index].validButton(index, validButtons))
        {
            // Create new button ClickedButton object
            ClickedGridButton clickedButton;
            clickedButton = new ClickedGridButton(rand);

            // Replaced UnclickedButton with ClickedButton
            buttons[index] = clickedButton;
            replaceButton(index, clickedButton);

            // Generate next integer
            nextMove();

        }
    }

    private void replaceButton(final int index, final ClickedGridButton clickedButton)
    {
        final int row;
        final int column;

        row    = index / NUM_ROWS;
        column = index % NUM_COL;

        buttons[index].setMaxWidth(Double.MAX_VALUE);
        buttons[index].setMaxHeight(Double.MAX_VALUE);

        gridPane.getChildren()
                .removeIf(node -> GridPane.getRowIndex(node) == row &&
                        GridPane.getColumnIndex(node) == column);

        gridPane.add(clickedButton, column, row);
    }

    private void nextMove()
    {
        checkIfWon();
        generateRandomInt();
        checkNextMove(rand);
    }

    private void checkIfWon()
    {
        boolean allClicked = true;

        if(buttons != null)
        {
            for(GridButton button : buttons)
            {
                if(!(button instanceof ClickedGridButton))
                {
                    allClicked = false;
                    break;
                }
            }

        }

        if(allClicked)
        {
            totalGamesWon++;
            totalGamesPlayed++;
            countPlacements();
            endSession();
        }
    }

    private void generateRandomInt()
    {
        Random random;
        random = new Random();
        rand   = random.nextInt(1000) + 1;
    }

    private void checkNextMove(final int rand)
    {
        validButtons = listValidButtons();

        if(validButtons.isEmpty())
        {
            mainLabel.setText("Impossible to place the next number: " + rand);
            countPlacements();
            totalGamesPlayed++;
            endSession();
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

    private void countPlacements()
    {
        for(GridButton button : buttons)
        {
            if(button instanceof ClickedGridButton)
            {
                placements++;
            }
        }
    }

    private ArrayList<Integer> listValidButtons()
    {
        // Clear valid buttons first
        validButtons.clear();

        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            if(previousButtonsValid(i) && postButtonsValid(i))
            {
                if(!(buttons[i] instanceof ClickedGridButton))
                {
                    validButtons.add(i);

                }
            }
        }

        // debugging purposes
        for(Integer validButton : validButtons)
        {
            System.out.println("Valid buttons: " + validButton);
        }
        System.out.println("------------");
        return validButtons;
    }

    /**
     * Returns true if all previous buttons' values (that have been clicked) are less than the current rand value, otherwise false.
     *
     * @param index
     *
     * @return
     */
    private boolean previousButtonsValid(final int index)
    {
        for(int i = 0; i < index; i++)
        {
            if((buttons[i] instanceof ClickedGridButton) && (buttons[i].getValue() > rand))
            {
                return false;
            }
        }
        return true;

    }

    /**
     * Returns true if all post buttons' values (that have been clicked) are greater than the current rand value, otherwise false.
     *
     * @param index
     *
     * @return
     */
    private boolean postButtonsValid(final int index)
    {
        for(int i = NUM_BUTTONS - LENGTH_CORRECTOR; i > index; i--)
        {
            if((buttons[i] instanceof ClickedGridButton) && (buttons[i].getValue() < rand))
            {
                return false;
            }
        }
        return true;
    }

    private void endSession()
    {
        final Alert      alert;
        final ButtonType tryAgain;
        final ButtonType endGame;

        alert    = new Alert(Alert.AlertType.WARNING);
        tryAgain = new ButtonType("Try Again");
        endGame  = new ButtonType("End Game");

        alert.setTitle("Game Over");
        alert.setHeaderText("Impossible to place the next number: " + rand);
        alert.setContentText("Try Again?" + scoreSummary());
        alert.getButtonTypes()
                .setAll(endGame, tryAgain);

        alert.showAndWait()
                .ifPresent(response ->
                           {
                               if(response == endGame)
                               {
                                   endGame();
                               }
                               else if(response == tryAgain)
                               {
                                   restartGame();
                               }

                           });

    }

    private void endGame()
    {
        final Alert      alert;
        final ButtonType exit;

        alert = new Alert(Alert.AlertType.WARNING);
        exit  = new ButtonType("Exit");

        alert.setTitle("Game Ended");
        alert.setHeaderText("Game ended, thanks for playing!");
        alert.setContentText(scoreSummary());
        alert.getButtonTypes()
                .setAll(exit);

        alert.showAndWait()
                .ifPresent(response ->
                           {
                               if(response == exit)
                               {
                                   System.exit(0);
                               }
                           });

    }

    private String scoreSummary()
    {
        final String summary;
        final double placementRate;

        placementRate = (double) placements / totalGamesPlayed;

        summary = String.format("You won %d out of %d game(s), with %d successful placements, averaging to %.2f placements per game", totalGamesWon, totalGamesPlayed, placements, placementRate);

        return summary;
    }

    private void restartGame()
    {
        initialize();
    }

}
