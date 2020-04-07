package Map;

import MainApplication.ApplicationController;
import initial.AirbnbListing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Class for BoroughViewer Window.
 *
 * @author BASE
 */
public class BoroughViewerController {
    @FXML
    private Button favouriteButton;

    @FXML
    private TableView<AirbnbListing> boroughPropertiesTable;

    @FXML
    private TableColumn<AirbnbListing, String> name;

    @FXML
    private TableColumn<AirbnbListing, String> hostName;

    @FXML
    private TableColumn<AirbnbListing, Integer> numberOfReviews;

    @FXML
    private TableColumn<AirbnbListing, Integer> price;

    @FXML
    private TableColumn<AirbnbListing, Integer> minimumNumberOfNights;

    @FXML
    private TableColumn<AirbnbListing, String> room_type;

    @FXML
    private Label boroughText;

    @FXML
    private ImageView favouriteIcon;

    private Image unfavouriteImage;

    private Image favouriteImage;

    private ArrayList<AirbnbListing> favouritedListings = new ArrayList<>(); //listings that are favourited by the user

    private List<String> allBoroughs;

    private ApplicationController ParentController;

    private String currentBorough; // Current borough as an abbreviation

    private ArrayList<AirbnbListing> listings; // All the listings to be shown in this table

    /**
     * Sets up the BoroughViewer by creating tableview.
     * Stores favourite image icon.
     *
     * @author BASE & James_D from Stackoverflow
     */
    @FXML
    void initialize() throws URISyntaxException {
        boroughPropertiesTable.setPlaceholder(new Label("No properties matching your preferences here"));

        // Setting up table columns
        name.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("name"));
        hostName.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("host_name"));
        numberOfReviews.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("numberOfReviews"));
        price.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("price"));
        minimumNumberOfNights.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("minimumNights"));
        room_type.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("room_type"));

        // Setting up images which will be used on button
        unfavouriteImage = new Image(getClass().getResource("img/unheart.png").toURI().toString());
        favouriteImage = new Image(getClass().getResource("img/heart.png").toURI().toString());

        assert favouriteButton != null : "fx:id=\"favouriteButton\" was not injected: check your FXML file 'BoroughViewer.fxml'.";

        //https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
        //on double clicking rows in the table
        boroughPropertiesTable.setRowFactory(tv -> {
            TableRow<AirbnbListing> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    updateFavouriteIcon();
                }
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    AirbnbListing rowData = row.getItem();
                    ParentController.openPropertyViewer(rowData, listings);
                }
            });
            return row;
        });
    }

    /**
     * Returns list of properties which are user's favourites
     *
     * @return ArrayList<AirbnbListing> favouritedListings
     */
    public ArrayList<AirbnbListing> getFavouritedListings() {
        return favouritedListings;
    }

    /**
     * Returns the next property of the current one in the list
     *
     * @return String next property in the current borough
     */
    public String getNextBorough() {
        if (allBoroughs.indexOf(currentBorough) == allBoroughs.size() - 1) {
            currentBorough = allBoroughs.get(0);
        } else {
            currentBorough = allBoroughs.get(allBoroughs.indexOf(currentBorough) + 1);
        }
        return currentBorough;
    }

    /**
     * Returns the previous property of the current one in the list
     *
     * @return String previous property in current borough
     */
    public String getPreviousBorough() {
        if (allBoroughs.indexOf(currentBorough) == 0) {
            currentBorough = allBoroughs.get(allBoroughs.size() - 1);
        } else {
            currentBorough = allBoroughs.get(allBoroughs.indexOf(currentBorough) - 1);
        }
        return currentBorough;
    }

    /**
     * Sets the text on boroughViewer to inform users which borough they are looking at
     *
     * @param fullBoroughName String
     */
    private void setBoroughText(String fullBoroughName) {
        boroughText.setText("Properties available in " + fullBoroughName);
    }

    /**
     * Populates the table based on the borough
     *
     * @param boroughAbbreviation String
     */
    public void populateTable(String boroughAbbreviation) {
        currentBorough = boroughAbbreviation;
        String fullBoroughName = ParentController.getBoroughAbbreviations().get(boroughAbbreviation);
        ParentController.getBoroughViewerStage().setTitle(fullBoroughName);
        setBoroughText(fullBoroughName);

        String boroughName = ParentController.getBoroughAbbreviations().get(currentBorough);
        ParentController.getBoroughFilter().filterData(ParentController.getInitialFilteredListings(), boroughName);
        listings = new ArrayList<>(ParentController.getBoroughFilter().getFilteredListings());

        boroughPropertiesTable.getItems().clear();

        for (AirbnbListing listing : listings) {
            boroughPropertiesTable.getItems().add(listing);
        }
    }

    /**
     * Set all boroughs which need to be displayed
     *
     * @param allBoroughs List
     */
    public void setAllBoroughs(List<String> allBoroughs) {
        this.allBoroughs = allBoroughs;
    }

    /**
     * Sets the parent controller of BoroughViewerController
     *
     * @param parentController ApplicationController
     */
    public void setParentController(ApplicationController parentController) {
        ParentController = parentController;
    }

    /**
     * When < button is clicked, the table shows list of properties of previous borough (from the current one)
     *
     * @param actionEvent ActionEvent
     */
    public void previousProperty(ActionEvent actionEvent) {
        populateTable(getPreviousBorough());
    }

    /**
     * When > button is clicked, the table shows list of properties of next borough (from the current one)
     *
     * @param actionEvent ActionEvent
     */
    public void nextProperty(ActionEvent actionEvent) {
        populateTable(getNextBorough());
    }

    /**
     * Adds the selected property into favourite table and updates the icon on favourite button
     *
     * @param actionEvent Action Event
     */
    public void favouriteProperty(ActionEvent actionEvent) {
        AirbnbListing listing = boroughPropertiesTable.getSelectionModel().getSelectedItem();
        if (listing != null) {
            addFavourite(listing);
            updateFavouriteIcon();
        }
    }

    /**
     * Add the property into favourite table
     * If the table already has the property, it removes from the table
     * Otherwise the property is added in the table
     *
     * @param listing listing to be favourited
     */
    public void addFavourite(AirbnbListing listing) {
        if (favouritedListings.contains(listing)) {
            favouritedListings.remove(listing);
        } else {
            favouritedListings.add(listing);
        }
        ParentController.refreshFavourites();
    }

    /**
     * Updates the image on the favourite button
     * If the selected item is already in the table, the icon shows '-' sign
     * Otherwise the icon shows '+' sign
     */
    private void updateFavouriteIcon() {
        AirbnbListing listing = boroughPropertiesTable.getSelectionModel().getSelectedItem();
        if (favouritedListings.contains(listing)) {
            favouriteIcon.setImage(favouriteImage);
        } else {
            favouriteIcon.setImage(unfavouriteImage);
        }
    }

    /**
     * Returns the image which has '-' sign
     *
     * @return Image unfavouriteImage
     */
    public Image getUnfavouriteImage() {
        return unfavouriteImage;
    }

    /**
     * Returns the image which has '+' sign
     *
     * @return Image favouriteImage
     */
    public Image getFavouriteImage() {
        return favouriteImage;
    }
}