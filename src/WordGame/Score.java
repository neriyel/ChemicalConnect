package WordGame;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Score
{
    // static variables
    private static final DateTimeFormatter FORMATTER             = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int               RESET_VALUE           = 0;
    private static final int               STATEMENT_SPLIT_LIMIT = 2;
    private static final int               VALUE_INDEX           = 1; // 1th index because statement[0]:value[1]

    final         String dateTimePlayed;
    private final int    numGamesPlayed;
    private final int    numCorrectFirstAttempt;
    private final int    numCorrectSecondAttempt;
    private final int    numIncorrectTwoAttempts;

    public Score(final LocalDateTime dateTime, final int games, final int first, final int second, final int incorrect)
    {
        this.dateTimePlayed          = dateTime.format(FORMATTER);
        this.numGamesPlayed          = games;
        this.numCorrectFirstAttempt  = first;
        this.numCorrectSecondAttempt = second;
        this.numIncorrectTwoAttempts = incorrect;
    }

    public static void appendCurrentScoreToFile(final Score score, final String fileName)
    {
        try(FileWriter writer = new FileWriter(fileName, true))
        {
            writer.write(score + System.lineSeparator());
        }
        catch(IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<Score> readAllScoresFromFile(final String filename) throws IOException
    {
        final List<Score> scores;
        scores = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            String line;
            String dateTime;
            int    gamesPlayed;
            int    firstAttempts;
            int    secondAttempts;
            int    incorrectAttempts;

            dateTime          = null;
            gamesPlayed       = RESET_VALUE;
            firstAttempts     = RESET_VALUE;
            secondAttempts    = RESET_VALUE;
            incorrectAttempts = RESET_VALUE;

            while((line = reader.readLine()) != null)
            {
                if(line.startsWith("Date and Time:"))
                {
                    dateTime = line.split(": ", STATEMENT_SPLIT_LIMIT)[VALUE_INDEX];
                }
                else if(line.startsWith("Games Played:"))
                {
                    gamesPlayed = Integer.parseInt(line.split(": ")[VALUE_INDEX]);
                }
                else if(line.startsWith("Correct First Attempts:"))
                {
                    firstAttempts = Integer.parseInt(line.split(": ")[VALUE_INDEX]);
                }
                else if(line.startsWith("Correct Second Attempts:"))
                {
                    secondAttempts = Integer.parseInt(line.split(": ")[VALUE_INDEX]);
                }
                else if(line.startsWith("Incorrect Attempts:"))
                {
                    incorrectAttempts = Integer.parseInt(line.split(": ")[VALUE_INDEX]);
                }
                else if(line.startsWith("Score:") && dateTime != null)
                {
                    final LocalDateTime parsedDate;
                    parsedDate = LocalDateTime.parse(dateTime, FORMATTER);

                    scores.add(new Score(parsedDate, gamesPlayed, firstAttempts, secondAttempts, incorrectAttempts));
                }
            }
        }
        return scores;
    }

    public static void checkHighestScoreAndPrompt(final List<Score> scores, final Score userScore)
    {
        final Score highScore;
        final int   userScoreValue;
        final int   highestScoreValue;

        highScore         = scores.stream().max((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore())).orElse(null);
        userScoreValue    = userScore.getScore();
        highestScoreValue = highScore.getScore();

        if(highScore == null || userScoreValue > highestScoreValue)
        {
            System.out.printf("You just set a new highscore of %d!!", userScoreValue);
        }
        else
        {
            System.out.printf("Sorry, you didnt beat the high score of %d points from %s.%n", highestScoreValue,
                              highScore.dateTimePlayed);
        }
    }

    @Override
    public String toString()
    {
        return String.format("""
                                     Date and Time: %s
                                     Games Played: %d
                                     Correct First Attempts: %d
                                     Correct Second Attempts: %d
                                     Incorrect Attempts: %d
                                     Score: %d points
                                     """, dateTimePlayed, numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt, numIncorrectTwoAttempts, getScore());
    }

    public int getScore()
    {
        return (numCorrectFirstAttempt * 2) + numCorrectSecondAttempt;
    }
}
