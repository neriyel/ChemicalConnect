package WordGame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordGame
{

    private final World   earth;
    private final Scanner sc;

    public WordGame(World earth, Scanner sc)
    {
        this.earth = earth;
        this.sc    = sc;
    }

    public void play() throws IOException
    {

        boolean playingWord = true;



        int wordGamesPlayed      = 0;
        int correctFirstAttempt  = 0;
        int correctSecondAttempt = 0;
        int incorrect            = 0;

        while(playingWord)
        {

            System.out.println("Welcome to the Word Game!");
            wordGamesPlayed++;

            Random             rand         = new Random();
            final List<String> countryNames = new ArrayList<>(earth.getAllCountries()
                                                                      .keySet());

            // Loop through 10 questions for each game session
            for(int i = 0; i < 10; i++)
            {
                int           randomIndex       = rand.nextInt(countryNames.size());
                final String  randomCountryName = countryNames.get(randomIndex);
                final Country country           = earth.getCountry(randomCountryName);

                int    questionType = rand.nextInt(3); // 0, 1, or 2
                String userAnswer   = null;

                switch(questionType)
                {

                    // Case 0: Ask for the country with a specific capital city
                    case 0 ->
                    {
                        System.out.printf("What country has the capital city %s?%n", country.getCapitalCityName());
                        userAnswer = sc.nextLine()
                                .trim();

                        // Checking the user answer
                        if(userAnswer.equalsIgnoreCase(country.getName()))
                        {
                            System.out.println("CORRECT!");
                            correctFirstAttempt += 2;  // Award points for the correct first attempt
                        }
                        else
                        {
                            System.out.println("INCORRECT! Try again.");
                            userAnswer = sc.nextLine()
                                    .trim();

                            if(userAnswer.equalsIgnoreCase(country.getName()))
                            {
                                System.out.println("CORRECT on second attempt!");
                                correctSecondAttempt++;
                            }
                            else
                            {
                                System.out.printf("INCORRECT! The correct answer was: %s%n", country.getName());
                                incorrect++;
                            }
                        }
                    }

                    // Case 1: Ask for the capital city of a specific country
                    case 1 ->
                    {
                        System.out.printf("What is the capital of %s?%n", country.getName());
                        userAnswer = sc.nextLine()
                                .trim();

                        // Checking the user answer
                        if(userAnswer.equalsIgnoreCase(country.getCapitalCityName()))
                        {
                            System.out.println("CORRECT!");
                            correctFirstAttempt += 2;  // Award points for the correct first attempt
                        }
                        else
                        {
                            System.out.println("INCORRECT! Try again.");
                            userAnswer = sc.nextLine()
                                    .trim();

                            if(userAnswer.equalsIgnoreCase(country.getCapitalCityName()))
                            {
                                System.out.println("CORRECT on second attempt!");
                                correctSecondAttempt++;
                            }
                            else
                            {
                                System.out.printf("INCORRECT! The correct answer was: %s%n", country.getCapitalCityName());
                                incorrect++;
                            }
                        }
                    }

                    // Case 2: Ask which country is described by a random fact
                    case 2 ->
                    {
                        String randomFact = country.getFacts()[rand.nextInt(country.getFacts().length)];
                        System.out.printf("Which country is described by this fact: %s%n", randomFact);
                        userAnswer = sc.nextLine()
                                .trim();

                        // Checking the user answer
                        if(userAnswer.equalsIgnoreCase(country.getName()))
                        {
                            System.out.println("CORRECT!");
                            correctFirstAttempt += 2;  // Award points for the correct first attempt
                        }
                        else
                        {
                            System.out.println("INCORRECT! Try again.");
                            userAnswer = sc.nextLine()
                                    .trim();

                            if(userAnswer.equalsIgnoreCase(country.getName()))
                            {
                                System.out.println("CORRECT on second attempt!");
                                correctSecondAttempt++;
                            }
                            else
                            {
                                System.out.printf("INCORRECT! The correct answer was: %s%n", country.getName());
                                incorrect++;
                            }
                        }
                    }

                    // Default case (should not occur)
                    default -> System.out.println("Invalid question type!");
                }

            }

            // Display the results after each game session
            displayResults(wordGamesPlayed, correctFirstAttempt, correctSecondAttempt, incorrect);

            // Ask the user if they want to play again
            playingWord = promptPlayAgain();
        }
    }

    private void displayResults(int wordGamesPlayed, int correctFirstAttempt, int correctSecondAttempt, int incorrect) throws IOException
    {

        final LocalDateTime time;
        final Score         userScore;
        final List<Score>   scores;

        System.out.printf("Game Over!\nWord games played: %d\nCorrect on First Attempt: %d\nCorrect on Second Attempt: %d\nIncorrect: %d\n", wordGamesPlayed, correctFirstAttempt, correctSecondAttempt, incorrect);

        time      = LocalDateTime.now();
        userScore = new Score(time, wordGamesPlayed, correctFirstAttempt, correctSecondAttempt, incorrect);

        Score.appendScoreToFile(userScore, "score.txt");

        scores = Score.readScoresFromFile("score.txt");

        // Check high score
        final Score highScore = scores.stream()
                .max((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()))
                .orElse(null);

        if(highScore != null && userScore.getScore() < highScore.getScore())
        {
            System.out.printf("You did not beat the high score of %d points from %s.%n", highScore.getScore(), highScore.dateTimePlayed);
        }
        else
        {
            System.out.println("Congratulations! You set a new high score!");
        }
    }

    private boolean promptPlayAgain()
    {

        System.out.println("Play again? (Yes/No)");

        while(true)
        {
            String playAgain = sc.nextLine()
                    .trim()
                    .toLowerCase();
            if(playAgain.equals("yes"))
            {
                return true;
            }
            else if(playAgain.equals("no"))
            {
                return false;
            }
            else
            {
                System.out.println("Invalid input. Please type 'Yes' or 'No':");
            }
        }
    }

}
