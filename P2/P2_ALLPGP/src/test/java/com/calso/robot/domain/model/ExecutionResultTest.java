package com.calso.robot.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExecutionResultTest {

	private Map<CleaningProduct, Integer> stock;
	private Map<CleaningProduct, Integer> requisitos;

	@BeforeEach
	void initializeValues() {
		stock = new EnumMap<>(CleaningProduct.class);
		stock.put(CleaningProduct.GLASS_CLEANER, 1);

		requisitos = new EnumMap<>(CleaningProduct.class);
		requisitos.put(CleaningProduct.GLASS_CLEANER, 1);
	}

	// Constructor

	@Test
	void testGivenNullMissingProductsWhenCreatingExecutionResultThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new ExecutionResult(false, null));
	}

	@Test
	void testGivenExecutedTrueAndEmptyMissingProductsWhenCreatingResultThenExecutedIsTrue() {
		ExecutionResult resultado = new ExecutionResult(true, Map.of());

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenExecutedTrueAndEmptyMissingProductsWhenCreatingResultThenMissingProductsIsEmpty() {
		ExecutionResult resultado = new ExecutionResult(true, Map.of());

		assertTrue(resultado.missingProducts().isEmpty());
	}

	@Test
	void testGivenExecutedTrueAndMissingProductsWater5Wax3WhenCreatingResultThenThrowsIllegalArgumentException() {
		Map<CleaningProduct, Integer> mapa = Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3);

		assertThrows(IllegalArgumentException.class, () -> new ExecutionResult(true, mapa));
	}

	@Test
	void testGivenExecutedFalseAndEmptyMissingProductsWhenCreatingResultThenExecutedIsFalse() {
		ExecutionResult resultado = new ExecutionResult(false, Map.of());

		assertFalse(resultado.executed());
	}

	@Test
	void testGivenExecutedFalseAndEmptyMissingProductsWhenCreatingResultThenMissingProductsIsEmpty() {
		ExecutionResult resultado = new ExecutionResult(false, Map.of());

		assertTrue(resultado.missingProducts().isEmpty());
	}

	@Test
	void testGivenExecutedFalseAndWater2AndSoap1MissingWhenCreatingResultThenExecutedIsFalse() {
		ExecutionResult resultado = new ExecutionResult(false,
				Map.of(CleaningProduct.WATER, 2, CleaningProduct.SOAP, 1));

		assertFalse(resultado.executed());
	}

	@Test
	void testGivenExecutedFalseAndWater2AndSoap1MissingWhenCreatingResultThenMissingProductsDoesNotChange() {
		ExecutionResult resultado = new ExecutionResult(false,
				Map.of(CleaningProduct.WATER, 2, CleaningProduct.SOAP, 1));

		assertEquals(Map.of(CleaningProduct.WATER, 2, CleaningProduct.SOAP, 1), resultado.missingProducts());
	}

	@Test
	void testGivenResultWithMissingProductsWater2AndSoap1WhenPuttingGlassCleaner1InReturnedMapThenThrowsUnsupportedOperationException() {
		ExecutionResult resultado = new ExecutionResult(false,
				Map.of(CleaningProduct.WATER, 2, CleaningProduct.SOAP, 1));

		Map<CleaningProduct, Integer> faltantes = resultado.missingProducts();

		assertThrows(UnsupportedOperationException.class, () -> faltantes.put(CleaningProduct.GLASS_CLEANER, 1));
	}

}
