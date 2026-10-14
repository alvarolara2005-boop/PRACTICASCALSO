package com.calso.robot.domain.valueobjects;

public final class Energy {
    private final int units;
    
    private Energy(int units) { 
        this.units = units; 
    }
    
    public static Energy of(int units) {
        if (units < 0) 
            throw new IllegalArgumentException("Energy must be positive");
        return new Energy(units);
    }
    
    public int getValue() { 
        return units; 
    }
    
    @Override
    public boolean equals(Object other) { 
        return this == other; 
    }
    
    @Override
    public int hashCode() { 
        return System.identityHashCode(this); 
    }
}
