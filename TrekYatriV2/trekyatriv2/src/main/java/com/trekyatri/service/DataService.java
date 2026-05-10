package com.trekyatri.service;

import com.trekyatri.model.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataService {
    private static DataService instance;
    private Map<String, Tourist> tourists;
    private Map<String, Guide> guides;
    private Map<String, Attraction> attractions;
    private Map<String, Booking> bookings;
    
    private static final String DATA_DIR = "data/";
    private static final String TOURISTS_FILE = DATA_DIR + "tourists.dat";
    private static final String GUIDES_FILE = DATA_DIR + "guides.dat";
    private static final String ATTRACTIONS_FILE = DATA_DIR + "attractions.dat";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.dat";
    
    private DataService() {
        tourists = new ConcurrentHashMap<>();
        guides = new ConcurrentHashMap<>();
        attractions = new ConcurrentHashMap<>();
        bookings = new ConcurrentHashMap<>();
        
        createDataDirectory();
        loadAllData();
        initializeSampleData();
    }
    
    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }
    
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private void initializeSampleData() {
        if (attractions.isEmpty()) {
            // Add sample attractions
            addAttraction(new Attraction("ATT001", "Everest Base Camp", 
                "World's highest mountain base camp trek", "Solukhumbu",
                Attraction.AttractionType.TREK, Attraction.DifficultyLevel.EXTREME, 5364, 2500.0));
            
            addAttraction(new Attraction("ATT002", "Annapurna Base Camp", 
                "Beautiful mountain sanctuary trek", "Annapurna Region",
                Attraction.AttractionType.TREK, Attraction.DifficultyLevel.MODERATE, 4130, 1200.0));
            
            addAttraction(new Attraction("ATT003", "Kathmandu Durbar Square", 
                "Historic palace complex", "Kathmandu",
                Attraction.AttractionType.HERITAGE, Attraction.DifficultyLevel.EASY, 1400, 50.0));
        }
        
        if (guides.isEmpty()) {
            // Add sample guides
            List<String> languages1 = Arrays.asList("English", "Nepali", "Hindi");
            addGuide(new Guide("GD001", "Ram Bahadur Sherpa", languages1, 15, 
                "+977-9841234567", "ram.sherpa@email.com", "High Altitude Trekking"));
            
            List<String> languages2 = Arrays.asList("English", "Nepali", "Japanese");
            addGuide(new Guide("GD002", "Sita Gurung", languages2, 8, 
                "+977-9851234567", "sita.gurung@email.com", "Cultural Tours"));
        }
    }
    
    // Tourist operations
    public void addTourist(Tourist tourist) {
        tourists.put(tourist.getTouristId(), tourist);
        saveTourists();
    }
    
    public void updateTourist(Tourist tourist) {
        tourists.put(tourist.getTouristId(), tourist);
        saveTourists();
    }
    
    public void deleteTourist(String touristId) {
        tourists.remove(touristId);
        saveTourists();
    }
    
    public Tourist getTourist(String touristId) {
        return tourists.get(touristId);
    }
    
    public Collection<Tourist> getAllTourists() {
        return tourists.values();
    }
    
    // Guide operations
    public void addGuide(Guide guide) {
        guides.put(guide.getGuideId(), guide);
        saveGuides();
    }
    
    public void updateGuide(Guide guide) {
        guides.put(guide.getGuideId(), guide);
        saveGuides();
    }
    
    public void deleteGuide(String guideId) {
        guides.remove(guideId);
        saveGuides();
    }
    
    public Guide getGuide(String guideId) {
        return guides.get(guideId);
    }
    
    public Collection<Guide> getAllGuides() {
        return guides.values();
    }
    
    // Attraction operations
    public void addAttraction(Attraction attraction) {
        attractions.put(attraction.getAttractionId(), attraction);
        saveAttractions();
    }
    
    public void updateAttraction(Attraction attraction) {
        attractions.put(attraction.getAttractionId(), attraction);
        saveAttractions();
    }
    
    public void deleteAttraction(String attractionId) {
        attractions.remove(attractionId);
        saveAttractions();
    }
    
    public Attraction getAttraction(String attractionId) {
        return attractions.get(attractionId);
    }
    
    public Collection<Attraction> getAllAttractions() {
        return attractions.values();
    }
    
    // Booking operations
    public void addBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveBookings();
    }
    
    public void updateBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveBookings();
    }
    
    public void deleteBooking(String bookingId) {
        bookings.remove(bookingId);
        saveBookings();
    }
    
    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
    
    public Collection<Booking> getAllBookings() {
        return bookings.values();
    }
    
    // File I/O operations
    private void loadAllData() {
        loadTourists();
        loadGuides();
        loadAttractions();
        loadBookings();
    }
    
    @SuppressWarnings("unchecked")
    private void loadTourists() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TOURISTS_FILE))) {
            tourists = (Map<String, Tourist>) ois.readObject();
        } catch (Exception e) {
            tourists = new ConcurrentHashMap<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadGuides() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUIDES_FILE))) {
            guides = (Map<String, Guide>) ois.readObject();
        } catch (Exception e) {
            guides = new ConcurrentHashMap<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadAttractions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ATTRACTIONS_FILE))) {
            attractions = (Map<String, Attraction>) ois.readObject();
        } catch (Exception e) {
            attractions = new ConcurrentHashMap<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {
            bookings = (Map<String, Booking>) ois.readObject();
        } catch (Exception e) {
            bookings = new ConcurrentHashMap<>();
        }
    }
    
    private void saveTourists() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TOURISTS_FILE))) {
            oos.writeObject(tourists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveGuides() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUIDES_FILE))) {
            oos.writeObject(guides);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveAttractions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ATTRACTIONS_FILE))) {
            oos.writeObject(attractions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveAllData() {
        saveTourists();
        saveGuides();
        saveAttractions();
        saveBookings();
    }
}
