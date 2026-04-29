package mycountrygame;

/**
 * A simple record-style class used to link a country's name to its calculated distance.
 * Used exclusively for sorting the leaderboard.
 */
public class DistanceRecord {
    String countryname;
    double distance;

    public DistanceRecord(String countryname, double distance) {
        this.countryname = countryname;
        this.distance = distance;
    }
}
