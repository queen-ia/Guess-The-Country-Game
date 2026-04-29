package mycountrygame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GuessTheCountryGame {
    /** Master list of all available countries, allowing for easy random selection via indices. */
    private List<Country> allCountries = new ArrayList<>();
    
    /** Master map of all countries for instant validation lookups. */
    private Map<String, Country> countryMap = new HashMap<>();

    public static void main(String[] args) {
        GuessTheCountryGame engine = new GuessTheCountryGame();
        engine.loadDataFromFile(args[0]); 
        engine.setupAndStart();
    }

    /**
     * Parses the tab-separated values file and populates the master ArrayList and Map.
     *
     * @param filename The path to the .tsv file containing country data.
     */
    private void loadDataFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true; 

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header line
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    double lat = Double.parseDouble(parts[1].trim());
                    double lon = Double.parseDouble(parts[2].trim());
                    String hint = parts[3].trim();

                    Country country = new Country(name, lat, lon, hint);
                    allCountries.add(country);
                    countryMap.put(name.toLowerCase(), country);
                }
            }
            System.out.println("Successfully loaded " + allCountries.size() + " countries.");
        } catch (IOException e) {
            System.out.println("Error reading the TSV file. Make sure 'countries.tsv' is in the correct directory.");
            e.printStackTrace();
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing coordinates in the TSV file. Check your data format.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Bootstraps the game by picking a target, requesting user mode, and utilizing Polymorphism
     * to start the correct GameSession instance.
     */
    private void setupAndStart() {
        if (allCountries.isEmpty()) {
            System.out.println("No countries loaded. Exiting game.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Country targetCountry = allCountries.get(random.nextInt(allCountries.size()));

        System.out.println("\nWelcome to the Country Guessing Game!");
        System.out.println("1. Solo Player");
        System.out.println("2. Two Players");
        System.out.print("Select mode (1 or 2): ");
        
        int mode = 1;
        try {
            mode = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, defaulting to Solo Player.");
        }

        GameSession session; // Polymorphic reference

        if (mode == 2) {
            System.out.print("Enter Player 1 name: ");
            String p1 = scanner.nextLine();
            System.out.print("Enter Player 2 name: ");
            String p2 = scanner.nextLine();
            session = new MultiplayerGame(targetCountry, countryMap, p1, p2);
        } else {
            System.out.print("Enter Player name: ");
            String p1 = scanner.nextLine();
            session = new SoloGame(targetCountry, countryMap, p1);
        }

        // The JVM dynamically binds to either SoloGame.play() or MultiplayerGame.play()
        session.play(); 
        scanner.close();
    }
}