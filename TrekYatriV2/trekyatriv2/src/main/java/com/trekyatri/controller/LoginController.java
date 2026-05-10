package com.trekyatri.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.trekyatri.util.LanguageManager;

import java.net.URL;

public class LoginController {

    @FXML private Label titleLabel;
    @FXML private Label languageLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private LanguageManager languageManager;

    @FXML
    private void initialize() {
        languageManager = LanguageManager.getInstance();

        // Initialize language combo box if not already set
        if (languageComboBox.getItems().isEmpty()) {
            languageComboBox.getItems().addAll("English", "नेपाली");
            languageComboBox.setValue("English");
        }

        updateLanguage();
    }

    @FXML
    private void onLanguageChanged() {
        String selectedLanguage = languageComboBox.getValue();
        if ("नेपाली".equals(selectedLanguage)) {
            languageManager.setCurrentLanguage("ne");
        } else {
            languageManager.setCurrentLanguage("en");
        }
        updateLanguage();
    }

    private void updateLanguage() {
        if (titleLabel != null) titleLabel.setText(languageManager.getString("login.title"));
        if (languageLabel != null) languageLabel.setText(languageManager.getString("login.language"));
        if (usernameLabel != null) usernameLabel.setText(languageManager.getString("login.username"));
        if (passwordLabel != null) passwordLabel.setText(languageManager.getString("login.password"));
        if (loginButton != null) loginButton.setText(languageManager.getString("login.button"));

        if (usernameField != null) usernameField.setPromptText(languageManager.getString("login.username.prompt"));
        if (passwordField != null) passwordField.setPromptText(languageManager.getString("login.password.prompt"));
    }

    @FXML
    private void onLoginClicked() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    languageManager.getString("login.error.title"),
                    languageManager.getString("login.error.empty"));
            return;
        }

        // Simple authentication (in real app, use proper authentication)
        if ("admin".equals(username) && "admin".equals(password)) {
            openDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    languageManager.getString("login.error.title"),
                    languageManager.getString("login.error.invalid"));
        }
    }

    private void openDashboard() {
        try {
            URL fxmlLocation = getClass().getResource("/fxml/Dashboard.fxml");
            if (fxmlLocation == null) {
                throw new Exception("Cannot find Dashboard.fxml file");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.getRoot(), 800, 600);

            URL cssLocation = getClass().getResource("/css/styles.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TrekYatri - " + languageManager.getString("dashboard.title"));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard: " + e.getMessage());
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
