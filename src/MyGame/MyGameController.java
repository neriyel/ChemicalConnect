package MyGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

public class MyGameController
{
    //Static variables
    private static       int    SINGLE_BOND_WIDTH = 5;
    private static       int    DOUBLE_BOND_WIDTH = 3;
    private static final double BOND_OFFSET       = 5.0;
    private static final String AMINO_ACIDS       = "ACDEFGHIKLMNPQRSTVWY";
    private static final int    LENGTH_USER_BONDS = 2; // 2 because a bond holds 2 elements

    private static final int SINGLE_BOND_LENGTH = 1;
    private static final int DOUBLE_BOND_LENGTH = 2;
    private static final int SINGLE_BOND_INDEX  = 0;
    private static final int DOUBLE_BOND_INDEX  = 1;

    // Instance variables (non-final)
    private AminoAcid              currentAminoAcid;
    private ArrayList<GameElement> currentAminoAcidGUI;
    private GameElement            selectedElement;
    private Bond                   currentBond;

    // Instance variables (final)
    private List<Bond[]>                     bonds; // Track all bonds (single and double)
    private Map<String, ArrayList<String[]>> usersAnswers;

    // FXML Variables
    @FXML
    private Pane gamePane;

    @FXML
    private HBox homeButtonHbox;

    @FXML
    private HBox gameButtonHBox;

    @FXML
    private Label gameLabel;

    @FXML
    private Button startGame;

    @FXML
    private Button rules;

    @FXML
    private Button submit;

    @FXML
    private void initialize()
    {
        // Instantiate _____
        usersAnswers = new HashMap<>();
        bonds        = new ArrayList<>();

        // Hide game buttons
        gameButtonHBox.setVisible(false);

        gameLabel.setText("Welcome to Khemical Konnect!");

        startGame.setOnAction(e -> nextQuestion(e));
        rules.setOnAction(e -> showRules());
        submit.setOnAction(e -> submitEvent(e));

        //debugg
        System.out.println("inside initilize: " + bonds);

    }

    /**
     * Sets up event handlers on GameElements for interactions.
     *
     * @param pane The pane where the dot will be added.
     */
    private void addMouseEventHandlers(GameElement element, Pane pane)
    {
        element.setOnMousePressed(e -> onDotPressed(element, pane, e));
        element.setOnMouseDragged(this::onDotDragged);
        element.setOnMouseReleased(e -> onDotReleased(element, pane, e));
    }

    /**
     * Handles when a dot is pressed, creating a temporary line for bonding.
     *
     * @param element The dot being pressed.
     * @param pane    The pane where the dot and line are located.
     * @param event   The mouse event triggered by pressing the dot.
     */
    private void onDotPressed(GameElement element, Pane pane, MouseEvent event)
    {
        selectedElement = element;

        currentBond = new Bond();
        currentBond.setStartX(element.getCenterX());
        currentBond.setStartY(element.getCenterY());
        currentBond.setEndX(element.getCenterX());
        currentBond.setEndY(element.getCenterY());
        currentBond.setStroke(Color.BLACK);
        currentBond.setStrokeWidth(SINGLE_BOND_WIDTH);

        // Set the initial Element
        currentBond.setElement1(selectedElement);

        pane.getChildren()
                .add(currentBond);
    }

    /**
     * Updates the temporary line's end position as the dot is dragged.
     *
     * @param event The mouse event triggered while dragging the dot.
     */
    private void onDotDragged(MouseEvent event)
    {
        if(currentBond != null)
        {
            currentBond.setEndX(event.getX());
            currentBond.setEndY(event.getY());
        }
    }

    /**
     * Handles when a dot is released and manages bond creation or removal.
     * <p>
     * Note 1: Creating a new GameElement, we later want to compare GameElements, so must override equals() in GameElement class.
     * <p>
     * Note 2: Every Bond must be STORED AS A Bond[]. NOT just a Bond object. This is because elements can have single or double bonds. See below.
     * <p>
     * In our bonds list: Storing a single bond, double bond, single bond looks like the following:
     * [
     * [single_bond_1] <------------------- Single bond (between e1 and e2)
     * [single_bond_1, double_bond_1] <---- Double bond (still between e1 and e2)
     * [single_bond_3] <------------------- Single bond (between e5 and e6)
     * ]
     * <p>
     * Note 3: Single bond exists so create a double bond THAT IS OFFSET PERPENDICULAR to original single bond. We remove the existing line in the process.
     *
     * @param element The dot being released.
     * @param pane    The pane where the dots and lines are located.
     * @param event   The mouse event triggered by releasing the dot.
     */
    private void onDotReleased(GameElement element, Pane pane, MouseEvent event)
    {
        // Note 1: see JavaDoc
        final GameElement targetElement;
        targetElement = getDotAt(event.getX(), event.getY(), pane);

        if(targetElement != null && targetElement != selectedElement)
        {
            // Get an array of existing Bonds. If no bonds, array size will be 0
            Bond[] existingBond = findBond(selectedElement, targetElement);

            if(existingBond.length == 0)
            {
                // No bonds exist: create the first bond
                currentBond.setEndX(targetElement.getCenterX());
                currentBond.setEndY(targetElement.getCenterY());

                // Set the second element
                currentBond.setElement2(targetElement);

                // Note 2: see JavaDoc
                bonds.add(new Bond[]{currentBond}); // Add single bond
                //debugging
                for(Bond[] bond : bonds)
                {
                    System.out.println(Arrays.toString(bond) + "hehe");
                }
                addClickListenerToLine(currentBond, pane); // Add click listener for removal
            }
            else if(existingBond.length == 1)
            {
                // Note 3: see JavaDoc
                pane.getChildren()
                        .remove(currentBond);

                // Add a second (parallel) line
                final Bond offsetDoubleBond;
                offsetDoubleBond = createParallelLine(existingBond[0], targetElement);//@@@@@@@@@@@@

                bonds.add(new Bond[]{existingBond[0], offsetDoubleBond});
                //debugging
                System.out.println("Start second bond: ");
                for(Bond[] bond : bonds)
                {
                    System.out.println(Arrays.toString(bond));
                }
                pane.getChildren()
                        .add(offsetDoubleBond);
                addClickListenerToLine(offsetDoubleBond, pane); // Add click listener for removal
            }
            else
            {
                // If two or more bonds exist: reject the action
                pane.getChildren()
                        .remove(currentBond); // Remove the redundant line
            }
        }
        else
        {
            // Remove the temporary line if the connection is invalid
            pane.getChildren()
                    .remove(currentBond);
            //debugging
            System.out.println("invalid bonds?: ");
            for(Bond[] bond : bonds)
            {
                System.out.println(Arrays.toString(bond));
            }
        }

        // Reset state
        selectedElement = null;
        currentBond     = null;
    }

    /**
     * Finds the dot that the mouse is hovering over.
     *
     * @param x    The x-coordinate of the mouse.
     * @param y    The y-coordinate of the mouse.
     * @param pane The pane containing the dots.
     *
     * @return The dot the mouse is over, or null if none.
     */
    private GameElement getDotAt(double x, double y, Pane pane)
    {
        for(final var node : pane.getChildren())
        {
            if(node instanceof GameElement)
            {
                GameElement element = (GameElement) node;
                if(element.contains(x, y))
                {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Checks if a bond already exists between two dots.
     * <p>
     * Note 1: We initially make a List<Bond> because we need to be able to dynamically add or remove Bond objects. We then return it as an array (.toArray) because, logically, we no longer need to dynamically add/remove elements.
     * <p>
     * Note 2: .toArray(new Bond[0]) just creates an empty Bond[] array IN THE CASE nothing was added to it. If Bonds were added to it, Java dynamically adjusts array size!
     *
     * @param e1 The first element.
     * @param e2 The second element.
     *
     * @return An array of existing lines (bonds) between the dots.
     */
    private Bond[] findBond(GameElement e1, GameElement e2)
    {
        // Note 1: see JavaDoc
        final List<Bond> bondsBetween;
        bondsBetween = new ArrayList<>();

        // TODO: add jason syntax
        for(final Bond[] bondPair : bonds)
        {
            for(final Bond bond : bondPair)
            {
                // TODO: probably make this a helper method
                if((bond.getStartX() == e1.getCenterX() && bond.getStartY() == e1.getCenterY() &&
                        bond.getEndX() == e2.getCenterX() && bond.getEndY() == e2.getCenterY()) ||
                        (bond.getStartX() == e2.getCenterX() &&
                                bond.getStartY() == e2.getCenterY() &&
                                bond.getEndX() == e1.getCenterX() &&
                                bond.getEndY() == e1.getCenterY()))
                {
                    bondsBetween.add(bond);
                }
            }
        }

        // Note 2: see JavaDoc
        return bondsBetween.toArray(new Bond[0]);
    }

    /**
     * Adds a click listener to a bond, allowing it to be removed by clicking.
     *
     * @param bond The bond to add the click listener to.
     * @param pane The pane where the bond is located.
     */
    private void addClickListenerToLine(final Bond bond, final Pane pane)
    {
        bond.setOnMouseClicked(event ->
                               {
                                   // Remove the bond from the list
                                   removeBondFromListAndScreen(bond, pane);

                                   //debugging
                                   System.out.println("removing bond");
                                   for(Bond[] currentBond : bonds)
                                   {
                                       System.out.println(Arrays.toString(currentBond));
                                   }
                               });
    }

    /**
     * Removes the specified bond (bond) from the bond list.
     *
     * @param bond The bond to remove from the bond list.
     * @param pane
     */
    private void removeBondFromListAndScreen(final Bond bond, final Pane pane)
    {
        // Iterate through the bonds list
        for(final Iterator<Bond[]> iter = bonds.iterator(); iter.hasNext(); )
        {
            final Bond[] bondPair;
            bondPair = iter.next();

            // Check if the bond belongs to this bond pair
            if(Arrays.asList(bondPair)
                    .contains(bond))
            {
                // Extract the coordinates of the bond being removed
                double lineStartX = bond.getStartX();
                double lineStartY = bond.getStartY();
                double lineEndX   = bond.getEndX();
                double lineEndY   = bond.getEndY();

                // Check if any other `bondPair` exists between the same coordinates
                for(final Bond[] otherBondPair : bonds)
                {
                    // Skip the current bondPair
                    if(otherBondPair != bondPair)
                    {
                        for(final Bond otherBond : otherBondPair)
                        {
                            // Compare coordinates (unordered since bonds are bidirectional)
                            if(linesConnectSamePoints(lineStartX, lineStartY, lineEndX, lineEndY, otherBond.getStartX(), otherBond.getStartY(), otherBond.getEndX(), otherBond.getEndY()))
                            {
                                // Prevent removal if higher-order bonds exist
                                showErrorPopup("You must remove all higher-order bonds (e.g., double bonds) before removing this bond.");
                                return; // Stop further execution
                            }
                        }
                    }
                }

                // If valid, remove the bond from the List, and the GUI
                iter.remove();
                pane.getChildren()
                        .remove(bond);
                break; // Stop once the bond is found and removed
            }
        }
    }

    // Helper method to check if two lines connect the same points (order doesn't matter)
    private boolean linesConnectSamePoints(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        // Check if line1 connects the same points as line2 (in any order)
        return ((x1 == x3 && y1 == y3 && x2 == x4 && y2 == y4) ||  // Same direction
                (x1 == x4 && y1 == y4 && x2 == x3 && y2 == y3));   // Opposite direction
    }

    /**
     * Shows an error popup with the given message.
     *
     * @param message The error message to display.
     */
    private void showErrorPopup(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Bond Removal");
        alert.setHeaderText("Error: Bond Removal Order");
        alert.setContentText(message);
        alert.showAndWait(); // Wait for the user to close the dialog
    }

    /**
     * Creates a parallel bond to an existing bond, offset by a small value.
     *
     * @param bond          The original bond.
     * @param targetElement
     *
     * @return A new Line object that is parallel to the original.
     */
    private Bond createParallelLine(Bond bond, final GameElement targetElement)
    {
        // Calculate offset for parallel bond
        double offset = BOND_OFFSET;
        double dx     = bond.getEndX() - bond.getStartX();
        double dy     = bond.getEndY() - bond.getStartY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalize to get unit vector
        double ux = dx / length;
        double uy = dy / length;

        // Perpendicular vector for offset
        double px = -uy * offset;
        double py = ux * offset;

        // Create parallel bond
        final Bond parallelLine = new Bond(
                bond.getStartX() + px,
                bond.getStartY() + py,
                bond.getEndX() + px, bond.getEndY() + py, selectedElement, targetElement);
        parallelLine.setStrokeWidth(DOUBLE_BOND_WIDTH);

        return parallelLine;
    }

    @FXML
    public void nextQuestion(final ActionEvent actionEvent)
    {
        // Hide home buttons | Show game buttons
        removeHomeButtons();
        gameButtonHBox.setVisible(true);

        // Remove previous question (bonds, currentAminoAcid)
        resetQuestion();

        // Now generate next question
        currentAminoAcid    = new AminoAcid(generateRandomAA());
        currentAminoAcidGUI = currentAminoAcid.getAminoAcid();

        for(final GameElement element : currentAminoAcidGUI)
        {
            addMouseEventHandlers(element, gamePane);
            gamePane.getChildren()
                    .add(element); // Add the dot to the pane
        }
    }

    private void removeHomeButtons()
    {
        if(homeButtonHbox != null)
        {
            homeButtonHbox.setVisible(false);
        }
    }

    /**
     * Removes current AminoAcid and Bonds from screen
     */
    private void resetQuestion()
    {
        currentAminoAcid = null;
        if(currentAminoAcidGUI != null)
        {
            for(final GameElement element : currentAminoAcidGUI)
            {
                gamePane.getChildren()
                        .remove(element);
            }
            currentAminoAcidGUI.clear();
        }

        if(bonds != null)
        {
            for(final Bond[] bond : bonds)
            {
                for(final Bond bond1 : bond)
                {
                    gamePane.getChildren()
                            .remove(bond1);
                }
            }
            bonds.clear();
        }
    }

    /**
     * When user hits submit button. Save amino acid,
     */
    private void submitEvent(final ActionEvent e)
    {
        // userKey: Amino Acid ID | userValuePlaceholder: Array of bonds ex [TC1, TN1]
        final String              userKey;
        final ArrayList<String[]> tempMapValues;
        String[]                  userValuePlaceholder;

        userKey              = currentAminoAcid.getAminoAcidID();
        tempMapValues        = new ArrayList<>();
        userValuePlaceholder = new String[LENGTH_USER_BONDS];

        if(bonds!= null && !bonds.isEmpty())
        {
            for(final Bond[] bond : bonds)
            {
                if(bond.length == SINGLE_BOND_LENGTH)
                {
                    // Store element1 and element2 in temp String[]
                    userValuePlaceholder[0] = bond[SINGLE_BOND_INDEX].getElement1()
                            .toString();
                    userValuePlaceholder[1] = bond[SINGLE_BOND_INDEX].getElement1()
                            .toString();
                    // Add to tempMapValue Array
                    tempMapValues.add(userValuePlaceholder);
                }
                else if(bond.length == DOUBLE_BOND_LENGTH)
                {
                    userValuePlaceholder[0] = bond[DOUBLE_BOND_INDEX].getElement1()
                            .toString();
                    userValuePlaceholder[1] = bond[DOUBLE_BOND_INDEX].getElement1()
                            .toString();
                    // Add to tempMapValue Array
                    tempMapValues.add(userValuePlaceholder);
                }
            }

            usersAnswers.put(currentAminoAcid.getAminoAcidID(), tempMapValues);

            //debugging
            for(Map.Entry<String, ArrayList<String[]>> entry : usersAnswers.entrySet())
            {
                System.out.print("INSIDE SUBMIT EVENT!!!!" + entry.getKey() + ": ");

                // Loop through the ArrayList
                for(String[] array : entry.getValue())
                {
                    System.out.println(Arrays.toString(array));  // Print each String[] as a string
                }
            }

            // Call next question after storing user results
            nextQuestion(e);
        }
        else
        {
            mustSubmitAnswerError();
        }

    }

    private void mustSubmitAnswerError()
    {

        // Create a new Alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // Set the title of the popup
        alert.setTitle("Error");

        // Set the header text (optional)
        alert.setHeaderText("An error has occurred");

        // Set the content text to the error message
        alert.setContentText("errorMessage");

        // Show the alert and wait for the user to close it
        alert.showAndWait();

    }

    private char generateRandomAA()
    {
        final Random random;
        final char   nextAA;

        random = new Random();
        nextAA = AMINO_ACIDS.charAt(random.nextInt(AMINO_ACIDS.length()));

        System.out.println("Randomly generated amino acid: " + nextAA);

        return nextAA;
    }

    @FXML
    private void showRules()
    {
    }
}

