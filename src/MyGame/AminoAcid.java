package MyGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single Amino Acid. The specific amino acid is determined when it is instantiated.
 */
public class AminoAcid
{
    // Instance variables (final)
    private final String            aminoAcid;
    private       List<GameElement> elements;

    public AminoAcid(final String aa)
    {
        this.aminoAcid = aa;
        this.elements  = createAminoAcid(aminoAcid);
    }

    /**
     * Returns a list of GameElements (dots with positions), representing a whole amino acid
     *
     * @param aminoAcid
     *
     * @return
     */
    private List<GameElement> createAminoAcid(final String aminoAcid)
    {
        final AminoAcidShop shop;
        shop = new AminoAcidShop();

        shop.getAminoAcid("Alanine");

        ElementFactory carbonFactory = new CarbonFactory();
        //        ElementFactory nitrogenFactory = new NitrogenFactory();
        //        ElementFactory oxygenFactory   = new OxygenFactory();
        //        ElementFactory sulphurFactory  = new SulphurFactory();

        // TODO: REFACTOR TO SCAN AND READ FILE AS A STREAM
        GameElement c1 = carbonFactory.createElement(200, 150);
        GameElement c2 = carbonFactory.createElement(300, 150);

        elements = new ArrayList<>();

        // TODO: add null pointer prevention styule jason blaj
        elements.add(c1);
        elements.add(c2);

        return elements;
    }

    public List<GameElement> getAminoAcidElements()
    {
        return this.elements;
    }

    public void answerKey()
    {
        // list of correct bonds
    }
}
