package mycountrygame;

import java.util.*;

public class MultiplayerGame extends GameSession {
    
    private Queue<Player> players = new LinkedList<>();

    public MultiplayerGame(Country targetCountry, Map<String, Country> countryMap, String p1Name, String p2Name) {
        super(targetCountry, countryMap);
 
        players.offer(new Player(p1Name));
        players.offer(new Player(p2Name));
    }

    /**
     * Executes the multiplayer loop, polling from and offering back to the Queue to switch turns.
     */
    @Override
    public void play() {
        System.out.println("\n--- Multiplayer Mode (Steal Enabled) ---");
        System.out.println("I have selected a country! Type 'hint' if you get stuck.");
        boolean gameEnded = false;

        while (!gameEnded) {
            Player currentPlayer = players.poll(); // Retrieve the next player
            System.out.println("------------------------------------------------");
            System.out.println("It is " + currentPlayer.getName() + "'s turn!");
            
            boolean turnComplete = false;
            
            // Inner loop prevents the turn from switching if the input is invalid or an action (like hint/history)
            while (!turnComplete) {
                System.out.print("Enter your guess [or 'history', 'hint', 'give up']: ");
                String input = scanner.nextLine().trim();

                int previousGuessCount = currentPlayer.getNumberOfGuesses();
                
                gameEnded = processGuess(currentPlayer, input);
                
                if (gameEnded) {
                    turnComplete = true; 
                } else if (currentPlayer.getNumberOfGuesses() > previousGuessCount) {
                    // The guess count increased, meaning it was a valid guess! Turn is over.
                    turnComplete = true;
                    // Put the player back at the end of the line
                    players.offer(currentPlayer); 
                }
            }
        }
    }
}
