package com.trekyatri.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle resourceBundle;
    private String currentLanguage;
    
    private LanguageManager() {
        setCurrentLanguage("en");
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    public void setCurrentLanguage(String language) {
        this.currentLanguage = language;
        Locale locale = "ne".equals(language) ? new Locale("ne", "NP") : Locale.ENGLISH;
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return key; // Return key if translation not found
        }
    }
    
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
