import WordGame.WordGame;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("""
                    Main Menu:
                    Press W to play the Word game.
                    Press N to play the Number game.
                    Press M to play MyGame.
                    Press Q to quit.
                    """);

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "W" -> new WordGame().play();
                case "N" -> GameLauncher.launchGame(NumberGame.NumberGame.class);
                case "M" -> GameLauncher.launchGame(MyGame.MyGame.class);
                case "Q" -> {
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid input! Please enter W, N, M, or Q.");
            }
        }

        scanner.close();
    }
}
