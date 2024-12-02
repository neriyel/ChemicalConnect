import WordGame.World;
import WordGame.WordGame;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws IOException
    {

        // Create a Scanner object for user input
        WordGame wordgame = new WordGame();
        try
        {
            wordgame.play();

        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
