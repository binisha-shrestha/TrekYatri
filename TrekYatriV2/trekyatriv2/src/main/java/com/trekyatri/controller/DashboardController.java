package com.trekyatri.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.trekyatri.util.LanguageManager;
import com.trekyatri.util.WeatherService;
import com.trekyatri.util.FestivalManager;

import java.io.IOException;

public class DashboardController {
    
    @FXML private Label titleLabel;
    @FXML private Label taglineLabel;
    @FXML private Button touristManagementButton;
    @FXML private Button guideManagementButton;
    @FXML private Button attractionManagementButton;
    @FXML private Button bookingButton;
    @FXML private Button tourPackagesButton;
    @FXML private Button reportButton;
    
    private LanguageManager languageManager;
    
    @FXML
    private void initialize() {
        languageManager = LanguageManager.getInstance();
        updateLanguage();
        showWelcomeInfo();
    }
    
    private void updateLanguage() {
        // Update button texts based on current language
        if (touristManagementButton != null) {
            touristManagementButton.setText(languageManager.getString("dashboard.tourist.management"));
        }
        if (guideManagementButton != null) {
            guideManagementButton.setText(languageManager.getString("dashboard.guide.management"));
        }
        if (attractionManagementButton != null) {
            attractionManagementButton.setText(languageManager.getString("dashboard.attraction.management"));
        }
        if (bookingButton != null) {
            bookingButton.setText(languageManager.getString("dashboard.booking"));
        }
        if (tourPackagesButton != null) {
            tourPackagesButton.setText(languageManager.getString("dashboard.tour.packages"));
        }
    }
    
    private void showWelcomeInfo() {
        // Show festival information if applicable
        FestivalManager festivalManager = FestivalManager.getInstance();
        if (festivalManager.isFestivalPeriod()) {
            FestivalManager.FestivalInfo festival = festivalManager.getCurrentFestival();
            showAlert(Alert.AlertType.INFORMATION, 
                     languageManager.getString("festival.title"),
                     String.format(languageManager.getString("festival.discount.message"), 
                                 festival.getName(), festival.getDiscountPercentage()));
        }
        
        // Show weather info for Kathmandu
        WeatherService weatherService = WeatherService.getInstance();
        WeatherService.WeatherInfo weather = weatherService.getWeatherInfo("Kathmandu");
        System.out.println("Current weather in Kathmandu: " + weather.toString());
    }
    
    @FXML
    private void handleTouristManagementButton() {
        openWindow("/fxml/TouristManagement.fxml", languageManager.getString("tourist.management.title"));
    }
    
    @FXML
    private void handleGuideManagementButton() {
        openWindow("/fxml/GuideManagement.fxml", languageManager.getString("guide.management.title"));
    }
    
    @FXML
    private void handleAttractionManagementButton() {
        openWindow("/fxml/AttractionManagement.fxml", languageManager.getString("attraction.management.title"));
    }
    
    @FXML
    private void handleBookingButton() {
        openWindow("/fxml/Booking.fxml", languageManager.getString("booking.title"));
    }
    
    @FXML
    private void handleTourPackagesButton() {
        openWindow("/fxml/Tour.fxml", languageManager.getString("tour.packages.title"));
    }
    
    @FXML
    private void handleReportButton() {
        openWindow("/fxml/Report.fxml", languageManager.getString("report.title"));
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
            showAlert(Alert.AlertType.ERROR, "Error", "Unexpected error occurred");
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
