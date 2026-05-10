package com.trekyatri.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import com.trekyatri.model.*;
import com.trekyatri.service.DataService;
import com.trekyatri.util.LanguageManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class ReportController {

    @FXML private Label totalTouristsLabel;
    @FXML private Label totalGuidesLabel;
    @FXML private Label totalAttractionsLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label popularAttractionLabel;
    @FXML private PieChart attractionTypeChart;
    @FXML private BarChart<String, Number> monthlyBookingsChart;
    @FXML private LineChart<String, Number> revenueChart;

    private DataService dataService;
    private LanguageManager languageManager;

    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        languageManager = LanguageManager.getInstance();

        loadStatistics();
        setupCharts();
    }

    private void loadStatistics() {
        // Basic statistics
        totalTouristsLabel.setText(String.valueOf(dataService.getAllTourists().size()));
        totalGuidesLabel.setText(String.valueOf(dataService.getAllGuides().size()));
        totalAttractionsLabel.setText(String.valueOf(dataService.getAllAttractions().size()));
        totalBookingsLabel.setText(String.valueOf(dataService.getAllBookings().size()));

        // Calculate total revenue
        double totalRevenue = dataService.getAllBookings().stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                .mapToDouble(Booking::getTotalPrice)
                .sum();
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));

        // Find most popular attraction
        Map<String, Long> attractionCounts = dataService.getAllBookings().stream()
                .filter(booking -> booking.getAttraction() != null)
                .collect(Collectors.groupingBy(
                        booking -> booking.getAttraction().getName(),
                        Collectors.counting()
                ));

        String popularAttraction = attractionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");

        popularAttractionLabel.setText(popularAttraction);
    }

    private void setupCharts() {
        setupAttractionTypeChart();
        setupMonthlyBookingsChart();
        setupRevenueChart();
    }

    private void setupAttractionTypeChart() {
        Map<Attraction.AttractionType, Long> typeCounts = dataService.getAllAttractions().stream()
                .collect(Collectors.groupingBy(
                        Attraction::getType,
                        Collectors.counting()
                ));

        attractionTypeChart.getData().clear();
        typeCounts.forEach((type, count) -> {
            PieChart.Data data = new PieChart.Data(type.toString(), count);
            attractionTypeChart.getData().add(data);
        });

        attractionTypeChart.setTitle("Attractions by Type");
    }

    private void setupMonthlyBookingsChart() {
        Map<Month, Long> monthlyBookings = dataService.getAllBookings().stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getBookingDate().getMonth(),
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings");

        for (Month month : Month.values()) {
            long count = monthlyBookings.getOrDefault(month, 0L);
            series.getData().add(new XYChart.Data<>(month.toString().substring(0, 3), count));
        }

        monthlyBookingsChart.getData().clear();
        monthlyBookingsChart.getData().add(series);
        monthlyBookingsChart.setTitle("Monthly Bookings");
    }

    private void setupRevenueChart() {
        Map<Month, Double> monthlyRevenue = dataService.getAllBookings().stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        booking -> booking.getBookingDate().getMonth(),
                        Collectors.summingDouble(Booking::getTotalPrice)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue ($)");

        for (Month month : Month.values()) {
            double revenue = monthlyRevenue.getOrDefault(month, 0.0);
            series.getData().add(new XYChart.Data<>(month.toString().substring(0, 3), revenue));
        }

        revenueChart.getData().clear();
        revenueChart.getData().add(series);
        revenueChart.setTitle("Monthly Revenue");
    }

    @FXML
    private void onRefreshData() {
        loadStatistics();
        setupCharts();
    }
}
