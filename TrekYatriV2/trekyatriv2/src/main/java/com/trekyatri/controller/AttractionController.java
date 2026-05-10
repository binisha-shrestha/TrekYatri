package com.trekyatri.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.trekyatri.model.Attraction;
import com.trekyatri.service.DataService;
import com.trekyatri.util.LanguageManager;
import com.trekyatri.util.WeatherService;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Collectors;

public class AttractionController {

    @FXML private TextField attractionIdField;
    @FXML private TextField attractionNameField;
    @FXML private TextField locationField;
    @FXML private TextField descriptionField;
    @FXML private TextField altitudeField;
    @FXML private TextField priceField;
    @FXML private TextField searchField;
    @FXML private ComboBox<Attraction.AttractionType> typeComboBox;
    @FXML private ComboBox<Attraction.DifficultyLevel> difficultyComboBox;
    @FXML private Label weatherLabel;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button altitudeCheckButton;

    @FXML private TableView<Attraction> attractionTable;
    @FXML private TableColumn<Attraction, String> attractionIdColumn;
    @FXML private TableColumn<Attraction, String> attractionNameColumn;
    @FXML private TableColumn<Attraction, String> locationColumn;
    @FXML private TableColumn<Attraction, Attraction.AttractionType> typeColumn;
    @FXML private TableColumn<Attraction, Attraction.DifficultyLevel> difficultyColumn;
    @FXML private TableColumn<Attraction, Integer> altitudeColumn;
    @FXML private TableColumn<Attraction, Double> priceColumn;

    private DataService dataService;
    private LanguageManager languageManager;
    private WeatherService weatherService;
    private ObservableList<Attraction> attractionList;

    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        languageManager = LanguageManager.getInstance();
        weatherService = WeatherService.getInstance();
        attractionList = FXCollections.observableArrayList();

        setupComboBoxes();
        setupTableColumns();
        loadAttractions();
        updateLanguage();

        // Add selection listener
        attractionTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                        updateWeatherInfo(newSelection.getLocation());
                    }
                });
    }

    private void setupComboBoxes() {
        typeComboBox.setItems(FXCollections.observableArrayList(Attraction.AttractionType.values()));
        difficultyComboBox.setItems(FXCollections.observableArrayList(Attraction.DifficultyLevel.values()));
    }

    private void setupTableColumns() {
        attractionIdColumn.setCellValueFactory(new PropertyValueFactory<>("attractionId"));
        attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        altitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        attractionTable.setItems(attractionList);
    }

    private void updateLanguage() {
        addButton.setText(languageManager.getString("button.add"));
        updateButton.setText(languageManager.getString("button.update"));
        deleteButton.setText(languageManager.getString("button.delete"));
        clearButton.setText(languageManager.getString("button.clear"));
    }

    private void updateWeatherInfo(String location) {
        WeatherService.WeatherInfo weather = weatherService.getWeatherInfo(location);
        weatherLabel.setText(weather.toString());
    }

    private void loadAttractions() {
        attractionList.clear();
        attractionList.addAll(dataService.getAllAttractions());
    }

    @FXML
    private void onAddAttraction() {
        if (!validateFields()) return;

        String id = attractionIdField.getText().trim();
        if (dataService.getAttraction(id) != null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Attraction ID already exists!");
            return;
        }

        Attraction attraction = createAttractionFromFields();
        dataService.addAttraction(attraction);
        loadAttractions();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Attraction added successfully!");
    }

    @FXML
    private void onUpdateAttraction() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an attraction to update!");
            return;
        }

        if (!validateFields()) return;

        Attraction attraction = createAttractionFromFields();
        dataService.updateAttraction(attraction);
        loadAttractions();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Attraction updated successfully!");
    }

    @FXML
    private void onDeleteAttraction() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an attraction to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this attraction?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            dataService.deleteAttraction(selected.getAttractionId());
            loadAttractions();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Attraction deleted successfully!");
        }
    }

    @FXML
    private void onClearFields() {
        clearFields();
    }

    @FXML
    private void onAltitudeCheck() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an attraction first!");
            return;
        }

        String message = "Altitude: " + selected.getAltitude() + "m\n";

        if (selected.isHighAltitude()) {
            message += "⚠️ HIGH ALTITUDE WARNING!\n";
            message += languageManager.getString("safety.altitude.warning") + "\n";
        }

        LocalDate now = LocalDate.now();
        Month currentMonth = now.getMonth();
        if ((currentMonth == Month.JUNE || currentMonth == Month.JULY || currentMonth == Month.AUGUST)
                && selected.isHighAltitude()) {
            message += "🌧️ MONSOON WARNING!\n";
            message += languageManager.getString("safety.monsoon.warning");
        }

        if (!selected.isHighAltitude()) {
            message += "✅ Safe altitude for most trekkers.";
        }

        showAlert(Alert.AlertType.INFORMATION, "Altitude Safety Check", message);
    }

    @FXML
    private void onSearchKeyReleased() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            loadAttractions();
        } else {
            ObservableList<Attraction> filteredList = FXCollections.observableArrayList(
                    dataService.getAllAttractions().stream()
                            .filter(attraction ->
                                    attraction.getName().toLowerCase().contains(searchText) ||
                                            attraction.getLocation().toLowerCase().contains(searchText) ||
                                            attraction.getDescription().toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            attractionTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (attractionIdField.getText().trim().isEmpty() ||
                attractionNameField.getText().trim().isEmpty() ||
                locationField.getText().trim().isEmpty() ||
                typeComboBox.getValue() == null ||
                difficultyComboBox.getValue() == null ||
                altitudeField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all required fields!");
            return false;
        }

        try {
            Integer.parseInt(altitudeField.getText().trim());
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Altitude and Price must be valid numbers!");
            return false;
        }

        return true;
    }

    private Attraction createAttractionFromFields() {
        return new Attraction(
                attractionIdField.getText().trim(),
                attractionNameField.getText().trim(),
                descriptionField.getText().trim(),
                locationField.getText().trim(),
                typeComboBox.getValue(),
                difficultyComboBox.getValue(),
                Integer.parseInt(altitudeField.getText().trim()),
                Double.parseDouble(priceField.getText().trim())
        );
    }

    private void populateFields(Attraction attraction) {
        attractionIdField.setText(attraction.getAttractionId());
        attractionNameField.setText(attraction.getName());
        locationField.setText(attraction.getLocation());
        descriptionField.setText(attraction.getDescription());
        typeComboBox.setValue(attraction.getType());
        difficultyComboBox.setValue(attraction.getDifficulty());
        altitudeField.setText(String.valueOf(attraction.getAltitude()));
        priceField.setText(String.valueOf(attraction.getPrice()));
    }

    private void clearFields() {
        attractionIdField.clear();
        attractionNameField.clear();
        locationField.clear();
        descriptionField.clear();
        typeComboBox.setValue(null);
        difficultyComboBox.setValue(null);
        altitudeField.clear();
        priceField.clear();
        weatherLabel.setText("Select an attraction to see weather");
        attractionTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
