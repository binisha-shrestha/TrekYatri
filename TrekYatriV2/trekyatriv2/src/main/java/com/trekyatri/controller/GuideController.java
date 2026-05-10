package com.trekyatri.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.trekyatri.model.Guide;
import com.trekyatri.service.DataService;
import com.trekyatri.util.LanguageManager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GuideController {

    @FXML private TextField guideIdField;
    @FXML private TextField guideNameField;
    @FXML private TextField languagesField;
    @FXML private TextField experienceField;
    @FXML private TextField contactField;
    @FXML private TextField emailField;
    @FXML private TextField specializationField;
    @FXML private TextField searchField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @FXML private TableView<Guide> guideTable;
    @FXML private TableColumn<Guide, String> guideIdColumn;
    @FXML private TableColumn<Guide, String> guideNameColumn;
    @FXML private TableColumn<Guide, String> languagesColumn;
    @FXML private TableColumn<Guide, Integer> experienceColumn;
    @FXML private TableColumn<Guide, String> contactColumn;
    @FXML private TableColumn<Guide, String> emailColumn;
    @FXML private TableColumn<Guide, String> specializationColumn;

    private DataService dataService;
    private LanguageManager languageManager;
    private ObservableList<Guide> guideList;

    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        languageManager = LanguageManager.getInstance();
        guideList = FXCollections.observableArrayList();

        setupTableColumns();
        loadGuides();
        updateLanguage();

        // Add selection listener
        guideTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    private void setupTableColumns() {
        guideIdColumn.setCellValueFactory(new PropertyValueFactory<>("guideId"));
        guideNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        languagesColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.join(", ", cellData.getValue().getLanguages())));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experienceYears"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        guideTable.setItems(guideList);
    }

    private void updateLanguage() {
        addButton.setText(languageManager.getString("button.add"));
        updateButton.setText(languageManager.getString("button.update"));
        deleteButton.setText(languageManager.getString("button.delete"));
        clearButton.setText(languageManager.getString("button.clear"));
    }

    private void loadGuides() {
        guideList.clear();
        guideList.addAll(dataService.getAllGuides());
    }

    @FXML
    private void onAddGuide() {
        if (!validateFields()) return;

        String id = guideIdField.getText().trim();
        if (dataService.getGuide(id) != null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Guide ID already exists!");
            return;
        }

        Guide guide = createGuideFromFields();
        dataService.addGuide(guide);
        loadGuides();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Guide added successfully!");
    }

    @FXML
    private void onUpdateGuide() {
        Guide selected = guideTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a guide to update!");
            return;
        }

        if (!validateFields()) return;

        Guide guide = createGuideFromFields();
        dataService.updateGuide(guide);
        loadGuides();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Guide updated successfully!");
    }

    @FXML
    private void onDeleteGuide() {
        Guide selected = guideTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a guide to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this guide?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            dataService.deleteGuide(selected.getGuideId());
            loadGuides();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Guide deleted successfully!");
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
            loadGuides();
        } else {
            ObservableList<Guide> filteredList = FXCollections.observableArrayList(
                    dataService.getAllGuides().stream()
                            .filter(guide ->
                                    guide.getName().toLowerCase().contains(searchText) ||
                                            guide.getSpecialization().toLowerCase().contains(searchText) ||
                                            guide.getContact().toLowerCase().contains(searchText) ||
                                            guide.getLanguages().stream().anyMatch(lang ->
                                                    lang.toLowerCase().contains(searchText)))
                            .collect(Collectors.toList())
            );
            guideTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (guideIdField.getText().trim().isEmpty() ||
                guideNameField.getText().trim().isEmpty() ||
                languagesField.getText().trim().isEmpty() ||
                experienceField.getText().trim().isEmpty() ||
                contactField.getText().trim().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all required fields!");
            return false;
        }

        try {
            Integer.parseInt(experienceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Experience must be a valid number!");
            return false;
        }

        return true;
    }

    private Guide createGuideFromFields() {
        String[] languageArray = languagesField.getText().trim().split(",");
        return new Guide(
                guideIdField.getText().trim(),
                guideNameField.getText().trim(),
                Arrays.stream(languageArray).map(String::trim).collect(Collectors.toList()),
                Integer.parseInt(experienceField.getText().trim()),
                contactField.getText().trim(),
                emailField.getText().trim(),
                specializationField.getText().trim()
        );
    }

    private void populateFields(Guide guide) {
        guideIdField.setText(guide.getGuideId());
        guideNameField.setText(guide.getName());
        languagesField.setText(String.join(", ", guide.getLanguages()));
        experienceField.setText(String.valueOf(guide.getExperienceYears()));
        contactField.setText(guide.getContact());
        emailField.setText(guide.getEmail());
        specializationField.setText(guide.getSpecialization());
    }

    private void clearFields() {
        guideIdField.clear();
        guideNameField.clear();
        languagesField.clear();
        experienceField.clear();
        contactField.clear();
        emailField.clear();
        specializationField.clear();
        guideTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
