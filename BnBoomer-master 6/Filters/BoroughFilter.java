package Filters;

import initial.AirbnbListing;

import java.util.ArrayList;

/*
 * BoroughFilter filters listings according to each borough.
 * @author BASE
 */
public class BoroughFilter extends DataFilter {
    private ArrayList<AirbnbListing> filteredListings;

    public BoroughFilter() {
    }

    /**
     * Filters listings to find those specific to each borough into a new ArrayList
     *
     * @param listingsToFilter Feed ArrayList of listings
     * @param boroughName      specify name of borough
     */
    public void filterData(ArrayList<AirbnbListing> listingsToFilter, String boroughName) {
        filteredListings = new ArrayList<AirbnbListing>();
        for (AirbnbListing listing : listingsToFilter) {
            if (listing.getNeighbourhood().equals(boroughName)) {
                filteredListings.add(listing);
            }
        }
    }

    /**
     * Returns ArrayList of filtered listings
     *
     * @return filteredListings
     */
    public ArrayList<AirbnbListing> getFilteredListings() {
        return filteredListings;
    }
}