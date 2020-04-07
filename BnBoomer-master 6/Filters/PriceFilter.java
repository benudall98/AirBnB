package Filters;

import initial.AirbnbListing;

import java.util.ArrayList;

/*
 * PriceFilter filters listings to include those within a specific price range.
 * @author BASE
 */
public class PriceFilter extends DataFilter {
    private ArrayList<AirbnbListing> filteredListings;

    public PriceFilter() {
    }

    /**
     * Based on the prices user selected, the Airbnb property data are filtered so that prices that only fall within
     * the range can be displayed
     *
     * @param listingsToFilter Arraylist
     * @param minPrice         int
     * @param maxPrice         int
     */
    public void filterData(ArrayList<AirbnbListing> listingsToFilter, int minPrice, int maxPrice) {
        filteredListings = new ArrayList<AirbnbListing>();
        for (AirbnbListing listing : listingsToFilter) {
            if (listing.getPrice() <= maxPrice && listing.getPrice() >= minPrice) {
                filteredListings.add(listing);
            }
        }
    }

    /**
     * Returns ArrayList of filtered listings.
     *
     * @return filteredListings
     */
    public ArrayList<AirbnbListing> getFilteredListings() {
        return filteredListings;
    }
}