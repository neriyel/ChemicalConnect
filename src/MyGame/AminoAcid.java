package MyGame;

import java.util.List;

public class AminoAcid
{
    // Instance variables (final)
    private final String aminoAcid;
    private final List<GameElement> elements;

    public AminoAcid(final String aa)
    {
        this.aminoAcid = aa;
        this.elements = createAminoAcid(aminoAcid);

    }

    private List<GameElement> createAminoAcid(final String aminoAcid)
    {
        return null;
    }
}
