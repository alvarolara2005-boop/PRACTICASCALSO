package com.calso.robot.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calso.robot.domain.model.CleaningProduct;

class StubCleaningInventoryTest {
	
	private Map<CleaningProduct, Integer> stock;
	
	@BeforeEach
	void crearMapa() {
		stock = new EnumMap<>(CleaningProduct.class);
	}

	@Test
	void testGivenNullMapWhenCreatingNewStubCleaningInventoryThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new StubCleaningInventory(null));
	}
	
	@Test
	void testGivenValueMinus1WhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException() {
		stock.put(CleaningProduct.GLASS_CLEANER, -1);
		
		assertThrows(IllegalArgumentException.class, () -> new StubCleaningInventory(stock));
	}
	
	@Test
	void testGivenKeyNullWhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException() {
		Map<CleaningProduct, Integer> stockConNull = new HashMap<>();
		stockConNull.put(null, 5);
		
		assertThrows(IllegalArgumentException.class, () -> new StubCleaningInventory(stockConNull));
	}
	
	@Test
	void testGivenValueNullWhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException() {
		stock.put(CleaningProduct.WAX, null);
		
		assertThrows(IllegalArgumentException.class, () -> new StubCleaningInventory(stock));
	}
	
	@Test
	void testGivenCorrectMapWhenCreatingNewStubCleaningInventoryThenDoesNotThrowAnyException() {
		stock.put(CleaningProduct.SOAP, 12);
		
		assertDoesNotThrow(() -> new StubCleaningInventory(stock));
	}

}
