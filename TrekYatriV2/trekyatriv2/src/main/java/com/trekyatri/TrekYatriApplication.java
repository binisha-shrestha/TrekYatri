package com.trekyatri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.trekyatri.util.LanguageManager;

import java.io.IOException;
import java.net.URL;

public class TrekYatriApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Initialize language manager
            LanguageManager.getInstance().setCurrentLanguage("en");

            // Load FXML file
            URL fxmlLocation = TrekYatriApplication.class.getResource("/fxml/Login.fxml");
            if (fxmlLocation == null) {
                throw new IOException("Cannot find Login.fxml file");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.getRoot(), 800, 600);

            // Add CSS styling
            URL cssLocation = getClass().getResource("/css/styles.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }

            stage.setTitle("TrekYatri - Nepal Tourism Management System");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading application: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
