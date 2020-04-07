package MainApplication;

import Filters.BoroughFilter;
import Filters.NightsFilter;
import Filters.PriceFilter;
import Map.BoroughViewerController;
import PropertyView.PropertyViewController;
import StatisticsWindow.Statistics;
import StatisticsWindow.StatisticsController;
import initial.AirbnbDataLoader;
import initial.AirbnbListing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller Class for Main Window.
 *
 * @author BASE
 */

public class ApplicationController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private TabPane mainTabPane;

    @FXML // fx:id="welcomeTabButton"
    private Tab welcomeTab, mapTab, accountTab; // Value injected by FXMLLoader

    @FXML
    private GridPane mapGrid;

    @FXML
    private TableView favouriteTable;

    @FXML
    private TableColumn<AirbnbListing, String> favouriteName;

    @FXML
    private TableColumn<AirbnbListing, String> favouriteHostName;

    @FXML
    private TableColumn<AirbnbListing, Integer> favouritePrice;

    @FXML
    private TableColumn<AirbnbListing, String> favouriteBorough;

    @FXML
    private TableColumn favouriteRoomType;

    @FXML
    private TableColumn favouriteMinimumNumberOfNights;

    @FXML
    private TableColumn favouriteNumberOfReviews;

    @FXML
    private TextField longCoord;

    @FXML
    private TextField latCoord;

    @FXML
    private Label closestPropertyName;

    @FXML
    private TextField cheapestProperty;

    @FXML
    private TextField expensiveProperty;

    @FXML
    private TextField mostReviews;

    @FXML
    private TextField averageTotalReviews;

    @FXML
    private TextField propertyWithPopularHost;

    @FXML
    private Slider minSlider, maxSlider; // Sliders used for prices

    @FXML
    private Label minPriceLabel, maxPriceLabel; // Labels to show the price selected from Sliders

    @FXML
    private DatePicker checkInDate, checkOutDate; // DataPickers for user selecting dates

    @FXML
    private Button statsButton; // Opens statistic window when clicked

    private Image icon = new Image(getClass().getResourceAsStream("img/icon.png"));

    private HashMap<String, String> boroughAbbreviation = new HashMap<>(); // HashMap for Borough names and their corresponding buttons
    private HashMap<String, String> reverseBoroughAbbreviation = new HashMap<>();

    private PriceFilter priceFilter = new PriceFilter();
    private BoroughFilter boroughFilter = new BoroughFilter();
    private NightsFilter nightsFilter = new NightsFilter();

    private Stage boroughViewerStage;

    private BoroughViewerController boroughViewerController;

    private Statistics statistics;

    private Stage statisticsStage;
    private StatisticsController statisticsController;

    private Stage propertyViewStage;
    private PropertyViewController propertyViewController;

    private ArrayList<AirbnbListing> allListings = new AirbnbDataLoader().load();
    private ArrayList<AirbnbListing> initialFilteredListings = new ArrayList<>();

    private Alert alertWelcomeGo;
    private Alert alertRefreshComparison;
    private Alert alertCoordinates;

    /**
     * This method is called by the FXMLLoader when initialisation is complete.
     */
    @FXML
    void initialize() {
        // Sets up the tabs
        setUpTabs();

        // Creates alert for the welcomeTab
        createWelcomeGoAlerts();

        // Creates alert for the comparison in the user account
        createRefreshComparisonAlerts();

        // Creates alert for the coordinates in the account tab
        createCoordinateAlert();

        // Setting up the favourites table
        setUpFavouritesTable();

        // Create a HashMap between the borough names and their abbreviations
        updateBoroughAbbreviation();

        // Create Borough Viewer
        createBoroughViewer();

        // Create Statistics Viewer
        createStatisticsViewer();

        // Create Property Viewer
        createPropertyViewer();
    }

    /**
     * There are 3 tabs in program -
     * welcome tab for user inserting prices and check-in/check-out dates
     * map tab for displaying boroughs and number of properties available in each borough
     * account tab for displaying user's saved properties
     */
    private void setUpTabs() {
        // Setting up the tab buttons
        assert welcomeTab != null : "fx:id=\"welcomeTabButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert mapTab != null : "fx:id=\"mapTabButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert accountTab != null : "fx:id=\"accountTabButton\" was not injected: check your FXML file 'MainWindow.fxml'.";

        mapTab.setDisable(true);
        accountTab.setDisable(true);
        statsButton.setDisable(true);

        // Setting up the sliders and datapickers on the welcomeTab
        initialiseSliders();
    }

    /**
     * Set up price sliders dynamically using data from the API.
     */
    public void initialiseSliders() {
        int highestPrice = getUpperPriceLimit();
        minSlider.setMin(0);
        minSlider.setMax(highestPrice * 0.75);
        minSlider.setMajorTickUnit(highestPrice * 0.1);

        maxSlider.setMin(50);
        maxSlider.setMax(highestPrice);
        maxSlider.setMajorTickUnit(highestPrice * 0.1);

        minPriceLabel.setText("£ " + minSlider.getValue());
        maxPriceLabel.setText("£ " + maxSlider.getValue());
    }

    /**
     * Creates alert for the welcomeTab to be displayed when invalid dates or prices entered by user.
     */
    private void createWelcomeGoAlerts() {
        alertWelcomeGo = new Alert(Alert.AlertType.ERROR);
        alertWelcomeGo.setTitle("Error Dialog");
        alertWelcomeGo.setHeaderText("You entered invalid dates or prices!");
        alertWelcomeGo.setContentText("Close this window and re-enter valid dates and price ranges");
    }

    /**
     * Creates alert for refreshing comparisons if favouritedListings is null.
     */
    private void createRefreshComparisonAlerts() {
        alertRefreshComparison = new Alert(Alert.AlertType.ERROR);
        alertRefreshComparison.setTitle("Error Dialog");
        alertRefreshComparison.setHeaderText("There is no properties in your favourite table");
    }

    /**
     * Creates alert for the coordinates input
     */
    private void createCoordinateAlert() {
        alertCoordinates = new Alert(Alert.AlertType.ERROR);
        alertCoordinates.setTitle("Error Dialog");
        alertCoordinates.setHeaderText("Enter valid coordinates and have some properties favourited. Must be decimal, not a string.");
    }

    /**
     * Sets up favourites tableview in account tab
     * Allows to open propertyViewer if user double clicks on selected item
     *
     * @author BASE & James_D from Stackoverflow
     */
    private void setUpFavouritesTable() {
        // Setting up the favourites table
        favouriteTable.setPlaceholder(new Label("You have not favourited any properties"));
        favouriteName.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("name"));
        favouriteBorough.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));
        favouriteHostName.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("host_name"));
        favouritePrice.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("price"));
        favouriteNumberOfReviews.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("numberOfReviews"));
        favouriteMinimumNumberOfNights.setCellValueFactory(new PropertyValueFactory<AirbnbListing, Integer>("minimumNights"));
        favouriteRoomType.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("room_type"));

        //https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
        //on double clicking rows in the table
        favouriteTable.setRowFactory(tv -> {
            TableRow<AirbnbListing> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    AirbnbListing rowData = row.getItem();
                    openPropertyViewer(rowData, boroughViewerController.getFavouritedListings());
                }
            });
            return row;
        });
    }

    /**
     * Creates BoroughViewer which is a window showing list of properties available in each borough using FXML resource.
     */
    private void createBoroughViewer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Map/BoroughViewer.fxml"));
            Parent root = loader.load();

            boroughViewerController = loader.getController();

            boroughViewerController.setAllBoroughs(new ArrayList<String>(boroughAbbreviation.keySet()));

            boroughViewerController.setParentController(this);

            boroughViewerStage = new Stage();
            boroughViewerStage.setScene(new Scene(root));
            boroughViewerStage.setResizable(false);
            boroughViewerStage.getIcons().add(icon);
            boroughViewerStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates StatisticsViewer which is a window showing statistics of properties
     * e.g. the most expensive/cheapest borough, average price ... etc.
     */
    private void createStatisticsViewer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../StatisticsWindow/StatisticsWindow.fxml"));
            Parent root = loader.load();

            statisticsController = loader.getController();

            statisticsController.setParentController(this);

            statisticsStage = new Stage();
            statisticsStage.setScene(new Scene(root));
            statisticsStage.setTitle("Statistics");
            statisticsStage.getIcons().add(icon);
            statisticsStage.setResizable(false);
            statisticsStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates PropertyViewer which is a window that comes out when user double clicks a property in the list/table
     */
    private void createPropertyViewer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../PropertyView/PropertyView.fxml"));
            Parent root = loader.load();

            propertyViewController = loader.getController();
            propertyViewController.setParentController(this);

            propertyViewStage = new Stage();
            propertyViewStage.setScene(new Scene(root));
            propertyViewStage.setTitle("Property Viewer");
            propertyViewStage.getIcons().add(icon);
            propertyViewStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the labels so that they can represent the values from sliders
     */
    @FXML
    public void updateSlider() {
        int minValue = (int) minSlider.getValue();
        int maxValue = (int) maxSlider.getValue();
        minPriceLabel.setText("£ " + minValue);
        maxPriceLabel.setText("£ " + maxValue);
    }

    /**
     * Runs when user clicks the Go button on the welcomeTab
     * The button only does anything if all the tabs could be unlocked
     * Filters the listings based on what things the user selected on the welcomeTab
     * Creates the statistics for the filtered listings
     * Changes the users current tab to the mapTab
     * Colours all the borough buttons based on user selection
     *
     * @param event when user's mouse clicks 'Go' button
     */
    @FXML
    public void welcomeGo(MouseEvent event) {
        if (unlockTabs()) {
            priceFilter.filterData(allListings, (int) minSlider.getValue(), (int) maxSlider.getValue());
            nightsFilter.filterData(priceFilter.getFilteredListings(), checkInDate.getValue(), checkOutDate.getValue());
            initialFilteredListings = nightsFilter.getFilteredListings();

            statistics = new Statistics(initialFilteredListings); // after the initial listings are filtered, stats are created
            statisticsController.setStatistics(statistics);
            statisticsController.setInitialStats();
            statisticsController.updateShownStatistics();

            mainTabPane.getSelectionModel().select(mapTab);

            colourBorough();
        }
    }

    /**
     * Shows the boroughViewer when the borough button on the map tab is clicked
     * Gets the name of the borough from the button and gets the full name from the list
     * Sets the title of the borough to the name of the borough
     * Populates the boroughViewer that is being shown
     *
     * @param mouseEvent when user clicks buttons on map tab
     * @throws Exception exception handling
     */
    @FXML
    public void openBoroughViewer(MouseEvent mouseEvent) throws Exception {
        Button sourceBoroughButton = (Button) mouseEvent.getSource();
        String boroughName = sourceBoroughButton.getText();

        boroughViewerController.populateTable(boroughName);

        boroughViewerStage.show();
    }

    /**
     * Colours the borough buttons depending on the number of properties available for user
     * Disables buttons where there are no properties so that user can't click
     * This is based on the colour scale conditional formatting
     */
    @FXML
    public void colourBorough() {
        HashMap<Button, Integer> propertiesPerBorough = new HashMap<>();

        for (Node boroughNode : mapGrid.getChildren()) { // Loops through all the buttons on the map
            Button boroughButton = (Button) boroughNode; // Cast node to button
            String boroughName = getBoroughAbbreviations().get(boroughButton.getText());
            int count = 0;
            for (AirbnbListing listing : initialFilteredListings) {
                if (listing.getNeighbourhood().equals(boroughName)) {
                    count++;
                }
            }
            propertiesPerBorough.put(boroughButton, count); // add button and numbers of properties at that borough to the hashmap
        }

        HashMap<Integer, Button> reverseHashMap = buttonDisable(propertiesPerBorough);
        colourButtons(reverseHashMap, propertiesPerBorough);
    }

    /**
     * Disable buttons if it has no properties available.
     *
     * @param propertiesPerBorough a hashMap which holds buttons with borough name and the number of properties for corresponding borough
     * @return HashMap reverseHashMap where the key is the number of properties in the borough and the value is the corresponding borough button
     */
    private HashMap<Integer, Button> buttonDisable(HashMap<Button, Integer> propertiesPerBorough) {
        HashMap<Integer, Button> reverseHashMap = new HashMap<>();
        for (Button boroughButton : propertiesPerBorough.keySet()) {
            int propertiesAtBorough = propertiesPerBorough.get(boroughButton);

            if (propertiesAtBorough == 0) {
                boroughButton.setDisable(true);
            } else {
                boroughButton.setDisable(false);
            }

            reverseHashMap.put(propertiesAtBorough, boroughButton);
            boroughButton.setTooltip(new Tooltip("" + propertiesAtBorough + " properties here"));
        }
        return reverseHashMap;
    }

    /**
     * Colour borough buttons if they have any properties available
     * The colour depends on the number of properties available
     *
     * @param reverseHashMap       a hashMap where key is the number of properties and value is the borough button
     * @param propertiesPerBorough a hashMap where key is the borough button and the value is the number of properties in that borough
     */
    private void colourButtons(HashMap<Integer, Button> reverseHashMap, HashMap<Button, Integer> propertiesPerBorough) {
        int minValue = Collections.min(reverseHashMap.keySet());
        int maxValue = Collections.max(reverseHashMap.keySet());

        for (Button boroughButton : propertiesPerBorough.keySet()) {
            double maxBrightness = 0.8;
            double minBrightness = 0.4;

            double brightness = maxBrightness + (minBrightness - maxBrightness) * (propertiesPerBorough.get(boroughButton) - minValue) / (maxValue - minValue);

            java.awt.Color rgbColour = java.awt.Color.getHSBColor(358, (float) 1.00, (float) brightness);

            String hex = String.format("#%02x%02x%02x",
                    rgbColour.getRed(),
                    rgbColour.getGreen(),
                    rgbColour.getBlue());

            boroughButton.setStyle("-fx-background-color: " + hex);
        }
    }

    /**
     * When statButton is clicked, opens the StatisticWindow
     *
     * @param actionEvent when the user clicks statistics button, statistics window is opened
     * @throws Exception
     */
    @FXML
    public void openStatisticsWindow(ActionEvent actionEvent) throws Exception {
        statisticsStage.show();
    }

    /**
     * Runs when a row in table is double clicked
     * Opens the propertyViewer and populates it with the listing that was doubled clicked
     *
     * @param listing  an AirbnbListing
     * @param listings ArrayList of listings to show in PropertyViewer
     */
    public void openPropertyViewer(AirbnbListing listing, ArrayList<AirbnbListing> listings) {
        propertyViewController.setListingsToShow(listings);
        propertyViewController.showProperty(listing);
        propertyViewStage.show();
    }

    /**
     * Tabs that were initially locked are unlocked if the user has input valid data in the welcomeTab
     *
     * @return Returns true if the tabs were unlocked, false otherwise
     */
    public boolean unlockTabs() {
        if (minSlider.getValue() <= maxSlider.getValue() && checkInDate.getValue() != null && checkOutDate.getValue() != null && checkInDate.getValue().isBefore(checkOutDate.getValue())) {
            mapTab.setDisable(false);
            accountTab.setDisable(false);
            statsButton.setDisable(false);
            return true;
        }
        alertWelcomeGo.showAndWait();
        return false;
    }

    /**
     * Returns the price of the most expensive property
     *
     * @return the price of the most expensive property of all the listings as an integer
     */
    private int getUpperPriceLimit() {
        Statistics statsForAllListings = new Statistics(allListings);
        return (int) Math.ceil(statsForAllListings.getHighestPrice());
    }

    /**
     * Populates boroughAbbreviation HashMap with the abbreviation and full name of the boroughs
     */
    public void updateBoroughAbbreviation() {
        boroughAbbreviation.put("KING", "Kingston upon Thames");
        boroughAbbreviation.put("CROY", "Croydon");
        boroughAbbreviation.put("BROM", "Bromley");
        boroughAbbreviation.put("HOUN", "Hounslow");
        boroughAbbreviation.put("EALI", "Ealing");
        boroughAbbreviation.put("HILL", "Hillingdon");
        boroughAbbreviation.put("HRRW", "Harrow");
        boroughAbbreviation.put("BREN", "Brent");
        boroughAbbreviation.put("BARN", "Barnet");
        boroughAbbreviation.put("ENFI", "Enfield");
        boroughAbbreviation.put("WALT", "Waltham Forest");
        boroughAbbreviation.put("REB", "Redbridge");
        boroughAbbreviation.put("SUTT", "Sutton");
        boroughAbbreviation.put("LAMB", "Lambeth");
        boroughAbbreviation.put("STHW", "Southwark");
        boroughAbbreviation.put("LEWS", "Lewisham");
        boroughAbbreviation.put("GWCH", "Greenwich");
        boroughAbbreviation.put("BEXL", "Bexley");
        boroughAbbreviation.put("RICH", "Richmond upon Thames");
        boroughAbbreviation.put("MERT", "Merton");
        boroughAbbreviation.put("WAND", "Wandsworth");
        boroughAbbreviation.put("HAMM", "Hammersmith and Fulham");
        boroughAbbreviation.put("KENS", "Kensington and Chelsea");
        boroughAbbreviation.put("WSTM", "Westminster");
        boroughAbbreviation.put("CAMD", "Camden");
        boroughAbbreviation.put("TOWH", "Tower Hamlets");
        boroughAbbreviation.put("ISLI", "Islington");
        boroughAbbreviation.put("HACK", "Hackney");
        boroughAbbreviation.put("HRGY", "Haringey");
        boroughAbbreviation.put("NEWH", "Newham");
        boroughAbbreviation.put("BARK", "Barking and Dagenham");
        boroughAbbreviation.put("HAVE", "Havering");
        boroughAbbreviation.put("CITY", "City of London");
        updateReverseBoroughAbbreviation();
    }

    /**
     * updates borough abbreviation
     */
    private void updateReverseBoroughAbbreviation() {
        for (String abbreviation : boroughAbbreviation.keySet()) {
            reverseBoroughAbbreviation.put(boroughAbbreviation.get(abbreviation), abbreviation);
        }
    }

    /**
     * @param fullName the full name of the borough
     * @return String borough abbreviation
     */
    public String getBoroughAbbreivation(String fullName) {
        return reverseBoroughAbbreviation.get(fullName);
    }

    /**
     * Clears the favourites table and populates it with the favourited listings
     */
    public void refreshFavourites() {
        favouriteTable.getItems().clear();
        favouriteTable.getItems().addAll(boroughViewerController.getFavouritedListings());
    }

    /**
     * Returns the Price Filter to be used
     *
     * @return priceFilter
     */
    public PriceFilter getPriceFilter() {
        return priceFilter;
    }

    /**
     * Returns the Borough Filter to be used
     *
     * @return boroughFilter
     */
    public BoroughFilter getBoroughFilter() {
        return boroughFilter;
    }

    /**
     * Returns the Nights Filter to be used
     *
     * @return nightsFilter
     */
    public NightsFilter getNightsFilter() {
        return nightsFilter;
    }

    /**
     * Returns the initialFilteredListings which is an ArrayList of listings which match the preferences set in the welcomeTab
     *
     * @return initialFilteredListings an ArrayList of all AirbnbListings
     */
    public ArrayList<AirbnbListing> getInitialFilteredListings() {
        return initialFilteredListings;
    }

    /**
     * Returns the HashMap from the abbreviations to the full borough names
     *
     * @return boroughAbbreviation a HashMap from the abbreviations to the full borough names
     */
    public HashMap<String, String> getBoroughAbbreviations() {
        return boroughAbbreviation;
    }

    /**
     * Returns the boroughViewerStage
     *
     * @return boroughViewerStage
     */
    public Stage getBoroughViewerStage() {
        return boroughViewerStage;
    }

    /**
     * Returns the BoroughViewerController instance
     *
     * @return boroughViewerController
     */
    public BoroughViewerController getBoroughViewerController() {
        return boroughViewerController;
    }

    /**
     * sets Minimum Slider to a specific value
     *
     * @param value value for minimum slider
     */
    public void setMinSlider(double value) {
        minSlider.setValue(value);
    }

    /**
     * Sets Maximum Slider to a specific value
     *
     * @param value value for maximum slider
     */
    public void setMaxSlider(double value) {
        maxSlider.setValue(value);
    }

    /**
     * Called when user searches for the listing closest to a given coordinate.
     *
     * @param actionEvent the user clicks search button
     */
    public void searchClosest(ActionEvent actionEvent) {
        try {
            if (!longCoord.getText().isEmpty() && !latCoord.getText().isEmpty()) {
                double longitude = Double.parseDouble(longCoord.getText());
                double latitude = Double.parseDouble(latCoord.getText());
                showClosestProperty(findClosestProperty(longitude, latitude));
            }
        } catch (NumberFormatException e) {
            alertCoordinates.showAndWait();
        }
    }

    /**
     * Displays closest property to a given coordinate.
     *
     * @param closestProperty closest Airbnb listing
     */
    private void showClosestProperty(AirbnbListing closestProperty) {
        if (closestProperty != null) {
            closestPropertyName.setText("Closest Property Found: " + closestProperty.getName());
        } else {
            alertCoordinates.showAndWait();
        }
    }

    /**
     * Searches for and returns the listing closest to a specified coordinate.
     *
     * @param longitude double the longitude of the location
     * @param latitude  double the latitude of the location
     * @return closestProperty AirbnbListing
     */
    private AirbnbListing findClosestProperty(double longitude, double latitude) {
        double shortestDistance = -1;
        AirbnbListing closestProperty = null;
        for (AirbnbListing favouriteListing : boroughViewerController.getFavouritedListings()) {
            double distance = (longitude - favouriteListing.getLongitude()) * (longitude - favouriteListing.getLongitude()) + (latitude - favouriteListing.getLatitude()) * (latitude - favouriteListing.getLatitude());
            if (shortestDistance == -1 || distance < shortestDistance) {
                shortestDistance = distance;
                closestProperty = favouriteListing;
            }
        }
        return closestProperty;
    }

    /**
     * Returns Hashmap of borough abbreviation
     *
     * @return borough Abbreviation
     */
    public HashMap<String, String> getBoroughAbbreviation() {
        return this.boroughAbbreviation;
    }

    /**
     * Returns ArrayList of all listings
     *
     * @return ArrayList all listings
     */
    public ArrayList<AirbnbListing> getAllListings() {
        return this.allListings;
    }

    /**
     * Compares favourited properties to find the cheapest, most expensive, most reviewed, averageTotalReviewed,
     * most popular host.
     * Sets Textfields to display these properties.
     *
     * @param actionEvent the user clicks 'Refresh Comparisons' button.
     */
    public void refreshCompare(ActionEvent actionEvent) {
        Statistics favouriteStatistics = new Statistics(boroughViewerController.getFavouritedListings());
        if (boroughViewerController.getFavouritedListings().size() != 0) {
            if (boroughViewerController.getFavouritedListings().size() == 1) {
                cheapestProperty.setText(favouriteStatistics.getExpensivePropertyName());
            } else {
                cheapestProperty.setText(favouriteStatistics.getCheapestPropertyName());
            }
            expensiveProperty.setText(favouriteStatistics.getExpensivePropertyName());
            mostReviews.setText(favouriteStatistics.getMostReviewed());
            averageTotalReviews.setText("" + favouriteStatistics.getAverageReviewPerProperty());
            propertyWithPopularHost.setText(favouriteStatistics.getPopularHost());
        } else {
            alertRefreshComparison.showAndWait();
        }
    }
}
