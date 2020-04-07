package StatisticsWindow;

import MainApplication.ApplicationController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Controller Class for StatisticsController Window.
 *
 * @author BASE
 */
public class StatisticsController {
    @FXML // fx:id="prevButton0"
    private Button prevButton0; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton0"
    private Button nextButton0; // Value injected by FXMLLoader

    @FXML // fx:id="statsName0"
    private Label statsName0; // Value injected by FXMLLoader

    @FXML // fx:id="statsFig0"
    private Label statsFig0; // Value injected by FXMLLoader

    @FXML // fx:id="prevButton2"
    private Button prevButton2; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton2"
    private Button nextButton2; // Value injected by FXMLLoader

    @FXML // fx:id="statsName2"
    private Label statsName2; // Value injected by FXMLLoader

    @FXML // fx:id="statsFig2"
    private Label statsFig2; // Value injected by FXMLLoader

    @FXML // fx:id="prevButton1"
    private Button prevButton1; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton1"
    private Button nextButton1; // Value injected by FXMLLoader

    @FXML // fx:id="statsName1"
    private Label statsName1; // Value injected by FXMLLoader

    @FXML // fx:id="statsFig1"
    private Label statsFig1; // Value injected by FXMLLoader

    @FXML // fx:id="prevButton3"
    private Button prevButton3; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton3"
    private Button nextButton3; // Value injected by FXMLLoader

    @FXML // fx:id="statsName3"
    private Label statsName3; // Value injected by FXMLLoader

    @FXML // fx:id="statsFig3"
    private Label statsFig3; // Value injected by FXMLLoader

    @FXML
    private Pane barChartPane;

    private ApplicationController ParentController;

    private Statistics statistics;

    private HashMap<String, String> allStatistics = new HashMap<String, String>();

    private HashMap<Label, Label> shownStatistics;

    private DecimalFormat priceFormatter;

    private DecimalFormat avgReviewFormatter;

    private XYChart.Series dataSeries;

    private BarChart<String, Number> barChart;

    /**
     * This method is called by the FXMLLoader when initialisation is completed
     */
    @FXML
    void initialize() {
        assert prevButton0 != null : "fx:id=\"prevButton0\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert nextButton0 != null : "fx:id=\"nextButton0\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsName0 != null : "fx:id=\"statsName0\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsFig0 != null : "fx:id=\"statsFig0\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert prevButton2 != null : "fx:id=\"prevButton2\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert nextButton2 != null : "fx:id=\"nextButton2\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsName2 != null : "fx:id=\"statsName2\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsFig2 != null : "fx:id=\"statsFig2\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert prevButton1 != null : "fx:id=\"prevButton1\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert nextButton1 != null : "fx:id=\"nextButton1\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsName1 != null : "fx:id=\"statsName1\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsFig1 != null : "fx:id=\"statsFig1\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert prevButton3 != null : "fx:id=\"prevButton3\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert nextButton3 != null : "fx:id=\"nextButton3\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsName3 != null : "fx:id=\"statsName3\" was not injected: check your FXML file 'StatisticsWindow'.";
        assert statsFig3 != null : "fx:id=\"statsFig3\" was not injected: check your FXML file 'StatisticsWindow'.";

        // Defines formatters used when updating statistics
        priceFormatter = new DecimalFormat("#.00");
        avgReviewFormatter = new DecimalFormat("#.0");
    }

    /**
     * Updates statistics based on the data and displays on window
     */
    public void updateStats() {
        setUpBarChart();
        statistics.updateAllStatistics();
        allStatistics.put("Average Property Price", "£ " + priceFormatter.format(statistics.getAveragePrice()));
        allStatistics.put("Total Properties", String.valueOf(statistics.getNumberOfProperties()));
        allStatistics.put("Average No. Of Reviews", avgReviewFormatter.format(statistics.getAverageReviewPerProperty()));
        allStatistics.put("Number of Home Properties", String.valueOf(statistics.getNumberOfHome()));
        allStatistics.put("Number of Private Properties", String.valueOf(statistics.getNumberOfPrivate()));
        allStatistics.put("Number of Shared Properties", String.valueOf(statistics.getNumberOfShared()));
        allStatistics.put("Most Expensive Property", statistics.getExpensivePropertyName() + " @ £" + priceFormatter.format(statistics.getMostExpensivePropertyPrice()));
        allStatistics.put("Cheapest Property", statistics.getCheapestPropertyName() + " @ £" + priceFormatter.format(statistics.getCheapestPropertyPrice()));
        allStatistics.put("Most Expensive Borough", statistics.getExpensiveBoroughName() + " @ £" + priceFormatter.format(statistics.getExpensiveBoroughPrice()));
        allStatistics.put("Cheapest Borough", statistics.getCheapestBoroughName() + " @ £" + priceFormatter.format(statistics.getCheapestBoroughPrice()));
    }

    /**
     * Creates bar chart and displays statistics. Adds visual element to the statistics tab
     */
    private void setUpBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setTitle("Average Price Per Borough");
        //xAxis.setLabel("Borough Name");
        yAxis.setLabel("Average Property Price x Minimum Nights (£)");
        dataSeries = new XYChart.Series();
        HashMap<Float, String> boroughPrices = statistics.getAvgBoroughPrice();
        for (Float boroughPrice : boroughPrices.keySet()) {
            dataSeries.getData().add(new XYChart.Data<>(ParentController.getBoroughAbbreivation(boroughPrices.get(boroughPrice)), boroughPrice));
        }
        barChart.getData().add(dataSeries);
        barChartPane.getChildren().clear();
        barChartPane.getChildren().add(barChart);
    }

    /**
     * Set initial statistics
     */
    public void setInitialStats() {
        setNextStat(statsName0, statsFig0);
        setNextStat(statsName1, statsFig1);
        setNextStat(statsName2, statsFig2);
        setNextStat(statsName3, statsFig3);
    }

    /**
     * Put statistics that are already shown on table
     *
     * @return shownStatistics a hashmap which holds statistics that are already displayed on window
     */
    private HashMap<Label, Label> getShownStatistics() {
        shownStatistics = new HashMap<>();
        shownStatistics.put(statsName0, statsFig0);
        shownStatistics.put(statsName1, statsFig1);
        shownStatistics.put(statsName2, statsFig2);
        shownStatistics.put(statsName3, statsFig3);
        return shownStatistics;
    }

    /**
     * Update shownStatistics so it shows what is should display
     */
    public void updateShownStatistics() {
        HashMap<Label, Label> shownStats = getShownStatistics();
        for (Label statsLabel : shownStats.keySet()) {
            if (!statsLabel.getText().equals("Stats0") && !statsLabel.getText().equals("Stats1") && !statsLabel.getText().equals("Stats2") && !statsLabel.getText().equals("Stats3")) {
                allStatistics.put(statsLabel.getText(), allStatistics.get(statsLabel.getText()));
                shownStats.get(statsLabel).setText(allStatistics.get(statsLabel.getText()));
            }
        }
    }

    /**
     * Sets the next statistic that should be displayed
     *
     * @param statsName label for statistic (e.g. the most expensive borough)
     * @param statsFig  label for statistic figure (e.g. Kingston Upon Thames)
     */
    private void setNextStat(Label statsName, Label statsFig) {
        String nextStatName = getNextStatName();
        statsName.setText(nextStatName);
        statsFig.setText(allStatistics.get(nextStatName));
    }

    /**
     * Returns the name/key of a random statistic which is not currently being shown
     *
     * @return String of a random statistic name
     */
    private String getNextStatName() {
        List<String> unshownStats = getUnshownStats();
        Random rand = new Random();
        return unshownStats.get(rand.nextInt(unshownStats.size())); // returns random stat, we may want to change this to return in some special order instead
    }

    /**
     * Collect and put all the statistics which are not displayed on window yet
     *
     * @return a hashMap which holds statistics that are not shown on window yet
     */
    private List<String> getUnshownStats() {
        ArrayList<String> statsLabelText = new ArrayList<>();

        getShownStatistics().keySet().forEach(statsLabel -> statsLabelText.add(statsLabel.getText()));

        return allStatistics.keySet().stream()
                .filter(statName -> !statsLabelText.contains(statName))
                .collect(Collectors.toList());
    }

    @FXML
    public void prevButton0(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName0, statsFig0);
    }

    @FXML
    public void nextButton0(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName0, statsFig0);
    }

    @FXML
    public void prevButton1(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName1, statsFig1);
    }

    @FXML
    public void nextButton1(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName1, statsFig1);
    }

    @FXML
    public void prevButton2(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName2, statsFig2);
    }

    @FXML
    public void nextButton2(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName2, statsFig2);
    }

    @FXML
    public void prevButton3(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName3, statsFig3);
    }

    @FXML
    public void nextButton3(javafx.scene.input.MouseEvent mouseEvent) {
        setNextStat(statsName3, statsFig3);
    }

    public void setParentController(ApplicationController parentController) {
        ParentController = parentController;
    }

    /**
     * Update statistics that are currently displayed.
     *
     * @param statistics
     */
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
        updateStats();
    }
}
