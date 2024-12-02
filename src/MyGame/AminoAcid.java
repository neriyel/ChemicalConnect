package MyGame;

import java.util.ArrayList;

/**
 * Represents a single Amino Acid. The specific amino acid is determined when it is instantiated.
 */
public class AminoAcid
{
    // Static variables
    private static final int ELEMENT_INDEX_START = 1; // starts at 1 because 0th index is Amino Acid name and not a elements
    private static final int ELEMENT_CHAR_INDEX  = 1; // ElementID ex: AC1, c is first index in ID
    private static final int ELEMENT_SPLIT_PARTS = 3; // should always be exactly 3 (name, x, y)
    private static final int ELEMENT_ID_INDEX    = 0;
    private static final int ELEMENT_X_INDEX     = 1;
    private static final int ELEMENT_Y_INDEX     = 2;

    private static final char CARBON_ID   = 'C';
    private static final char NITROGEN_ID = 'N';
    private static final char OXYGEN_ID   = 'O';
    private static final char SULPHUR_ID  = 'S';

    // Instance variables (final)
    private final String                 aminoAcid;
    private final ArrayList<GameElement> elements;

    // Instance variables (non-final)

    /**
     * Constructor for AminoAcid objects
     *
     * @param aa is the AA specified
     */
    public AminoAcid(final char aa) throws ElementsOverlappingOnScreenException
    {
        this.aminoAcid = String.valueOf(aa);
        this.elements  = new ArrayList<>();

        createAminoAcid(aminoAcid);
    }

    /**
     * Returns a list of GameElements (dots with positions), representing a whole amino acid
     * <p>
     * populate ArrayList<GameElement>
     *
     * @param aminoAcid is the target amino acid
     */
    private void createAminoAcid(final String aminoAcid) throws ElementsOverlappingOnScreenException
    {
        final AminoAcidShop     shop;
        final ElementFactory    carbonFactory;
        final ElementFactory    nitrogenFactory;
        final ElementFactory    oxygenFactory;
        final ElementFactory    sulphurFactory;
        final ArrayList<String> aminoPositions;

        shop            = new AminoAcidShop();
        carbonFactory   = new CarbonFactory();
        nitrogenFactory = new NitrogenFactory();
        oxygenFactory   = new OxygenFactory();
        sulphurFactory  = new SulphurFactory();
        aminoPositions  = shop.getAminoAcidAsMapValue(aminoAcid);

        if(aminoPositions != null)
        {
            for(int i = ELEMENT_INDEX_START; i < aminoPositions.size(); i++)
            {
                final String   currentElement;
                final String[] temp;
                final String   elementID;
                final double   elementPosX;
                final double   elementPosY;
                final char     elementCalled;

                currentElement = aminoPositions.get(i);
                temp           = currentElement.split(" ", ELEMENT_SPLIT_PARTS);
                elementID      = temp[ELEMENT_ID_INDEX];
                elementPosX    = Double.parseDouble(temp[ELEMENT_X_INDEX]);
                elementPosY    = Double.parseDouble(temp[ELEMENT_Y_INDEX]);
                elementCalled  = elementID.charAt(ELEMENT_CHAR_INDEX);

                // Determine which element is being called
                switch(elementCalled)
                {
                    case CARBON_ID:
                        elements.add(carbonFactory.createElement(elementID, elementPosX, elementPosY));
                        break;
                    case NITROGEN_ID:
                        elements.add(nitrogenFactory.createElement(elementID, elementPosX, elementPosY));
                        break;
                    case SULPHUR_ID:
                        elements.add(sulphurFactory.createElement(elementID, elementPosX, elementPosY));
                        break;
                    case OXYGEN_ID:
                        elements.add(oxygenFactory.createElement(elementID, elementPosX, elementPosY));
                        break;
                }
            }
        }

        checkForOverlappingCircles();

    }

    /**
     * Getter for amino acid's actual element postions
     * @return the amino acid configuration as an ArrayList<GameElement>
     */
    public final ArrayList<GameElement> getAminoAcidElements()
    {
        return this.elements;
    }

    /**
     * Getter for amino acid ID
     * @return
     */
    public final String getAminoAcidID()
    {
        return this.aminoAcid;
    }

    /**
     * To string representation of amino acid. Returns amino acid name from it's ID
     * @return
     */
    @Override
    public String toString()
    {
        return switch(String.valueOf(this.getAminoAcidID()))
        {
            case "A" -> "Alanine";
            case "R" -> "Arginine";
            case "N" -> "Asparagine";
            case "D" -> "Aspartic acid";
            case "C" -> "Cysteine";
            case "E" -> "Glutamic acid";
            case "Q" -> "Glutamine";
            case "G" -> "Glycine";
            case "H" -> "Histidine";
            case "I" -> "Isoleucine";
            case "L" -> "Leucine";
            case "K" -> "Lysine";
            case "M" -> "Methionine";
            case "F" -> "Phenylalanine";
            case "P" -> "Proline";
            case "S" -> "Serine";
            case "T" -> "Threonine";
            case "W" -> "Tryptophan";
            case "Y" -> "Tyrosine";
            case "V" -> "Valine";
            default -> "Unknown amino acid";
        };
    }

    /**
     * Checks for overlapping elements on the GUI. Prevents this.
     *
     * @throws ElementsOverlappingOnScreenException prevents overlapping elements on screen.
     */
    public final void checkForOverlappingCircles() throws ElementsOverlappingOnScreenException
    {
        if(elements != null)
        {
            for(int i = 0; i < elements.size(); i++)
            {
                final GameElement circle1;
                circle1 = elements.get(i);

                for(int j = i + 1; j < elements.size(); j++)
                {
                    final GameElement circle2;
                    final double      dx;
                    final double      dy;
                    final double      distance;

                    circle2  = elements.get(j);
                    dx       = circle1.getCenterX() - circle2.getCenterX();
                    dy       = circle1.getCenterY() - circle2.getCenterY();
                    distance = Math.sqrt(dx * dx + dy * dy);

                    if(distance < circle1.getRadius() + circle2.getRadius())
                    {
                        throw new ElementsOverlappingOnScreenException(
                                "Overlapping detected between circles at indices " + i + " and " +
                                        j + ": Circle1 (" + circle1.getCenterX() + ", " +
                                        circle1.getCenterY() + ") and Circle2 (" +
                                        circle2.getCenterX() + ", " + circle2.getCenterY() + ")");
                    }
                }
            }
        }

    }

}
