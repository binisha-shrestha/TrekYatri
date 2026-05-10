package com.trekyatri.util;

import java.util.Random;

public class WeatherService {
    private static WeatherService instance;
    private Random random;
    
    private WeatherService() {
        this.random = new Random();
    }
    
    public static WeatherService getInstance() {
        if (instance == null) {
            instance = new WeatherService();
        }
        return instance;
    }
    
    public WeatherInfo getWeatherInfo(String location) {
        // Simulate weather data for different locations in Nepal
        WeatherInfo weather = new WeatherInfo();
        weather.setLocation(location);
        
        // Simulate temperature based on location
        if (location.toLowerCase().contains("everest") || location.toLowerCase().contains("base camp")) {
            weather.setTemperature(-10 + random.nextInt(20)); // -10 to 10°C
            weather.setCondition("Snow");
        } else if (location.toLowerCase().contains("kathmandu")) {
            weather.setTemperature(15 + random.nextInt(15)); // 15 to 30°C
            weather.setCondition(random.nextBoolean() ? "Sunny" : "Cloudy");
        } else {
            weather.setTemperature(10 + random.nextInt(20)); // 10 to 30°C
            weather.setCondition(getRandomCondition());
        }
        
        weather.setHumidity(40 + random.nextInt(40)); // 40-80%
        weather.setWindSpeed(5 + random.nextInt(15)); // 5-20 km/h
        
        return weather;
    }
    
    private String getRandomCondition() {
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Foggy", "Clear"};
        return conditions[random.nextInt(conditions.length)];
    }
    
    public static class WeatherInfo {
        private String location;
        private int temperature;
        private String condition;
        private int humidity;
        private int windSpeed;
        
        // Getters and Setters
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public int getTemperature() { return temperature; }
        public void setTemperature(int temperature) { this.temperature = temperature; }
        
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        
        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }
        
        public int getWindSpeed() { return windSpeed; }
        public void setWindSpeed(int windSpeed) { this.windSpeed = windSpeed; }
        
        @Override
        public String toString() {
            return String.format("%s: %d°C, %s, Humidity: %d%%, Wind: %d km/h", 
                               location, temperature, condition, humidity, windSpeed);
        }
    }
}
