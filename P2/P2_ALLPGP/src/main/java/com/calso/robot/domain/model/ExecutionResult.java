package com.calso.robot.domain.model;
import java.util.Map;
import java.util.Objects;

public record ExecutionResult(boolean executed, Map<CleaningProduct,Integer> missingProducts) {
    
    public ExecutionResult {
        Objects.requireNonNull(missingProducts, "missingProducts");
        missingProducts = Map.copyOf(missingProducts);
    }
}
