package StatisticsWindow;

import initial.AirbnbListing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Statistics {
    private ArrayList<AirbnbListing> data;

    private float averagePrice;
    private int numberOfProperties;
    private float averageReviewPerProperty;
    private int numberOfHome;
    private int numberOfShared;
    private int numberOfPrivate;

    private String expensivePropertyName;
    private int mostExpensivePropertyPrice;

    private String cheapestPropertyName;
    private int cheapestPropertyPrice;

    private String expensiveBoroughName;
    private float expensiveBoroughPrice;

    private String cheapestBoroughName;
    private float cheapestBoroughPrice;

    private int highestPrice;

    private HashMap<Float, String> avgBoroughPrice;

    private ArrayList<String> boroughNames = new ArrayList<>();

    /**
     * Loads and organise data and updates all information that must be displayed
     *
     * @param data Arraylist of all listings
     */
    public Statistics(ArrayList<AirbnbListing> data) {
        this.data = data;
        setBoroughNames();
        avgBoroughPrice = new HashMap<>();
        updateAllStatistics();
    }

    /**
     * Add all borough names into arrayList
     */
    private void setBoroughNames() {
        boroughNames.add("Kingston upon Thames");
        boroughNames.add("Croydon");
        boroughNames.add("Bromley");
        boroughNames.add("Hounslow");
        boroughNames.add("Ealing");
        boroughNames.add("Hillingdon");
        boroughNames.add("Harrow");
        boroughNames.add("Brent");
        boroughNames.add("Barnet");
        boroughNames.add("Enfield");
        boroughNames.add("Waltham Forest");
        boroughNames.add("Redbridge");
        boroughNames.add("Sutton");
        boroughNames.add("Lambeth");
        boroughNames.add("Southwark");
        boroughNames.add("Lewisham");
        boroughNames.add("Greenwich");
        boroughNames.add("Bexley");
        boroughNames.add("Richmond upon Thames");
        boroughNames.add("Merton");
        boroughNames.add("Wandsworth");
        boroughNames.add("Hammersmith and Fulham");
        boroughNames.add("Kensington and Chelsea");
        boroughNames.add("Westminster");
        boroughNames.add("Camden");
        boroughNames.add("Tower Hamlets");
        boroughNames.add("Islington");
        boroughNames.add("Hackney");
        boroughNames.add("Haringey");
        boroughNames.add("Newham");
        boroughNames.add("Barking and Dagenham");
        boroughNames.add("Havering");
        boroughNames.add("City of London");
    }

    /**
     * Update all the statistics about properties on window
     */
    public void updateAllStatistics() {
        updateBoroughPrices();
        updateAveragePrice();
        updateTotalAvailableProperties();
        updateAvgReviewsPerProperty();
        updateNumberOfHomes();
        updateNumberOfPrivateRooms();
        updateNumberOfSharedRooms();
        updateMostExpensiveProperty();
        updateCheapestProperty();
        updateHighestPrice();
        updateMostExpensiveBorough();
        updateCheapestBorough();
    }

    /**
     * Returns the average of values in column
     *
     * @return float AverageValue
     */
    private float getColumnAverage(ArrayList<Integer> column) {
        float total = 0;
        for (int value : column) {
            total += value;
        }
        return total / column.size();
    }

    /**
     * Update hashMap avgBoroughPrice which holds average price of properties of each borough
     */
    private void updateBoroughPrices() {
        for (String borough : boroughNames) {
            float boroughPrice = 0;
            int listingsInBorough = 0;
            for (AirbnbListing listing : data) {
                if (listing.getNeighbourhood().equals(borough)) {
                    boroughPrice += listing.getPrice() * listing.getMinimumNights();
                    listingsInBorough++;
                }
            }
            float boroughAvgPrice = boroughPrice / listingsInBorough;
            avgBoroughPrice.put(boroughAvgPrice, borough);
        }
    }

    /**
     * Updates average price of properties statistic
     */
    private void updateAveragePrice() {
        ArrayList columnValues = new ArrayList<Integer>();
        for (AirbnbListing listing : data) {
            columnValues.add(listing.getPrice());
        }
        averagePrice = getColumnAverage(columnValues);
    }

    /**
     * Updates the number of total available properties statistic
     */
    private void updateTotalAvailableProperties() {
        numberOfProperties = data.size();
    }

    /**
     * Updates average number of reviews statistic
     */
    private void updateAvgReviewsPerProperty() {
        ArrayList columnValues = new ArrayList<Integer>();
        for (AirbnbListing listing : data) {
            columnValues.add(listing.getNumberOfReviews());
        }
        averageReviewPerProperty = getColumnAverage(columnValues);
        ;
    }

    /**
     * Updates number of home type properties statistic
     */
    private void updateNumberOfHomes() {
        int i = 0;
        for (AirbnbListing listing : data) {
            if (listing.getRoom_type().equals("Entire home/apt")) {
                i++;
            }
        }
        numberOfHome = i;
    }

    /**
     * Updates number of shared room type properties statistic
     */
    private void updateNumberOfSharedRooms() {
        int i = 0;
        for (AirbnbListing listing : data) {
            if (listing.getRoom_type().equals("Shared room")) {
                i++;
            }
        }
        numberOfShared = i;
    }

    /**
     * Updates number of private room type statistics statistic
     */
    private void updateNumberOfPrivateRooms() {
        int i = 0;
        for (AirbnbListing listing : data) {
            if (listing.getRoom_type().equals("Private room")) {
                i++;
            }
        }
        numberOfPrivate = i;
    }

    /**
     * Updates highest property price (in a single night) statistic
     */
    private void updateHighestPrice() {
        int price = 0;
        for (AirbnbListing listing : data) {
            if (listing.getPrice() > price) {
                price = listing.getPrice();
            }
        }
        highestPrice = price;
    }

    /**
     * Updates most expensive property (depends on the number of nights the user wants to stay) statistic
     */
    private void updateMostExpensiveProperty() {
        String propertyName = "";
        int maxPrice = 0;
        for (AirbnbListing listing : data) {
            if (listing.getPrice() * listing.getMinimumNights() > maxPrice) {
                maxPrice = listing.getPrice() * listing.getMinimumNights();
                propertyName = listing.getName();
            }
        }
        mostExpensivePropertyPrice = maxPrice;
        expensivePropertyName = propertyName;
    }

    /**
     * Updates cheapest property price (depends on the number of nights the user wants to stay) statistic
     */
    private void updateCheapestProperty() {
        String propertyName = "";
        int minPrice = mostExpensivePropertyPrice;
        for (AirbnbListing listing : data) {
            if (listing.getPrice() * listing.getMinimumNights() < minPrice) {
                minPrice = listing.getPrice() * listing.getMinimumNights();
                propertyName = listing.getName();
            }
        }
        cheapestPropertyPrice = minPrice;
        cheapestPropertyName = propertyName;
    }

    /**
     * Updates most expensive borough statistic
     */
    private void updateMostExpensiveBorough() {
        expensiveBoroughPrice = Collections.max(avgBoroughPrice.keySet());
        expensiveBoroughName = avgBoroughPrice.get(expensiveBoroughPrice);
    }

    /**
     * Updates cheapest borough statistic
     */
    private void updateCheapestBorough() {
        cheapestBoroughPrice = Collections.min(avgBoroughPrice.keySet());
        cheapestBoroughName = avgBoroughPrice.get(expensiveBoroughPrice);
    }

    /**
     * Accessor method that gets average price of all listings
     *
     * @return float averagePrice
     */
    public float getAveragePrice() {
        return averagePrice;
    }

    /**
     * Accessor method that returns total number of properties
     *
     * @return int numberOfProperties
     */
    public int getNumberOfProperties() {
        return numberOfProperties;
    }

    /**
     * Accessor method that returns average number of reviews among properties
     *
     * @return float averageReviewPerProperty
     */
    public float getAverageReviewPerProperty() {
        return averageReviewPerProperty;
    }

    /**
     * Returns number of home type properties
     *
     * @return int numberOfHome
     */
    public int getNumberOfHome() {
        return numberOfHome;
    }

    /**
     * Returns number of shared type properties
     *
     * @return int numberOfShared
     */
    public int getNumberOfShared() {
        return numberOfShared;
    }

    /**
     * Returns number of private type properties
     *
     * @return int numberOfPrivate
     */
    public int getNumberOfPrivate() {
        return numberOfPrivate;
    }

    /**
     * Returns the name of most expensive property
     *
     * @return String expensivePropertyName
     */
    public String getExpensivePropertyName() {
        return expensivePropertyName;
    }

    /**
     * Returns the name of cheapest property
     *
     * @return String cheapestPropertyName
     */
    public String getCheapestPropertyName() {
        return cheapestPropertyName;
    }

    /**
     * Returns the price of the most expensive property (depends on the number of nights the user wants to stay)
     *
     * @return int mostExpensivePropertyPrice
     */
    public int getMostExpensivePropertyPrice() {
        return mostExpensivePropertyPrice;
    }

    /**
     * Returns name of borough with highest average price of listings
     *
     * @return String expensiveBoroughName
     */
    public String getExpensiveBoroughName() {
        return expensiveBoroughName;
    }

    /**
     * Returns highest average price of listings borough-wise
     *
     * @return float expensiveBoroughPrice
     */
    public float getExpensiveBoroughPrice() {
        return expensiveBoroughPrice;
    }

    /**
     * Returns name of borough with lowest average price of listings
     *
     * @return String cheapestBoroughName
     */
    public String getCheapestBoroughName() {
        return cheapestBoroughName;
    }

    /**
     * Returns average listing price in cheapest borough
     *
     * @return float cheapestBoroughPrice
     */
    public float getCheapestBoroughPrice() {
        return cheapestBoroughPrice;
    }

    /**
     * Returns highest property price (per night)
     *
     * @return int highestPrice
     */
    public int getHighestPrice() {
        return highestPrice;
    }

    /**
     * Returns cheapest property price
     *
     * @return int cheapestPropertyPrice
     */
    public int getCheapestPropertyPrice() {
        return cheapestPropertyPrice;
    }

    /**
     * returns a hashmap which holds average price of each borough
     *
     * @return HashMap<Float, String> avgBoroughPrice
     */
    public HashMap<Float, String> getAvgBoroughPrice() {
        return avgBoroughPrice;
    }

    /**
     * returns the listing with highest number of reviews
     *
     * @return String mostReviewed
     */
    public String getMostReviewed() {
        int noOfReviews = -1;
        String mostReviewed = null;
        for (AirbnbListing listing : data) {
            if (noOfReviews == -1 || listing.getNumberOfReviews() > noOfReviews) {
                noOfReviews = listing.getNumberOfReviews();
                mostReviewed = listing.getName();
            }
        }
        return mostReviewed;
    }

    /**
     * Returns name of most popular host.
     *
     * @return String hostName
     */
    public String getPopularHost() {
        int hostListings = -1;
        String hostName = null;
        for (AirbnbListing listing : data) {
            if (hostListings == -1 || listing.getCalculatedHostListingsCount() > hostListings) {
                hostListings = listing.getCalculatedHostListingsCount();
                hostName = listing.getHost_name();
            }
        }
        return hostName;
    }
}
