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
    private       ArrayList<GameElement> elements;

    /**
     * Constructor
     *
     * @param aa
     */
    public AminoAcid(final char aa)
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
     * @param aminoAcid
     *
     * @return
     */
    private void createAminoAcid(final String aminoAcid)
    {
        final AminoAcidShop  shop;
        final ElementFactory carbonFactory;
        //        final ElementFactory    nitrogenFactory;
        //        final ElementFactory    oxygenFactory;
        //        final ElementFactory    sulphurFactory;
        final ArrayList<String> aminoPositions;

        shop          = new AminoAcidShop();
        carbonFactory = new CarbonFactory();
        //        nitrogenFactory = new NitrogenFactory();
        //        oxygenFactory   = new OxygenFactory();
        //        sulphurFactory  = new SulphurFactory();
        aminoPositions = shop.getAminoAcidAsMapValue(aminoAcid);

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
                        break;
                }
            }
        }

    }

    public ArrayList<GameElement> getAminoAcid()
    {
        // debugging
        System.out.println("inside getAminoAcid" + this.elements);
        return this.elements;
    }

    public void answerKey()
    {
        // list of correct bonds
    }
}
