package com.example.trekyatriv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.LanguageManager;

import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application{
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        LanguageManager.setLocale(Locale.ENGLISH); // Default
        setRoot("Login");
        primaryStage.show();
    }
    public static void setRoot(String fxml) throws Exception {
        ResourceBundle bundle = LanguageManager.getBundle();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxmls/" + fxml + ".fxml"), bundle);
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle(bundle.getString("title." + fxml.toLowerCase()));
    }



}
