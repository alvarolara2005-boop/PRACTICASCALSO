package com.calso.robot.domain.model;

public enum CleaningProduct {
    WATER("agua"), SOAP("jabón"), WAX("cera"), GLASS_CLEANER("cristales");
    
    private final String label;
    
    CleaningProduct(String label) { 
        this.label = label; 
    }
    
    @Override
    public String toString() { 
        return label; 
    }
}
