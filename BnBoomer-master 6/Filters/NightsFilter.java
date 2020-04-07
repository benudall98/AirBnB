package Filters;

import initial.AirbnbListing;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;

/*
 * NightsFilter filters out listings that are not available for rent for the user's minimum rent period.
 * @author BASE
 */
public class NightsFilter extends DataFilter {
    private ArrayList<AirbnbListing> filteredListings;

    //constructor
    public NightsFilter() {
    }

    /**
     * Based on the check-in and check-out dates user selected, the property data are filtered so that minimum number of
     * nights available shorter than desired rent period are excluded and not displayed on boroughViewer
     *
     * @param listingsToFilter ArrayList of listings
     * @param checkInDate      Temporal
     * @param checkOutDate     Temporal
     */
    public void filterData(ArrayList<AirbnbListing> listingsToFilter, Temporal checkInDate, Temporal checkOutDate) {
        filteredListings = new ArrayList<AirbnbListing>();
        int nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        for (AirbnbListing listing : listingsToFilter) {
            if (nights > listing.getMinimumNights()) {
                filteredListings.add(listing);
            }
        }
    }

    /**
     * Returns ArrayList of listings filtered
     *
     * @return filteredListings
     */
    public ArrayList<AirbnbListing> getFilteredListings() {
        return filteredListings;
    }
}