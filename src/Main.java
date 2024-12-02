import WordGame.World;
import WordGame.WordGame;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        // Create a World object to hold country data
        World earth = new World();

        // Create a Scanner object for user input
        try(Scanner sc = new Scanner(System.in))
        {
            // Create the WordGame object with the World and Scanner
            WordGame game = new WordGame(earth, sc);

            // Start the game
            try
            {
                game.play();
            }
            catch(IOException e)
            {
                System.err.println("An error occurred while running the game: " + e.getMessage());
            }
        }
    }
}
