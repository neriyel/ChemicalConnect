package WordGame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordGame
{
    // static variables
    private static final int    RESET_VALUE        = 0;
    private static final int    MAX_QUESTIONS      = 10;
    private static final int    MAX_QUESTION_INDEX = 3;
    private static final String CORRECT_MESSAGE    = "CORRECT!";
    private static final String INCORRECT_MESSAGE  = "INCORRECT! The correct answer was: %s%n";

    // instance variables (final)
    private final World   world;
    private final Scanner scan;

    // instance variables (non-final)
    private int         totalGamesPlayed;
    private int         correctFirstAttempt;
    private int         correctSecondAttempt;
    private int         incorrect;
    private List<Score> scores;

    public WordGame()
    {
        this.world                = new World();
        this.scan                 = new Scanner(System.in);
        this.totalGamesPlayed     = RESET_VALUE;
        this.correctFirstAttempt  = RESET_VALUE;
        this.correctSecondAttempt = RESET_VALUE;
        this.incorrect            = RESET_VALUE;
        this.scores               = new ArrayList<>();
    }

    private boolean isCorrectAnswer(final String userResponse, final String correctAnswer)
    {
        return userResponse.equalsIgnoreCase(correctAnswer);
    }

    private void handleResponse(final String correctAnswer)
    {
        String userResponse = scan.nextLine()
                .trim();

        if(isCorrectAnswer(userResponse, correctAnswer))
        {
            System.out.println(CORRECT_MESSAGE);
            correctFirstAttempt += 2; // Award points for the correct first attempt
        }
        else
        {
            System.out.println("INCORRECT! Try again.");
            userResponse = scan.nextLine()
                    .trim();

            if(isCorrectAnswer(userResponse, correctAnswer))
            {
                System.out.println(CORRECT_MESSAGE + " on second attempt!");
                correctSecondAttempt++;
            }
            else
            {
                System.out.printf(INCORRECT_MESSAGE, correctAnswer);
                incorrect++;
            }
        }
    }

    public void play() throws IOException
    {
        boolean currentlyPlaying = true;

        while(currentlyPlaying)
        {
            System.out.println("Welcome to Word Game!");
            totalGamesPlayed++;

            final Random rand = new Random();
            final List<String> countryNames = new ArrayList<>(world.getMapOfCountries()
                                                                      .keySet());

            for(int i = 0; i < MAX_QUESTIONS; i++)
            {
                final int     randInt         = rand.nextInt(countryNames.size());
                final String  randCountryName = countryNames.get(randInt);
                final Country country         = world.getCountry(randCountryName);
                final int     questionIndex   = rand.nextInt(MAX_QUESTION_INDEX);

                switch(questionIndex)
                {
                    case 0 ->
                    {
                        // Case 0: Question: *country* | Answer: *capital city*
                        System.out.printf("This country has a capital city named %s?%n", country.getCapitalCity());
                        handleResponse(country.getCountryName());
                    }
                    case 1 ->
                    {
                        // Case 1: Ask for the capital city of a specific country
                        System.out.printf("What is the capital of %s?%n", country.getCountryName());
                        handleResponse(country.getCapitalCity());
                    }
                    case 2 ->
                    {
                        // Case 2: Ask which country is described by a random fact
                        String randomFact = country.getFacts()[rand.nextInt(country.getFacts().length)];
                        System.out.printf("This fun fact belongs to which country? : %s%n", randomFact);
                        handleResponse(country.getCountryName());
                    }
                    default -> System.out.println("Invalid question type!");
                }
            }

            // Display results and prompt user to play again
            displayResults();
            currentlyPlaying = restartGame();
        }
    }

    private void displayResults() throws IOException
    {
        System.out.printf("""
                                  Game Over!
                                  Word games played: %d
                                  Correct on First Attempt: %d
                                  Correct on Second Attempt: %d
                                  Incorrect: %d
                                  """, totalGamesPlayed, correctFirstAttempt, correctSecondAttempt, incorrect);

        final LocalDateTime time;
        Score               currentScore;

        time         = LocalDateTime.now();
        currentScore = new Score(time, totalGamesPlayed, correctFirstAttempt, correctSecondAttempt, incorrect);

        // First: append the current score to the score file
        Score.appendCurrentScoreToFile(currentScore, "score.txt");
        scores = Score.readAllScoresFromFile("score.txt");

        // Second: check for the currently highest score and notify user if they beat it or not
        Score.checkHighestScoreAndPrompt(scores, currentScore);
    }

    private boolean restartGame()
    {
        System.out.println("Play again? (Yes/No)");

        while(true)
        {
            switch(scan.nextLine()
                    .trim()
                    .toLowerCase())
            {
                case "yes" ->
                {
                    return true;
                }
                case "no" ->
                {
                    return false;
                }
                default -> System.out.println("Please only type 'Yes' or 'No':");
            }
        }
    }

}
