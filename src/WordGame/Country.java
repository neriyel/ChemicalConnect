package WordGame;

public class Country
{

    // instance variables (final)
    private final String countryName;
    private final String capitalCity;

    // instance variables (non-final)
    private final String[] facts;

    public Country(final String countrName, final String capitalCity, final String[] facts)
    {
        this.countryName = countrName;
        this.capitalCity = capitalCity;
        this.facts       = facts;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public String getCapitalCity()
    {
        return capitalCity;
    }

    public String[] getFacts()
    {
        return facts;
    }

    public final void setFactsAtIndex(final String fact, final int index)
    {
        facts[index] = fact;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb;
        sb = new StringBuilder();

        sb.append("Country: ");
        sb.append(countryName);
        sb.append("Capital:  \n");
        sb.append(capitalCity);
        sb.append("Facts: ");

        if(facts != null)
        {
            for(final String fact : facts)
            {
                sb.append("\n  - ");
                sb.append(fact);
            }
        }

        return sb.toString();
    }
}
