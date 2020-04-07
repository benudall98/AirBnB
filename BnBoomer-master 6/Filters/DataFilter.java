package Filters;

import initial.AirbnbListing;

import java.util.ArrayList;

/**
 * An abstract class for data filtering
 *
 * @author BASE
 */
abstract class DataFilter {
    private ArrayList<AirbnbListing> filteredListings;

    //constructor
    public DataFilter() {
    }

    public void filterData(ArrayList<AirbnbListing> listingsToFilter) {
        //
    }

    abstract ArrayList<AirbnbListing> getFilteredListings();
}