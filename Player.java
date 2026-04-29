package mycountrygame;

/**
 * Represents a player in the game, encapsulating their identity and current score.
 */
public class Player {
    private String name;
    private int numberOfGuesses;

    /**
     * Constructs a new Player with a starting guess count of 0.
     *
     * @param name The player's chosen name.
     */
    public Player(String name) {
        this.name = name;
        this.numberOfGuesses = 0;
    }

    public String getName() { 
        return name; 
    }
    
    public int getNumberOfGuesses() { 
        return numberOfGuesses; 
    }
    
    /** Increments the player's total number of guesses by 1. */
    public void incrementGuesses() { 
    	numberOfGuesses++;
    }
}