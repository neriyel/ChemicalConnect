package MyGame;

import java.util.ArrayList;
import java.util.List;

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

    public List<GameElement> createAminoAcid(final String aminoAcid)
    {
        ElementFactory carbonFactory   = new CarbonFactory();
//        ElementFactory nitrogenFactory = new NitrogenFactory();
//        ElementFactory oxygenFactory   = new OxygenFactory();
//        ElementFactory sulphurFactory  = new SulphurFactory();

        // TODO: REFACTOR TO SCAN AN READ FILE AS A STREAM
        GameElement c1 = carbonFactory.createElement(200, 150);
        GameElement c2 = carbonFactory.createElement(300, 150);

        elements = new ArrayList<>();

        // TODO: add null pointer prevention styule jason blaj
        elements.add(c1);
        elements.add(c2);

        return elements;
    }
}
