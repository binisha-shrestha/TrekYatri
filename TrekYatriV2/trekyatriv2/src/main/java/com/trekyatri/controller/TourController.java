package com.trekyatri.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.trekyatri.util.LanguageManager;

public class TourController {

    @FXML private Button dashboardButton;
    @FXML private Button touristManagementButton;
    @FXML private Button guideManagementButton;
    @FXML private Button attractionManagementButton;
    @FXML private Button reportButton;
    @FXML private Button annapurnaButton;
    @FXML private Button langtangButton;
    @FXML private Button everestButton;

    private LanguageManager languageManager;

    @FXML
    private void initialize() {
        languageManager = LanguageManager.getInstance();
    }

    @FXML
    private void onDashboardClicked() {
        openWindow("/fxml/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void onTouristManagementClicked() {
        openWindow("/fxml/TouristManagement.fxml", "Tourist Management");
    }

    @FXML
    private void onGuideManagementClicked() {
        openWindow("/fxml/GuideManagement.fxml", "Guide Management");
    }

    @FXML
    private void onAttractionManagementClicked() {
        openWindow("/fxml/AttractionManagement.fxml", "Attraction Management");
    }

    @FXML
    private void onReportClicked() {
        openWindow("/fxml/Report.fxml", "Reports");
    }

    @FXML
    private void onAnnapurnaExplore() {
        showTourInfo("Annapurna Base Camp",
                "Experience the stunning Annapurna Sanctuary with breathtaking mountain views. " +
                        "Duration: 12-14 days. Difficulty: Moderate. Best Season: March-May, September-November.");
    }

    @FXML
    private void onLangtangExplore() {
        showTourInfo("Langtang Valley Trek",
                "Discover the beautiful Langtang Valley, known as the 'Valley of Glaciers'. " +
                        "Duration: 7-10 days. Difficulty: Easy to Moderate. Best Season: March-May, October-November.");
    }

    @FXML
    private void onEverestExplore() {
        showTourInfo("Everest Base Camp",
                "Journey to the base of the world's highest mountain. An adventure of a lifetime! " +
                        "Duration: 14-16 days. Difficulty: Challenging. Best Season: March-May, September-November.");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.getRoot(), 900, 700);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("TrekYatri - " + title);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open " + title + ": " + e.getMessage());
        }
    }

    private void showTourInfo(String tourName, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tour Information");
        alert.setHeaderText(tourName);
        alert.setContentText(description);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
