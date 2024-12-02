package MyGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * AminoAcidShop represents all 20 amino acids. Reads the file.
 */
public class AminoAcidShop
{
    // Static variables
    private static String AA_IDENTIFIER        = "[A-Z]";
    private static int    FIRST_ELEMENT_INDEX  = 0;
    private static int    SECOND_ELEMENT_INDEX = 1;

    // Instance variables (non-final)
    private final Map<String, ArrayList<String>>   aminoAcidReferences;
    private final Map<String, ArrayList<String[]>> answerKey;

    public AminoAcidShop()
    {
        this.aminoAcidReferences = new HashMap<>();
        this.answerKey           = new HashMap<>();
        populateAminoAcids();
        populateAminoAcidsAnswerKey();
    }

    private void populateAminoAcidsAnswerKey()
    {
        try
        {
            final Path           inputFile;
            final List<String>   tempList;
            final Stream<String> lines;

            inputFile = Paths.get("src", "MyGame", "answer.txt");
            tempList  = Files.readAllLines(inputFile);
            lines     = acidStream(tempList);

            // Parse file, and store in answerKey Map for each amino acid
            String currentAminoAcid = null;

            for(final String line : lines.toList())
            {
                // If new amino acid detected, input new Map Key
                if(line.matches(AA_IDENTIFIER))
                {
                    currentAminoAcid = line;
                    answerKey.put(currentAminoAcid, new ArrayList<>());
                }
                else if(currentAminoAcid != null)
                {
                    final Bond     bond;
                    final String[] tempAnswerKeyValue;
                    final String[] tempBuffer;

                    tempAnswerKeyValue = new String[2];
                    tempBuffer         = line.split(" ", 2);

                    // Else continue adding to current amino acid's Map Value
                    tempAnswerKeyValue[FIRST_ELEMENT_INDEX]  = tempBuffer[FIRST_ELEMENT_INDEX];
                    tempAnswerKeyValue[SECOND_ELEMENT_INDEX] = tempBuffer[SECOND_ELEMENT_INDEX];

                    // Add bond connections to Map's value
                    answerKey.get(currentAminoAcid)
                            .add(tempAnswerKeyValue);
                }
            }
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }

        //debugging
        //        System.out.println("INSIDE POPULATE ANSWER KEY!!!!");
        //        for(Map.Entry<String, ArrayList<String[]>> entry : answerKey.entrySet())
        //        {
        //            System.out.print(entry.getKey() + ": ");
        //
        //            // Loop through the ArrayList
        //            for(String[] array : entry.getValue())
        //            {
        //                System.out.println(Arrays.toString(array));  // Print each String[] as a string
        //            }
        //        }
    }

    /**
     * Parses the file and populates the aminoAcidReferences Map.
     *
     * @return
     */
    private void populateAminoAcids()
    {
        try
        {
            final Path           inputFile;
            final List<String>   tempList;
            final Stream<String> lines;

            // Set up path | temp List for text file lines | create non-null stream
            inputFile = Paths.get("src", "MyGame", "test.txt");
            tempList  = Files.readAllLines(inputFile);
            lines     = acidStream(tempList);

            // Parse file, and store in Map for each amino acid
            String currentAminoAcid = null;

            for(final String line : lines.toList())
            {
                if(line.isEmpty())
                {
                    continue;
                }
                // If new amino acid detected, input new Map Key
                if(line.matches(AA_IDENTIFIER))
                {
                    currentAminoAcid = line;
                    aminoAcidReferences.put(line, new ArrayList<>());
                }
                else if(currentAminoAcid != null)
                {
                    // Else continue adding to current amino acid's Map Value
                    aminoAcidReferences.get(currentAminoAcid)
                            .add(line);
                }

            }

            //debugging
            //            for(Map.Entry<String, ArrayList<String>> entry : aminoAcidReferences.entrySet())
            //            {
            //                System.out.println(entry.getKey() + ": " + entry.getValue());
            //            }

        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Filters out null strings if any
     *
     * @param tempList
     *
     * @return
     */
    private Stream<String> acidStream(final List<String> tempList)
    {
        return tempList.stream()
                .filter(Objects::nonNull);
    }

    /**
     * Goes into the map and returns its value
     *
     * @param aa is the requested amino acid
     */
    public ArrayList<String> getAminoAcidAsMapValue(final String aa)
    {
        //        System.out.println("Inside getAAAsMapValue: " + aminoAcidReferences.get(aa));
        return aminoAcidReferences.get(aa);
    }

    public Map<String, ArrayList<String[]>> getAnswerKey()
    {
        return answerKey;
    }

}


