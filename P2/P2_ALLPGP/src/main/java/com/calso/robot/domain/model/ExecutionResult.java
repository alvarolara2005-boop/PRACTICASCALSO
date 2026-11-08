package com.calso.robot.domain.model;

import java.util.Map;
import java.util.Objects;

public record ExecutionResult(boolean executed, Map<CleaningProduct, Integer> missingProducts) {

	public ExecutionResult {
		Objects.requireNonNull(missingProducts, "missingProducts");

		// Según la especificación "executed == true implica que missingProducts está
		// vacío"

		if (executed && !missingProducts.isEmpty()) {
			throw new IllegalArgumentException("Un resultado ejecutado no puede tener productos faltantes");
		}

		missingProducts = Map.copyOf(missingProducts);
	}
}
