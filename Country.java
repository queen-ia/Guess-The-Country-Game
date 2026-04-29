package mycountrygame;

/**
 * Represents a playable country with geographical coordinates and a hint.
 */
public class Country {
    private String name;
    private double latitude;
    private double longitude;
    private String hint;

    /**
     * Constructs a new Country.
     *
     * @param name      The name of the country.
     * @param latitude  The latitudinal coordinate.
     * @param longitude The longitudinal coordinate.
     * @param hint      A fun fact or hint about the country.
     */
    public Country(String name, double latitude, double longitude, String hint) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hint= hint;
    }

    public String getName() { 
        return name; 
    }
    
    public String getHint() { 
        return hint; 
    }
    
    /**
     * Calculates the distance between this country and another using the Haversine formula.
     *
     * @param other The target Country to measure distance to.
     * @return The distance in miles between the two countries.
     */
    public double calculateDistanceInMiles(Country other) {
        // Radius of the earth in miles
        final int R = 3958; 
        double latDistance = Math.toRadians(other.latitude - this.latitude);
        double lonDistance = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}