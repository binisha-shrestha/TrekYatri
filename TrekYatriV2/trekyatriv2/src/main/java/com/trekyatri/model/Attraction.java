package com.trekyatri.model;

import java.io.Serializable;

public class Attraction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String attractionId;
    private String name;
    private String description;
    private String location;
    private AttractionType type;
    private DifficultyLevel difficulty;
    private int altitude; // in meters
    private double price;
    private boolean availableDuringMonsoon;
    
    public enum AttractionType {
        TREK, HERITAGE, ADVENTURE, CULTURAL, WILDLIFE
    }
    
    public enum DifficultyLevel {
        EASY, MODERATE, DIFFICULT, EXTREME
    }
    
    public Attraction() {
        this.availableDuringMonsoon = true;
    }
    
    public Attraction(String attractionId, String name, String description, String location,
                     AttractionType type, DifficultyLevel difficulty, int altitude, double price) {
        this.attractionId = attractionId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.difficulty = difficulty;
        this.altitude = altitude;
        this.price = price;
        this.availableDuringMonsoon = altitude < 3000; // High altitude treks not available during monsoon
    }
    
    // Getters and Setters
    public String getAttractionId() { return attractionId; }
    public void setAttractionId(String attractionId) { this.attractionId = attractionId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public AttractionType getType() { return type; }
    public void setType(AttractionType type) { this.type = type; }
    
    public DifficultyLevel getDifficulty() { return difficulty; }
    public void setDifficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; }
    
    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) { 
        this.altitude = altitude;
        this.availableDuringMonsoon = altitude < 3000;
    }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public boolean isAvailableDuringMonsoon() { return availableDuringMonsoon; }
    public void setAvailableDuringMonsoon(boolean availableDuringMonsoon) { 
        this.availableDuringMonsoon = availableDuringMonsoon; 
    }
    
    public boolean isHighAltitude() {
        return altitude > 3000;
    }
    
    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
