package WordGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Country
{

    // instance variables (final)
    private final String name;
    private final String capitalCityName;

    // instance variables (non-final)
    private String[] facts;

    public Country(String name, String capitalCityName, String[] facts)
    {
        this.name            = name;
        this.capitalCityName = capitalCityName;
        this.facts           = facts;
    }

    public String getName()
    {
        return name;
    }

    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    public String[] getFacts()
    {
        return facts;
    }

    public void setFactsAtIndex(final String fact, final int index)
    {
        facts[index] = fact;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Country: ")
                .append(name)
                .append(" Capital:  \n")
                .append(capitalCityName)
                .append(" Facts: ");
        for(String fact : facts)
        {
            sb.append("\n  - ")
                    .append(fact);  // Print each fact on a new line
        }
        System.out.println("\n");
        return sb.toString();
    }
}
