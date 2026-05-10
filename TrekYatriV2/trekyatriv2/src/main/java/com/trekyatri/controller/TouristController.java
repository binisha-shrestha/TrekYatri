package com.trekyatri.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.trekyatri.model.Tourist;
import com.trekyatri.service.DataService;
import com.trekyatri.util.LanguageManager;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class TouristController {
    
    @FXML private TextField touristIdField;
    @FXML private TextField touristNameField;
    @FXML private TextField touristNationalityField;
    @FXML private TextField touristContactField;
    @FXML private TextField touristEmergencyField;
    @FXML private TextField touristEmailField;
    @FXML private TextField searchField;
    
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    
    @FXML private TableView<Tourist> touristTable;
    @FXML private TableColumn<Tourist, String> touristIdColumn;
    @FXML private TableColumn<Tourist, String> touristNameColumn;
    @FXML private TableColumn<Tourist, String> touristNationalityColumn;
    @FXML private TableColumn<Tourist, String> touristContactColumn;
    @FXML private TableColumn<Tourist, String> touristEmergencyColumn;
    @FXML private TableColumn<Tourist, String> touristEmailColumn;
    @FXML private TableColumn<Tourist, LocalDate> touristDateColumn;
    
    private DataService dataService;
    private LanguageManager languageManager;
    private ObservableList<Tourist> touristList;
    
    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        languageManager = LanguageManager.getInstance();
        touristList = FXCollections.observableArrayList();
        
        setupTableColumns();
        loadTourists();
        updateLanguage();
        
        // Add selection listener
        touristTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFields(newSelection);
                }
            });
    }
    
    private void setupTableColumns() {
        touristIdColumn.setCellValueFactory(new PropertyValueFactory<>("touristId"));
        touristNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        touristNationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        touristContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        touristEmergencyColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContact"));
        touristEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        touristDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        
        touristTable.setItems(touristList);
    }
    
    private void updateLanguage() {
        addButton.setText(languageManager.getString("button.add"));
        updateButton.setText(languageManager.getString("button.update"));
        deleteButton.setText(languageManager.getString("button.delete"));
        clearButton.setText(languageManager.getString("button.clear"));
    }
    
    private void loadTourists() {
        touristList.clear();
        touristList.addAll(dataService.getAllTourists());
    }
    
    @FXML
    private void onAddTourist() {
        if (!validateFields()) return;
        
        String id = touristIdField.getText().trim();
        if (dataService.getTourist(id) != null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Tourist ID already exists!");
            return;
        }
        
        Tourist tourist = createTouristFromFields();
        dataService.addTourist(tourist);
        loadTourists();
        clearFields();
        
        showAlert(Alert.AlertType.INFORMATION, "Success", "Tourist added successfully!");
    }
    
    @FXML
    private void onUpdateTourist() {
        Tourist selected = touristTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a tourist to update!");
            return;
        }
        
        if (!validateFields()) return;
        
        Tourist tourist = createTouristFromFields();
        dataService.updateTourist(tourist);
        loadTourists();
        clearFields();
        
        showAlert(Alert.AlertType.INFORMATION, "Success", "Tourist updated successfully!");
    }
    
    @FXML
    private void onDeleteTourist() {
        Tourist selected = touristTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a tourist to delete!");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this tourist?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            dataService.deleteTourist(selected.getTouristId());
            loadTourists();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Tourist deleted successfully!");
        }
    }
    
    @FXML
    private void onClearFields() {
        clearFields();
    }
    
    @FXML
    private void onSearchKeyReleased() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            loadTourists();
        } else {
            ObservableList<Tourist> filteredList = FXCollections.observableArrayList(
                dataService.getAllTourists().stream()
                    .filter(tourist -> 
                        tourist.getName().toLowerCase().contains(searchText) ||
                        tourist.getNationality().toLowerCase().contains(searchText) ||
                        tourist.getContact().toLowerCase().contains(searchText))
                    .collect(Collectors.toList())
            );
            touristTable.setItems(filteredList);
        }
    }
    
    private boolean validateFields() {
        if (touristIdField.getText().trim().isEmpty() ||
            touristNameField.getText().trim().isEmpty() ||
            touristNationalityField.getText().trim().isEmpty() ||
            touristContactField.getText().trim().isEmpty()) {
            
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all required fields!");
            return false;
        }
        return true;
    }
    
    private Tourist createTouristFromFields() {
        return new Tourist(
            touristIdField.getText().trim(),
            touristNameField.getText().trim(),
            touristNationalityField.getText().trim(),
            touristContactField.getText().trim(),
            touristEmergencyField.getText().trim(),
            touristEmailField.getText().trim()
        );
    }
    
    private void populateFields(Tourist tourist) {
        touristIdField.setText(tourist.getTouristId());
        touristNameField.setText(tourist.getName());
        touristNationalityField.setText(tourist.getNationality());
        touristContactField.setText(tourist.getContact());
        touristEmergencyField.setText(tourist.getEmergencyContact());
        touristEmailField.setText(tourist.getEmail());
    }
    
    private void clearFields() {
        touristIdField.clear();
        touristNameField.clear();
        touristNationalityField.clear();
        touristContactField.clear();
        touristEmergencyField.clear();
        touristEmailField.clear();
        touristTable.getSelectionModel().clearSelection();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
