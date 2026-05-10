package com.trekyatri.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookingId;
    private Tourist tourist;
    private Guide guide;
    private Attraction attraction;
    private LocalDate bookingDate;
    private LocalDate trekDate;
    private BookingStatus status;
    private double totalPrice;
    private double discountPercentage;
    private String notes;
    private boolean emergencyReported;
    
    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }
    
    public Booking() {
        this.bookingDate = LocalDate.now();
        this.status = BookingStatus.PENDING;
        this.discountPercentage = 0.0;
        this.emergencyReported = false;
    }
    
    public Booking(String bookingId, Tourist tourist, Guide guide, Attraction attraction, 
                   LocalDate trekDate) {
        this.bookingId = bookingId;
        this.tourist = tourist;
        this.guide = guide;
        this.attraction = attraction;
        this.bookingDate = LocalDate.now();
        this.trekDate = trekDate;
        this.status = BookingStatus.PENDING;
        this.totalPrice = attraction.getPrice();
        this.discountPercentage = 0.0;
        this.emergencyReported = false;
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public Tourist getTourist() { return tourist; }
    public void setTourist(Tourist tourist) { this.tourist = tourist; }
    
    public Guide getGuide() { return guide; }
    public void setGuide(Guide guide) { this.guide = guide; }
    
    public Attraction getAttraction() { return attraction; }
    public void setAttraction(Attraction attraction) { 
        this.attraction = attraction;
        if (attraction != null) {
            this.totalPrice = attraction.getPrice() * (1 - discountPercentage / 100);
        }
    }
    
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    
    public LocalDate getTrekDate() { return trekDate; }
    public void setTrekDate(LocalDate trekDate) { this.trekDate = trekDate; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { 
        this.discountPercentage = discountPercentage;
        if (attraction != null) {
            this.totalPrice = attraction.getPrice() * (1 - discountPercentage / 100);
        }
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isEmergencyReported() { return emergencyReported; }
    public void setEmergencyReported(boolean emergencyReported) { this.emergencyReported = emergencyReported; }
    
    @Override
    public String toString() {
        return bookingId + " - " + (tourist != null ? tourist.getName() : "Unknown");
    }
}
