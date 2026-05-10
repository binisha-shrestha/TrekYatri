package com.trekyatri.util;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class FestivalManager {
    private static FestivalManager instance;
    private Map<String, FestivalInfo> festivals;
    
    private FestivalManager() {
        initializeFestivals();
    }
    
    public static FestivalManager getInstance() {
        if (instance == null) {
            instance = new FestivalManager();
        }
        return instance;
    }
    
    private void initializeFestivals() {
        festivals = new HashMap<>();
        
        // Add major Nepali festivals (approximate dates)
        festivals.put("Dashain", new FestivalInfo("Dashain", Month.OCTOBER, 1, 15, 15.0));
        festivals.put("Tihar", new FestivalInfo("Tihar", Month.NOVEMBER, 1, 5, 10.0));
        festivals.put("Holi", new FestivalInfo("Holi", Month.MARCH, 15, 2, 8.0));
        festivals.put("Buddha Jayanti", new FestivalInfo("Buddha Jayanti", Month.MAY, 15, 1, 5.0));
    }
    
    public FestivalInfo getCurrentFestival() {
        LocalDate today = LocalDate.now();
        
        for (FestivalInfo festival : festivals.values()) {
            if (festival.isActive(today)) {
                return festival;
            }
        }
        return null;
    }
    
    public double getFestivalDiscount() {
        FestivalInfo currentFestival = getCurrentFestival();
        return currentFestival != null ? currentFestival.getDiscountPercentage() : 0.0;
    }
    
    public boolean isFestivalPeriod() {
        return getCurrentFestival() != null;
    }
    
    public static class FestivalInfo {
        private String name;
        private Month month;
        private int startDay;
        private int duration;
        private double discountPercentage;
        
        public FestivalInfo(String name, Month month, int startDay, int duration, double discountPercentage) {
            this.name = name;
            this.month = month;
            this.startDay = startDay;
            this.duration = duration;
            this.discountPercentage = discountPercentage;
        }
        
        public boolean isActive(LocalDate date) {
            if (date.getMonth() != month) {
                return false;
            }
            
            int day = date.getDayOfMonth();
            return day >= startDay && day < (startDay + duration);
        }
        
        // Getters
        public String getName() { return name; }
        public Month getMonth() { return month; }
        public int getStartDay() { return startDay; }
        public int getDuration() { return duration; }
        public double getDiscountPercentage() { return discountPercentage; }
    }
}
