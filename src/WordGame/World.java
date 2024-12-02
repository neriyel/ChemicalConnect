package WordGame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World
{
    // static variables
    private static final String COUNTRY_IDENTIFIER         = ":";
    private static final int    COUNTRY_LINE_SPLIT_LIMITER = 2; // 2 because country:capital
    private static final int    COUNTRY_NAME_INDEX         = 0;
    private static final int    CAPITAL_CITY_INDEX         = 1;
    private static final int    COUNTRY_NUM_OF_FACTS       = 3;
    private static final int    FACTS_COUNTER_RESET        = 0;

    // instance variables (final)
    private final Map<String, Country> countries;

    // instance variables (non-final)
    private int factsCounter = FACTS_COUNTER_RESET;

    public World()
    {
        countries = new HashMap<>();
        processFiles();

    }

    public Country getCountry(String name)
    {
        return countries.get(name);
    }

    public Map<String, Country> getMapOfCountries()
    {
        return countries;
    }

    private void processFiles()
    {

        Path dirPath = null;
        final File folder;

        try
        {
            // Parse files
            dirPath = Path.of("src", "CountryFiles");
            folder  = new File(String.valueOf(dirPath));

            if(!folder.exists() || !folder.isDirectory())
            {
                System.out.println("Data folder missing or invalid!" + String.valueOf(dirPath));
                return;
            }

            Files.walk(dirPath)
                    .filter(path -> path.toString()
                            .endsWith(".txt"))
                    .forEach(path ->
                             {
                                 try
                                 {
                                     // Read line-by-line, updating the currentCountry being parsed
                                     final List<String> lines;
                                     String             currentCountryName;

                                     lines              = Files.readAllLines(path, StandardCharsets.UTF_8);
                                     currentCountryName = null;

                                     for(String line : lines)
                                     {
                                         // Trim and skip blank lines
                                         line = line.trim();
                                         if(line.isEmpty())
                                         {
                                             continue;
                                         }

                                         if(line.contains(COUNTRY_IDENTIFIER))
                                         {
                                             // New country identified, assign currentCountryName, capitalCity, and facts array
                                             final String[] tempBuffer;
                                             final String   capitalCity;
                                             final String[] facts;
                                             final Country  countryToAddToMap;

                                             tempBuffer         = line.split(COUNTRY_IDENTIFIER, COUNTRY_LINE_SPLIT_LIMITER);
                                             currentCountryName = tempBuffer[COUNTRY_NAME_INDEX].trim();
                                             capitalCity        = tempBuffer[CAPITAL_CITY_INDEX].trim();
                                             facts              = new String[COUNTRY_NUM_OF_FACTS];
                                             countryToAddToMap  = new Country(currentCountryName, capitalCity, facts);

                                             countries.put(currentCountryName, countryToAddToMap);
                                             // Reset facts array counter (since we're mapping a new country)
                                             factsCounter = FACTS_COUNTER_RESET;
                                         }
                                         else if(currentCountryName != null)
                                         {
                                             // Otherwise add facts to country facts array at specified index, THEN POST increment
                                             countries.get(currentCountryName)
                                                     .setFactsAtIndex(line.trim(), factsCounter++);
                                         }
                                     }

                                 }
                                 catch(IOException e)
                                 {
                                     throw new RuntimeException("Error reading file: " + path, e);
                                 }
                             });
        }
        catch(IOException e)
        {
            throw new RuntimeException("Error processing files in directory: " + dirPath, e);
        }
    }

}




