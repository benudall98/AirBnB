package PropertyView;

import MainApplication.ApplicationController;
import initial.AirbnbListing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Controller Class for PropertyView Window.
 *
 * @author BASE
 */
public class PropertyViewController {
    @FXML
    private TextField nameLabel;

    @FXML
    private TextField hostLabel;

    @FXML
    private TextField availabilityLabel;

    @FXML
    private TextField roomTypeLabel;

    @FXML
    private TextField lastReviewLabel;

    @FXML
    private AnchorPane dynamicMap;

    @FXML
    private ImageView favouriteIcon;

    private Image unfavouriteImage;

    private Image favouriteImage;

    private WebView mapView = new WebView();

    private ApplicationController ParentController;

    private ArrayList<AirbnbListing> propertiesToShow;

    private AirbnbListing currentProperty;

    /**
     * Initialise PropertyView
     */
    @FXML
    void initialize() {
        assert nameLabel != null : "fx:id=\"nameLabel\" was not injected: check your FXML file 'PropertyView.fxml'.";
        assert hostLabel != null : "fx:id=\"hostLabel\" was not injected: check your FXML file 'PropertyView.fxml'.";
        assert availabilityLabel != null : "fx:id=\"availabilityLabel\" was not injected: check your FXML file 'PropertyView.fxml'.";
        assert roomTypeLabel != null : "fx:id=\"roomTypeLabel\" was not injected: check your FXML file 'PropertyView.fxml'.";
        assert lastReviewLabel != null : "fx:id=\"lastReviewLabel\" was not injected: check your FXML file 'PropertyView.fxml'.";

        dynamicMap.getChildren().add(mapView);
    }

    /**
     * Sets list of properties that propertyView should know so it can display
     *
     * @param listings properties to show
     */
    public void setListingsToShow(ArrayList<AirbnbListing> listings) {
        propertiesToShow = listings;
    }

    /**
     * Set parent controller of PropertyView
     * Loads images which will need to be used on the favourite icon to local variable
     *
     * @param parentController the parent controller which is Application Controller
     */
    public void setParentController(ApplicationController parentController) {
        ParentController = parentController;
        unfavouriteImage = ParentController.getBoroughViewerController().getUnfavouriteImage();
        favouriteImage = ParentController.getBoroughViewerController().getFavouriteImage();
    }

    /**
     * Displays the selected property and opens the map to show the location of the property.
     * Updates the favouriteIcon
     * Updates label text
     *
     * @param listing the airbnb data to display
     */
    public void showProperty(AirbnbListing listing) {
        if (listing != null) {
            currentProperty = listing;
            updateFavouriteIcon();
            openMap(currentProperty);
            nameLabel.setText(listing.getName());
            hostLabel.setText(listing.getHost_name());
            availabilityLabel.setText("" + listing.getAvailability365());
            roomTypeLabel.setText(listing.getRoom_type());
            lastReviewLabel.setText(listing.getLastReview());
        }
    }

    /**
     * Updates the current property to next one
     * If the current one was the last one in the list, display the first property in the list (makes it cyclical)
     *
     * @return currentProperty (the property to display)
     */
    public AirbnbListing getNextProperty() {
        if (propertiesToShow.size() != 0) {
            if (propertiesToShow.indexOf(currentProperty) == propertiesToShow.size() - 1) {
                currentProperty = propertiesToShow.get(0);
            } else {
                currentProperty = propertiesToShow.get(propertiesToShow.indexOf(currentProperty) + 1);
            }
        }
        return currentProperty;
    }

    /**
     * Updates the current property to previous one
     * If the current one was the first one in the list, automatically display the last property in the list (makes it cyclical)
     *
     * @return currentProperty (the property to display)
     */
    public AirbnbListing getPreviousProperty() {
        if (propertiesToShow.size() != 0) {
            if (propertiesToShow.indexOf(currentProperty) == 0) {
                currentProperty = propertiesToShow.get(propertiesToShow.size() - 1);
            } else {
                currentProperty = propertiesToShow.get(propertiesToShow.indexOf(currentProperty) - 1);
            }
        }
        return currentProperty;
    }

    /**
     * Reacts to next button
     * When clicked, the next property is shown
     *
     * @param actionEvent when the user clicks > button
     */
    public void nextProperty(ActionEvent actionEvent) {
        showProperty(getNextProperty());
    }

    /**
     * Reacts to previous button
     * When clicked, the previous property is shown
     *
     * @param actionEvent when the user clicks < button
     */
    public void previousProperty(ActionEvent actionEvent) {
        showProperty(getPreviousProperty());
    }

    /**
     * Using google map, the location of property is displayed in the WebView
     *
     * @param listing the airbnb data
     */
    private void openMap(AirbnbListing listing) {
        String urlString = "http://maps.google.com/maps?q=" + listing.getLatitude() + "," + listing.getLongitude();
        mapView.getEngine().load(urlString);
    }

    /**
     * Puts the selected item in favourite table
     *
     * @param actionEvent when the user clicks the favourite button
     */
    public void favouriteProperty(ActionEvent actionEvent) throws URISyntaxException {
        ParentController.getBoroughViewerController().addFavourite(currentProperty);
        updateFavouriteIcon();
    }

    /**
     * Update the icon on favourite button depending on whether the property exists in the favourite table
     * If the property is not in the table, the icon shows '+'
     * Otherwise the icon shows '-'
     */
    private void updateFavouriteIcon() {
        if (ParentController.getBoroughViewerController().getFavouritedListings().contains(currentProperty)) {
            favouriteIcon.setImage(favouriteImage);
        } else {
            favouriteIcon.setImage(unfavouriteImage);
        }
    }
}