package com.trekyatri.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.trekyatri.model.*;
import com.trekyatri.service.DataService;
import com.trekyatri.util.LanguageManager;
import com.trekyatri.util.FestivalManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Collectors;

public class BookingController {

    @FXML private TextField bookingIdField;
    @FXML private ComboBox<Tourist> touristComboBox;
    @FXML private ComboBox<Guide> guideComboBox;
    @FXML private ComboBox<Attraction> attractionComboBox;
    @FXML private DatePicker trekDatePicker;
    @FXML private ComboBox<Booking.BookingStatus> statusComboBox;
    @FXML private TextField priceField;
    @FXML private TextField discountField;
    @FXML private TextField searchField;
    @FXML private Label festivalLabel;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button emergencyButton;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> guideColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, LocalDate> trekDateColumn;
    @FXML private TableColumn<Booking, Booking.BookingStatus> statusColumn;
    @FXML private TableColumn<Booking, Double> priceColumn;

    private DataService dataService;
    private LanguageManager languageManager;
    private FestivalManager festivalManager;
    private ObservableList<Booking> bookingList;

    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        languageManager = LanguageManager.getInstance();
        festivalManager = FestivalManager.getInstance();
        bookingList = FXCollections.observableArrayList();

        setupComboBoxes();
        setupTableColumns();
        loadBookings();
        updateLanguage();
        checkFestivalDiscount();

        // Add selection listener
        bookingTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        // Add attraction change listener for price calculation
        attractionComboBox.setOnAction(e -> calculatePrice());
    }

    private void setupComboBoxes() {
        // Load tourists
        ObservableList<Tourist> tourists = FXCollections.observableArrayList(dataService.getAllTourists());
        touristComboBox.setItems(tourists);

        // Load guides
        ObservableList<Guide> guides = FXCollections.observableArrayList(dataService.getAllGuides());
        guideComboBox.setItems(guides);

        // Load attractions
        ObservableList<Attraction> attractions = FXCollections.observableArrayList(dataService.getAllAttractions());
        attractionComboBox.setItems(attractions);

        // Load booking statuses
        statusComboBox.setItems(FXCollections.observableArrayList(Booking.BookingStatus.values()));
        statusComboBox.setValue(Booking.BookingStatus.PENDING);
    }

    private void setupTableColumns() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        touristColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTourist() != null ?
                                cellData.getValue().getTourist().getName() : ""));
        guideColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getGuide() != null ?
                                cellData.getValue().getGuide().getName() : ""));
        attractionColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAttraction() != null ?
                                cellData.getValue().getAttraction().getName() : ""));
        trekDateColumn.setCellValueFactory(new PropertyValueFactory<>("trekDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        bookingTable.setItems(bookingList);
    }

    private void updateLanguage() {
        addButton.setText(languageManager.getString("button.add"));
        updateButton.setText(languageManager.getString("button.update"));
        deleteButton.setText(languageManager.getString("button.delete"));
        clearButton.setText(languageManager.getString("button.clear"));
    }

    private void checkFestivalDiscount() {
        if (festivalManager.isFestivalPeriod()) {
            FestivalManager.FestivalInfo festival = festivalManager.getCurrentFestival();
            festivalLabel.setText("🎉 " + festival.getName() + " Festival! " +
                    festival.getDiscountPercentage() + "% Discount Available!");
            festivalLabel.setVisible(true);
            discountField.setText(String.valueOf(festival.getDiscountPercentage()));
        } else {
            festivalLabel.setVisible(false);
            discountField.setText("0");
        }
    }

    @FXML
    private void calculatePrice() {
        Attraction selectedAttraction = attractionComboBox.getValue();
        if (selectedAttraction != null) {
            double basePrice = selectedAttraction.getPrice();
            double discount = 0;

            try {
                discount = Double.parseDouble(discountField.getText().trim());
            } catch (NumberFormatException e) {
                discount = 0;
            }

            double finalPrice = basePrice * (1 - discount / 100);
            priceField.setText(String.format("%.2f", finalPrice));

            // Check for safety warnings
            checkSafetyWarnings(selectedAttraction);
        }
    }

    private void checkSafetyWarnings(Attraction attraction) {
        if (attraction.isHighAltitude()) {
            showAlert(Alert.AlertType.WARNING,
                    "Safety Warning",
                    languageManager.getString("safety.altitude.warning"));
        }

        LocalDate trekDate = trekDatePicker.getValue();
        if (trekDate != null) {
            Month trekMonth = trekDate.getMonth();
            if ((trekMonth == Month.JUNE || trekMonth == Month.JULY || trekMonth == Month.AUGUST)
                    && attraction.isHighAltitude()) {
                showAlert(Alert.AlertType.ERROR,
                        "Monsoon Warning",
                        languageManager.getString("safety.monsoon.warning"));
            }
        }
    }

    private void loadBookings() {
        bookingList.clear();
        bookingList.addAll(dataService.getAllBookings());
    }

    @FXML
    private void onAddBooking() {
        if (!validateFields()) return;

        String id = bookingIdField.getText().trim();
        if (dataService.getBooking(id) != null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Booking ID already exists!");
            return;
        }

        Booking booking = createBookingFromFields();
        dataService.addBooking(booking);
        loadBookings();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Booking added successfully!");
    }

    @FXML
    private void onUpdateBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking to update!");
            return;
        }

        if (!validateFields()) return;

        Booking booking = createBookingFromFields();
        dataService.updateBooking(booking);
        loadBookings();
        clearFields();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Booking updated successfully!");
    }

    @FXML
    private void onDeleteBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this booking?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            dataService.deleteBooking(selected.getBookingId());
            loadBookings();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully!");
        }
    }

    @FXML
    private void onClearFields() {
        clearFields();
    }

    @FXML
    private void onReportEmergency() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking to report emergency!");
            return;
        }

        selected.setEmergencyReported(true);
        selected.setNotes("Emergency reported on " + LocalDate.now());
        dataService.updateBooking(selected);
        loadBookings();

        showAlert(Alert.AlertType.INFORMATION, "Emergency Reported",
                "Emergency has been logged for booking: " + selected.getBookingId());
    }

    @FXML
    private void onSearchKeyReleased() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            loadBookings();
        } else {
            ObservableList<Booking> filteredList = FXCollections.observableArrayList(
                    dataService.getAllBookings().stream()
                            .filter(booking ->
                                    (booking.getTourist() != null && booking.getTourist().getName().toLowerCase().contains(searchText)) ||
                                            (booking.getGuide() != null && booking.getGuide().getName().toLowerCase().contains(searchText)) ||
                                            (booking.getAttraction() != null && booking.getAttraction().getName().toLowerCase().contains(searchText)) ||
                                            booking.getBookingId().toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            bookingTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (bookingIdField.getText().trim().isEmpty() ||
                touristComboBox.getValue() == null ||
                guideComboBox.getValue() == null ||
                attractionComboBox.getValue() == null ||
                trekDatePicker.getValue() == null) {

            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all required fields!");
            return false;
        }

        if (trekDatePicker.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Trek date cannot be in the past!");
            return false;
        }

        return true;
    }

    private Booking createBookingFromFields() {
        Booking booking = new Booking(
                bookingIdField.getText().trim(),
                touristComboBox.getValue(),
                guideComboBox.getValue(),
                attractionComboBox.getValue(),
                trekDatePicker.getValue()
        );

        booking.setStatus(statusComboBox.getValue());

        try {
            double discount = Double.parseDouble(discountField.getText().trim());
            booking.setDiscountPercentage(discount);
        } catch (NumberFormatException e) {
            booking.setDiscountPercentage(0);
        }

        return booking;
    }

    private void populateFields(Booking booking) {
        bookingIdField.setText(booking.getBookingId());
        touristComboBox.setValue(booking.getTourist());
        guideComboBox.setValue(booking.getGuide());
        attractionComboBox.setValue(booking.getAttraction());
        trekDatePicker.setValue(booking.getTrekDate());
        statusComboBox.setValue(booking.getStatus());
        priceField.setText(String.valueOf(booking.getTotalPrice()));
        discountField.setText(String.valueOf(booking.getDiscountPercentage()));
    }

    private void clearFields() {
        bookingIdField.clear();
        touristComboBox.setValue(null);
        guideComboBox.setValue(null);
        attractionComboBox.setValue(null);
        trekDatePicker.setValue(null);
        statusComboBox.setValue(Booking.BookingStatus.PENDING);
        priceField.clear();
        discountField.setText("0");
        bookingTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
