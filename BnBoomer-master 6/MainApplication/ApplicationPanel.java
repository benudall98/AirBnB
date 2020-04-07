package MainApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Application Panel is the main class of the BASE Property Viewer application. It builds and
 * displays the application GUI and initialises all other components.
 *
 * @author BASE
 */
public class ApplicationPanel extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane mainPane = (AnchorPane) FXMLLoader.load(ApplicationPanel.class.getResource("MainWindow.fxml"));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
        primaryStage.setTitle("BASE Property Viewer");
        primaryStage.show();
    }
}