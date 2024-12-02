package MyGame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class MyGameController
{
    //Static variables
    private static final int    MAX_QUESTION_COUNT = 5;
    private static final int    SINGLE_BOND_WIDTH  = 5;
    private static final int    DOUBLE_BOND_WIDTH  = 3;
    private static final double BOND_OFFSET        = 5.0;
    private static final String AMINO_ACIDS        = "AVILM";
    //    private static final String AMINO_ACIDS        = "ACDEFGHIKLMNPQRSTVWY";
    private static final int    LENGTH_USER_BONDS  = 2; // 2 because a bond holds 2 elements

    private static final int    SINGLE_BOND_LENGTH    = 1;
    private static final int    DOUBLE_BOND_LENGTH    = 2;
    private static final int    SINGLE_BOND_INDEX     = 0;
    private static final int    DOUBLE_BOND_INDEX     = 1;
    private static final int    E1_INDEX              = 0;
    private static final int    E2_INDEX              = 1;
    private static final double PERCENT               = 100.00;
    private static final int    NO_QUESTIONS_ANSWERED = 0;

    // Instance variables (non-final)
    private AminoAcid                        currentAminoAcid;
    private ArrayList<GameElement>           currentAminoAcidGUI;
    private GameElement                      selectedElement;
    private Bond                             currentBond;
    private List<Character>                  availableAminoAcids;
    private List<Bond[]>                     bonds; // Track all bonds (single and double)
    private Map<String, ArrayList<String[]>> usersAnswers;
    private Map<String, ArrayList<Bond>>     usersAnswersAsBonds;
    private ArrayList<AminoAcid>             correctAminoAcids;
    private ArrayList<AminoAcid>             incorrectAminoAcids;
    private int                              score;
    private int                              questionCount          = 0;
    private int                              totalQuestionsAnswered = 0;

    // FXML Variables
    @FXML
    private Pane gamePane;

    @FXML
    private HBox homeButtonHbox;

    @FXML
    private HBox gameButtonHBox;

    @FXML
    private VBox resultsPanelVBox;

    @FXML
    private Label gameLabel;

    @FXML
    private Button startGame;

    @FXML
    private Button rules;

    @FXML
    private Button submit;

    @FXML
    private Button endSession;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label scoreDetailsLabel;

    @FXML
    private Label scorePercentLabel;

    @FXML
    public void initialize()
    {
        // Instantiate _____
        usersAnswers        = new HashMap<>();
        usersAnswersAsBonds = new HashMap<>();
        bonds               = new ArrayList<>();
        availableAminoAcids = new ArrayList<>();

        // Hide game and result buttons
        resultsPanelVBox.setVisible(false);
        gameButtonHBox.setVisible(false);

        // Set main label
        gameLabel.setText("Welcome to Chemical Connect!");

        // Load available amino acids
        for(final char aa : AMINO_ACIDS.toCharArray())
        {
            availableAminoAcids.add(aa);
        }

        // Set button handlers
        startGame.setOnAction(e -> nextQuestion(e));
        rules.setOnAction(e -> showRules());
        submit.setOnAction(e -> submitEvent(e));

        //debugg
        //        System.out.println("inside initilize: " + bonds);

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

        pane.getChildren().add(currentBond);
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
                //                for(Bond[] bond : bonds)
                //                {
                //                    System.out.println(Arrays.toString(bond) + "hehe");
                //                }
                addClickListenerToLine(currentBond, pane); // Add click listener for removal
            }
            else if(existingBond.length == 1)
            {
                // Note 3: see JavaDoc
                pane.getChildren().remove(currentBond);

                // Add a second (parallel) line
                final Bond offsetDoubleBond;
                offsetDoubleBond = createParallelLine(existingBond[0], targetElement);//@@@@@@@@@@@@

                bonds.add(new Bond[]{existingBond[0], offsetDoubleBond});
                //debugging
                //                System.out.println("Start second bond: ");
                //                for(Bond[] bond : bonds)
                //                {
                //                    System.out.println(Arrays.toString(bond));
                //                }
                pane.getChildren().add(offsetDoubleBond);
                addClickListenerToLine(offsetDoubleBond, pane); // Add click listener for removal
            }
            else
            {
                // If two or more bonds exist: reject the action
                pane.getChildren().remove(currentBond); // Remove the redundant line
            }
        }
        else
        {
            // Remove the temporary line if the connection is invalid
            pane.getChildren().remove(currentBond);
            //debugging
            //            System.out.println("invalid bonds?: ");
            //            for(Bond[] bond : bonds)
            //            {
            //                System.out.println(Arrays.toString(bond));
            //            }
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
        for(final Node node : pane.getChildren())
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
                if((bond.getStartX() == e1.getCenterX() && bond.getStartY() == e1.getCenterY() && bond.getEndX() == e2.getCenterX() && bond.getEndY() == e2.getCenterY()) || (bond.getStartX() == e2.getCenterX() && bond.getStartY() == e2.getCenterY() && bond.getEndX() == e1.getCenterX() && bond.getEndY() == e1.getCenterY()))
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
                                   //                                   System.out.println("removing bond");
                                   //                                   for(Bond[] currentBond : bonds)
                                   //                                   {
                                   //                                       System.out.println(Arrays.toString(currentBond));
                                   //                                   }
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
            if(Arrays.asList(bondPair).contains(bond))
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
                pane.getChildren().remove(bond);
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
        final Bond parallelLine = new Bond(bond.getStartX() + px, bond.getStartY() + py, bond.getEndX() + px, bond.getEndY() + py, selectedElement, targetElement);
        parallelLine.setStrokeWidth(DOUBLE_BOND_WIDTH);

        return parallelLine;
    }

    @FXML
    public void nextQuestion(final ActionEvent actionEvent)
    {
        // Hide home buttons | Show game buttons
        removeHomeButtons();
        resultsPanelVBox.setVisible(false);
        gameButtonHBox.setVisible(true);

        // Remove previous question (bonds, currentAminoAcid)
        removeQuestion();

        // Now generate next question
        currentAminoAcid = new AminoAcid(generateRandomAA());
        gameLabel.setText("Draw the following amino acid:\n" + currentAminoAcid);

        // Get the GameElement positions (as a map)
        currentAminoAcidGUI = currentAminoAcid.getAminoAcidElements();

        // Insert GameElements onto GUI
        for(final GameElement element : currentAminoAcidGUI)
        {
            addMouseEventHandlers(element, gamePane);
            gamePane.getChildren().add(element); // Add the dot to the pane
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
    private void removeQuestion()
    {
        currentAminoAcid = null;
        if(currentAminoAcidGUI != null)
        {
            for(final GameElement element : currentAminoAcidGUI)
            {
                gamePane.getChildren().remove(element);
            }
            currentAminoAcidGUI.clear();
        }

        if(bonds != null)
        {
            for(final Bond[] bond : bonds)
            {
                for(final Bond bond1 : bond)
                {
                    gamePane.getChildren().remove(bond1);
                }
            }
            bonds.clear();
        }
    }

    /**
     * When user hits submit button. Save amino acid
     * <p>
     * Note 1: Even though you're adding userValuePlaceholder to tempMapValues before overwriting the next bond, the
     * list is storing a reference to the same array object, not a copy of its contents. Updating the
     * placeholder affects all the entries in the list that references it.
     */
    private void submitEvent(final ActionEvent e)
    {
        // userKey: Amino Acid ID | userValuePlaceholder: Array of bonds ex [TC1, TN1]
        final String              userKey;
        final ArrayList<String[]> tempMapValues;

        userKey       = currentAminoAcid.getAminoAcidID();
        tempMapValues = new ArrayList<>();

        if(bonds != null && !bonds.isEmpty())
        {

            for(final Bond[] bond : bonds)
            {
                // Note 1: See javaDoc
                String[] userValuePlaceholder;

                userValuePlaceholder = new String[LENGTH_USER_BONDS];
                usersAnswersAsBonds.put(userKey, new ArrayList<>());

                if(bond.length == SINGLE_BOND_LENGTH)
                {
                    // Store element1 and element2 in temp String[]
                    userValuePlaceholder[0] = bond[SINGLE_BOND_INDEX].getElement1().toString();
                    userValuePlaceholder[1] = bond[SINGLE_BOND_INDEX].getElement2().toString();
                    // Add to tempMapValue Array AND usersAnswersAsBonds map
                    tempMapValues.add(userValuePlaceholder);
                    usersAnswersAsBonds.get(userKey).add(bond[SINGLE_BOND_INDEX]);
                }
                else if(bond.length == DOUBLE_BOND_LENGTH)
                {
                    userValuePlaceholder[0] = bond[DOUBLE_BOND_INDEX].getElement1().toString();
                    userValuePlaceholder[1] = bond[DOUBLE_BOND_INDEX].getElement2().toString();
                    // Add to tempMapValue Array
                    tempMapValues.add(userValuePlaceholder);
                    usersAnswersAsBonds.get(userKey).add(bond[DOUBLE_BOND_INDEX]);
                }
            }

            // update answer key | increase question count | increase questions answered
            usersAnswers.put(userKey, tempMapValues);
            questionCount++;
            totalQuestionsAnswered++;

            //debugging
            for(Map.Entry<String, ArrayList<String[]>> entry : usersAnswers.entrySet())
            {
                System.out.print("INSIDE SUBMIT EVENT!!!!" + entry.getKey() + ": ");

                // Loop through the ArrayList
                for(final String[] array : entry.getValue())
                {
                    System.out.println(Arrays.toString(array));  // Print each String[] as a string
                }
            }

            // Call next question after storing user results
            if(questionCount < MAX_QUESTION_COUNT)
            {
                System.out.println(questionCount);
                nextQuestion(e);
            }
            else
            {
                endSession(e);
            }
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
        alert.setTitle("Oops!");

        // Set the header text (optional)
        alert.setHeaderText("Cannot submit a blank answer!");

        // Set the content text to the error message
        alert.setContentText("Please add at least one bond.");

        // Show the alert and wait for the user to close it
        alert.showAndWait();

    }

    public char generateRandomAA()
    {

        if(availableAminoAcids.isEmpty())
        {
            throw new IllegalStateException("No more amino acids to generate!");
        }

        final Random random;
        random = new Random();

        // Remove and return a random amino acid
        return availableAminoAcids.remove(random.nextInt(availableAminoAcids.size()));
    }

    @FXML
    private void showRules()
    {
        Alert         rules;
        StringBuilder sb;

        rules = new Alert(Alert.AlertType.INFORMATION);
        sb    = new StringBuilder();

        rules.setTitle("Rules🧪");
        rules.setHeaderText("Rules of Chemical Connect");

        // Build rule message
        sb.append("Objective: 'draw' the amino acid by connecting the dots!\n");
        sb.append("You will be prompted 5 randomized amino acids.\n\n");
        sb.append("1. To add a bond, drag the mouse from a circle to the next circle.\n");
        sb.append("2. To remove a bond, simply click on it.\n");
        sb.append("3. Hit submit when you are finished.\n");
        sb.append("4. Hit quit when you do NOT want to complete all 5 questions.\n");
        sb.append("5. Your score is displayed once the game ends.\n");
        sb.append("\tYour correct and incorrect amino acids will be listed.\n\n\n");
        sb.append("Note: You cannot submit blank responses.\n");
        rules.setContentText(sb.toString());
        rules.showAndWait();
    }

    public void endSession(final ActionEvent actionEvent)
    {
        // remove buttons (submit)
        gameButtonHBox.setVisible(false);
        removeQuestion();
        checkAllResponses();

        // show result panel
        resultsPanelVBox.setVisible(true);

        // display results
    }

    public void checkAllResponses()
    {
        // Get answer key from aminoAcidShop
        AminoAcidShop                    shop;
        Map<String, ArrayList<String[]>> answerKey;

        shop                = new AminoAcidShop();
        answerKey           = shop.getAnswerKey();
        correctAminoAcids   = new ArrayList<>();
        incorrectAminoAcids = new ArrayList<>();

        // Iterate through the usersAnswer's set: comparing the bond lists for each response
        for(final String key : usersAnswers.keySet())
        {
            final ArrayList<String[]> expectedBonds;
            final ArrayList<String[]> userBonds;

            expectedBonds = answerKey.get(key);
            userBonds     = usersAnswers.get(key);

            if(checkOneResponse(expectedBonds, userBonds))
            {
                score++;

                // Add correct amino acid
                final AminoAcid correctAminoAcid;
                correctAminoAcid = new AminoAcid(key.charAt(0));
                correctAminoAcids.add(correctAminoAcid);

            }
            else
            {
                // Add incorrect amino acid
                final AminoAcid incorrectAminoAcid;
                incorrectAminoAcid = new AminoAcid(key.charAt(0));
                incorrectAminoAcids.add(incorrectAminoAcid);

            }
        }
        //debugging
        System.out.println("CORRECT AS ARAY" + correctAminoAcids);
        System.out.println("INCORRECT AS ARAY" + incorrectAminoAcids);
        displayResults();
    }

    private boolean checkOneResponse(final ArrayList<String[]> expectedBonds, final ArrayList<String[]> userBonds)
    {

        // First: check if correct # of bonds were submitted. Note: .containsAll() doesn't account for bond frequency
        if(expectedBonds.size() != userBonds.size())
        {
            return false;
        }

        // Second: check if all expected bonds exist in userResponse, then check if frequency matches
        for(final String[] bond : expectedBonds)
        {
            if(!bondExistsIn(bond, userBonds))
            {
                return false;
            }
        }

        // Third: check if bond frequencies in answer key match user's bond frequency
        if(!bondFrequencyEqual(expectedBonds, userBonds))
        {
            return false;
        }

        return true;
    }

    private boolean bondFrequencyEqual(final ArrayList<String[]> bond, final ArrayList<String[]> userBonds)
    {
        // copy and normalize both lists
        final ArrayList<String> normalizedBond;
        final ArrayList<String> normalizedUserBonds;

        normalizedBond      = normalizeAndSort(bond);
        normalizedUserBonds = normalizeAndSort(userBonds);

        // Check if the two normalized lists are equal
        return normalizedBond.equals(normalizedUserBonds);
    }

    /**
     * Helper method to normalize and sort a list of String[] elements
     */
    private ArrayList<String> normalizeAndSort(ArrayList<String[]> bonds)
    {
        final ArrayList<String> normalizedList;
        normalizedList = new ArrayList<>();

        for(final String[] bond : bonds)
        {
            // Normalize each pair (order-independent)
            normalizedList.add(normalizeBonds(bond));
        }

        // Sort the normalized list for comparison
        Collections.sort(normalizedList);
        return normalizedList;
    }

    /**
     * Helper method to normalize a String[] (e.g., ["a", "b"] and ["b", "a"] become the same)
     *
     * @param bond
     *
     * @return
     */
    private String normalizeBonds(String[] bond)
    {
        if(bond.length != 2)
        {
            throw new IllegalArgumentException("Each bond must be a pair of two strings");
        }

        // Sort the bond to make the order irrelevant
        final String[] sortedBond;
        sortedBond = bond.clone();
        Arrays.sort(sortedBond);

        // Convert to a String
        return Arrays.toString(sortedBond);
    }

    private boolean bondExistsIn(final String[] bond, final ArrayList<String[]> userBonds)
    {
        for(final String[] uBond : userBonds)
        {
            if(isBondEqual(uBond, bond))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isBondEqual(final String[] uBond, final String[] bond)
    {
        final String  userElement1;
        final String  userElement2;
        final String  keyElement1;
        final String  keyElement2;
        final boolean result;

        userElement1 = uBond[E1_INDEX];
        userElement2 = uBond[E2_INDEX];
        keyElement1  = bond[E1_INDEX];
        keyElement2  = bond[E2_INDEX];

        // Returns equality in any order: Ie [A,B] == [B,A]
        result = (userElement1.equals(keyElement1) && userElement2.equals(keyElement2)) || (userElement1.equals(keyElement2) && userElement2.equals(keyElement1));

        return result;
    }

    private void displayResults()
    {
        final StringBuilder sbCorrect;
        final StringBuilder sbIncorrect;
        final String        correctAA;
        final String        incorrectAA;
        final double        scoreAsPercent;

        sbCorrect   = new StringBuilder("The amino acids you drew correctly: \n");
        sbIncorrect = new StringBuilder("The amino acids you drew incorrectly: \n");

        // add correct amino acids as a string, via a stream
        correctAA = correctAminoAcids.stream().map(AminoAcid::toString).collect(Collectors.joining("\n"));
        // add incorrect amino acids as a string, via a stream
        incorrectAA = incorrectAminoAcids.stream().map(AminoAcid::toString).collect(Collectors.joining("\n"));

        // calculate percent score
        scoreAsPercent = (totalQuestionsAnswered == NO_QUESTIONS_ANSWERED) ? NO_QUESTIONS_ANSWERED : (double) score / totalQuestionsAnswered * PERCENT;

        sbCorrect.append(correctAA);
        sbIncorrect.append(incorrectAA);

        // Set up gui for displaying results
        gameLabel.setText("Thanks for playing Khemical Konnect! Try Again?");
        scoreLabel.setText(String.format("You scored %d out of %d", score, totalQuestionsAnswered));
        scorePercentLabel.setText(String.format("%.1f%%", scoreAsPercent));
        scoreDetailsLabel.setText(sbCorrect + "\n" + sbIncorrect);

    }

    public void restartGame(final ActionEvent e)
    {
        // Restart score | question count | usersAnswers | usersAnswersAsBonds | availableAminoAcids
        score                  = 0;
        questionCount          = 0;
        totalQuestionsAnswered = 0;
        usersAnswers.clear();
        usersAnswersAsBonds.clear();
        availableAminoAcids.clear();
        correctAminoAcids.clear();
        incorrectAminoAcids.clear();

        // Reset labels
        gameLabel.setText("Welcome to Khemical Konnect!");

        // Reset available amino acids
        for(final char aa : AMINO_ACIDS.toCharArray())
        {
            availableAminoAcids.add(aa);
        }

        nextQuestion(e);
    }

    public void closeApplication(final ActionEvent actionEvent)
    {
        Platform.exit();
    }
}


