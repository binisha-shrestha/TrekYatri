package com.example.trekyatriv2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private Label loginLabel, usernameLabel, passwordLabel;
    @FXML private Button loginButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> langBox;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        changeLanguage("en");

        langBox.getItems().addAll("English", "Nepali");
        langBox.setOnAction(e -> {
            String selected = langBox.getValue();
            if (selected.equals("Nepali")) {
                changeLanguage("ne");
            } else {
                changeLanguage("en");
            }
        });
    }

    private void changeLanguage(String langCode) {
        bundle = ResourceBundle.getBundle("org.example.app.lang.messages", new Locale(langCode));
        loginLabel.setText(bundle.getString("login.title"));
        usernameLabel.setText(bundle.getString("login.username"));
        passwordLabel.setText(bundle.getString("login.password"));
        loginButton.setText(bundle.getString("login.button"));
    }

    @FXML
    public void onLoginClicked() {
        // Your login logic here
    }
}
