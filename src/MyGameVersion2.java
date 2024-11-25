import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MyGameVersion2 extends Application {

    private Circle selectedDot = null;
    private Line currentLine = null;
    private final List<Line[]> bonds = new ArrayList<>(); // Track all bonds (single and double)

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 800, 600);

        // Create dots
        Circle dot1 = createDot(200, 200, 10, pane);
        Circle dot2 = createDot(400, 200, 10, pane);
        Circle dot3 = createDot(300, 400, 10, pane);

        pane.getChildren().addAll(dot1, dot2, dot3);

        primaryStage.setTitle("Connect the Dots with Bonds");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a dot on the pane and sets up event handlers for interactions.
     * @param x The x-coordinate of the dot.
     * @param y The y-coordinate of the dot.
     * @param radius The radius of the dot.
     * @param pane The pane where the dot will be added.
     * @return The created Circle object representing the dot.
     */
    private Circle createDot(double x, double y, double radius, Pane pane) {
        Circle circle = new Circle(x, y, radius, Color.DEEPSKYBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        circle.setOnMouseEntered(e -> circle.setFill(Color.LIGHTGREEN));
        circle.setOnMouseExited(e -> circle.setFill(Color.DEEPSKYBLUE));
        circle.setOnMousePressed(e -> onDotPressed(circle, pane, e));
        circle.setOnMouseDragged(this::onDotDragged);
        circle.setOnMouseReleased(e -> onDotReleased(circle, pane, e));

        return circle;
    }

    /**
     * Handles when a dot is pressed, creating a temporary line for bonding.
     * @param circle The dot being pressed.
     * @param pane The pane where the dot and line are located.
     * @param event The mouse event triggered by pressing the dot.
     */
    private void onDotPressed(Circle circle, Pane pane, MouseEvent event) {
        selectedDot = circle;
        currentLine = new Line();
        currentLine.setStartX(circle.getCenterX());
        currentLine.setStartY(circle.getCenterY());
        currentLine.setEndX(circle.getCenterX());
        currentLine.setEndY(circle.getCenterY());
        currentLine.setStroke(Color.BLACK);
        currentLine.setStrokeWidth(2);

        pane.getChildren().add(currentLine);
    }

    /**
     * Updates the temporary line's end position as the dot is dragged.
     * @param event The mouse event triggered while dragging the dot.
     */
    private void onDotDragged(MouseEvent event) {
        if (currentLine != null) {
            currentLine.setEndX(event.getX());
            currentLine.setEndY(event.getY());
        }
    }

    /**
     * Handles when a dot is released and manages bond creation or removal.
     * @param circle The dot being released.
     * @param pane The pane where the dots and lines are located.
     * @param event The mouse event triggered by releasing the dot.
     */
    private void onDotReleased(Circle circle, Pane pane, MouseEvent event) {
        Circle targetDot = getDotAt(event.getX(), event.getY(), pane);

        if (targetDot != null && targetDot != selectedDot) {
            // Check existing bonds
            Line[] existingBond = findBond(selectedDot, targetDot);

            if (existingBond.length == 0) {
                // No bonds exist: create the first bond
                currentLine.setEndX(targetDot.getCenterX());
                currentLine.setEndY(targetDot.getCenterY());
                bonds.add(new Line[]{currentLine}); // Add single bond
                addClickListenerToLine(currentLine, pane); // Add click listener for removal
            } else if (existingBond.length == 1) {
                // Single bond exists: create a double bond
                pane.getChildren().remove(currentLine); // Remove the temporary line

                // Add a second (parallel) line
                Line offsetLine = createParallelLine(existingBond[0]);
                bonds.add(new Line[]{existingBond[0], offsetLine});
                pane.getChildren().add(offsetLine);
                addClickListenerToLine(offsetLine, pane); // Add click listener for removal
            } else if (existingBond.length >= 2) {
                // If two or more bonds exist: reject the action
                pane.getChildren().remove(currentLine); // Remove the redundant line
            }
        } else {
            // Remove the temporary line if the connection is invalid
            pane.getChildren().remove(currentLine);
        }

        // Reset state
        selectedDot = null;
        currentLine = null;
    }

    /**
     * Finds the dot that the mouse is hovering over.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     * @param pane The pane containing the dots.
     * @return The dot the mouse is over, or null if none.
     */
    private Circle getDotAt(double x, double y, Pane pane) {
        for (var node : pane.getChildren()) {
            if (node instanceof Circle) {
                Circle dot = (Circle) node;
                if (dot.contains(x, y)) {
                    return dot;
                }
            }
        }
        return null;
    }

    /**
     * Checks if a bond already exists between two dots.
     * @param dot1 The first dot.
     * @param dot2 The second dot.
     * @return An array of existing lines (bonds) between the dots.
     */
    private Line[] findBond(Circle dot1, Circle dot2) {
        List<Line> bondsBetween = new ArrayList<>();

        for (Line[] bondPair : bonds) {
            for (Line bond : bondPair) {
                if ((bond.getStartX() == dot1.getCenterX() && bond.getStartY() == dot1.getCenterY() &&
                        bond.getEndX() == dot2.getCenterX() && bond.getEndY() == dot2.getCenterY()) ||
                        (bond.getStartX() == dot2.getCenterX() && bond.getStartY() == dot2.getCenterY() &&
                                bond.getEndX() == dot1.getCenterX() && bond.getEndY() == dot1.getCenterY())) {
                    bondsBetween.add(bond);
                }
            }
        }

        return bondsBetween.toArray(new Line[0]);
    }

    /**
     * Adds a click listener to a line, allowing it to be removed by clicking.
     * @param line The line to add the click listener to.
     * @param pane The pane where the line is located.
     */
    private void addClickListenerToLine(Line line, Pane pane) {
        line.setOnMouseClicked(event -> {
            // Remove the clicked line from the pane
            pane.getChildren().remove(line);

            // Remove the bond from the list
            removeBondFromList(line);
        });
    }

    /**
     * Removes the specified bond (line) from the bond list.
     * @param line The line to remove from the bond list.
     */
    private void removeBondFromList(Line line) {
        for (Iterator<Line[]> iter = bonds.iterator(); iter.hasNext(); ) {
            Line[] bondPair = iter.next();
            if (Arrays.asList(bondPair).contains(line)) {
                iter.remove();
                break; // Stop once the bond is found and removed
            }
        }
    }

    /**
     * Creates a parallel line to an existing line, offset by a small value.
     * @param line The original line.
     * @return A new Line object that is parallel to the original.
     */
    private Line createParallelLine(Line line) {
        // Calculate offset for parallel line
        double offset = 5.0;
        double dx = line.getEndX() - line.getStartX();
        double dy = line.getEndY() - line.getStartY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalize to get unit vector
        double ux = dx / length;
        double uy = dy / length;

        // Perpendicular vector for offset
        double px = -uy * offset;
        double py = ux * offset;

        // Create parallel line
        return new Line(
                line.getStartX() + px, line.getStartY() + py,
                line.getEndX() + px, line.getEndY() + py
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
