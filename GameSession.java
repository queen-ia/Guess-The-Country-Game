package mycountrygame;

import java.util.*;

public abstract class GameSession {
    protected Country targetCountry;
    /** State flag so that the hint is only used once per session. */
    protected boolean hintUsed = false;
    /** O(1) lookup map for validating and retrieving country objects from user strings. */
    protected Map<String, Country> countryMap;
    
    protected Set<String> guessedCountries = new HashSet<>();
    
    protected Stack<String> guessHistory = new Stack<>();
    
    protected LinkedList<DistanceRecord> closestGuesses = new LinkedList<>();
    
    protected Scanner scanner = new Scanner(System.in);

    /**
     * Initializes a new GameSession.
     *
     * @param targetCountry The randomly selected country to guess.
     * @param countryMap    The populated Map of all valid countries.
     */
    public GameSession(Country targetCountry, Map<String, Country> countryMap) {
        this.targetCountry = targetCountry;
        this.countryMap = countryMap;
    }

    /**
     * Abstract method forcing concrete subclasses to define their specific gameplay loop.
     */
    public abstract void play();

    /**
     * Processes a player's action or guess.
     *
     * @param player The player making the move.
     * @param input  The raw string input from the user.
     * @return True if the game has ended (won or gave up), False if the game continues.
     */
    protected boolean processGuess(Player player, String input) {
        if (input.equalsIgnoreCase("give up")) {
            System.out.println("\n " + player.getName() + " gave up.");
            System.out.println("The correct country was: " + targetCountry.getName() + "!");
            return true; 
        }

        if (input.equalsIgnoreCase("history")) {
            printHistory();
            return false; 
        }
        
        if (input.equalsIgnoreCase("hint")) {
            if (!hintUsed) {
                System.out.println("\n Hint: " + targetCountry.getHint() + "\n");  
                hintUsed = true;
            } else {
                System.out.println("\n The hint has already been used this session!\n");
            }
            return false; 
        }

        String formattedGuess = input.toLowerCase();

        if (!countryMap.containsKey(formattedGuess)) {
            System.out.println("Invalid country name. Please try again.");
            return false; 
        }

        if (guessedCountries.contains(formattedGuess)) {
            System.out.println("That country has already been guessed! Try another one.");
            return false; 
        }

        // At this point, it's a valid, brand new guess.
        player.incrementGuesses();
        guessedCountries.add(formattedGuess);
        Country guessedCountry = countryMap.get(formattedGuess);
        guessHistory.push(guessedCountry.getName());  
        
        double distance = guessedCountry.calculateDistanceInMiles(targetCountry);

        System.out.println("\nTotal Guesses: " + player.getNumberOfGuesses());

        if (distance == 0) {
            System.out.println("BINGO! " + player.getName() + " guessed correctly: " + targetCountry.getName() + "!");
            return true; 
        } else {
            System.out.printf("%s is %.2f miles away from the target country.\n", guessedCountry.getName(), distance);
            updateClosestGuesses(guessedCountry.getName(), distance);
            printTopClosest();
            return false; 
        }
    }

    /**
     * Updates the leaderboard of the top 3 closest guesses.
     * Iterates through the existing records to insert the new guess in ascending 
     * order of distance, ensuring the list never exceeds 3 items.
     *
     * @param name     The name of the guessed country.
     * @param distance The calculated distance from the target country in miles.
     */
    private void updateClosestGuesses(String name, double distance) {
        DistanceRecord newRecord = new DistanceRecord(name, distance);
        boolean added = false;
        int insertIndex = 0;
        
        // Loop through the existing records to find the correct sorted position
        for (DistanceRecord record : closestGuesses) {
            if (distance < record.distance) {
                closestGuesses.add(insertIndex, newRecord); 
                added = true;
                break;
            }
            insertIndex++;
        }
        
        // If it wasn't smaller than any existing distances, add it to the end
        if (!added) {
            closestGuesses.add(newRecord); 
        }
        
        // Keep the leaderboard strictly to the top 3 closest guesses
        if (closestGuesses.size() > 3) {
            closestGuesses.removeLast();
        }
    }
    /** Iterates over the sorted LinkedList to print the closest guesses. */
    private void printTopClosest() {
        System.out.println("--- Top Closest Guesses So Far ---");
        for (DistanceRecord record : closestGuesses) {
            System.out.printf("- %s: %.2f miles\n", record.countryname, record.distance);
        }
        System.out.println();
    }

    /** Pops/iterates through the Stack to display chronologically descending guesses. */
    private void printHistory() {
        System.out.println("\n--- Guess History (Most Recent First) ---");
        if (guessHistory.isEmpty()) {
            System.out.println("No guesses made yet.");
        } else {
            for (int i= guessHistory.size() -1; i>=0; i++)
            	
            	System.out.println("- " + guessHistory.get(i));
            
        }
        System.out.println();
    }
}