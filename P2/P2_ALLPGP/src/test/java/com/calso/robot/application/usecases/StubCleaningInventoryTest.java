package com.calso.robot.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calso.robot.domain.model.CleaningProduct;

class StubCleaningInventoryTest {

	private Map<CleaningProduct, Integer> stock;
	private StubCleaningInventory inventory;

	@BeforeEach
	void initializeStockAndInventory() {
		stock = new EnumMap<>(CleaningProduct.class);
		stock.put(CleaningProduct.GLASS_CLEANER, 1);
		inventory = new StubCleaningInventory(stock);
	}

	// Constructor

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
	void testGivenSoap0WhenCreatingNewStubCleaningInventoryThenDoesNotThrowAnyException() {
		stock.put(CleaningProduct.SOAP, 0);

		assertDoesNotThrow(() -> new StubCleaningInventory(stock));
	}

	@Test
	void testGivenSoap1WhenCreatingNewStubCleaningInventoryThenDoesNotThrowAnyException() {
		stock.put(CleaningProduct.SOAP, 1);

		assertDoesNotThrow(() -> new StubCleaningInventory(stock));
	}

	// Metodo available

	@Test
	void testGivenNullCleaningProductWhenCheckingAvailableProductThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> inventory.available(null));
	}

	@Test
	void testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenCheckingSoapQuantityThenResturns0() {
		assertEquals(0, inventory.available(CleaningProduct.SOAP));
	}

	@Test
	void testGivenInventoryWithGlassCleaner1WhenCheckingGlassCleanerQuantityThenReturns1() {
		assertEquals(1, inventory.available(CleaningProduct.GLASS_CLEANER));
	}

	// Metodo consume

	@Test
	void testGivenNullCleaningProductWhenConsumingProductThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> inventory.consume(null, 1));
	}

	@Test
	void testGivenQuantityLessThan0WhenConsumingCleaningProductThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> inventory.consume(CleaningProduct.GLASS_CLEANER, -1));
	}

	@Test
	void testGivenGlassCleanerQuantity1WhenConsumingGlassCleanerQuantity2ThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> inventory.consume(CleaningProduct.GLASS_CLEANER, 2));
	}

	@Test
	void testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenConsumingQuantity0ThenDoesNotThrowAnyException() {
		assertDoesNotThrow(() -> inventory.consume(CleaningProduct.SOAP, 0));
	}

	@Test
	void testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenConsumingQuantity0ThenQuantityStays0() {
		int anterior = inventory.available(CleaningProduct.GLASS_CLEANER);
		inventory.consume(CleaningProduct.GLASS_CLEANER, 0);
		int actual = inventory.available(CleaningProduct.GLASS_CLEANER);

		assertEquals(anterior, actual);
	}

	@Test
	void testGivenGlassCleanerQuantity1WhenConsumingQuantity1ThenQuantity0() {
		inventory.consume(CleaningProduct.GLASS_CLEANER, 1);
		int actual = inventory.available(CleaningProduct.GLASS_CLEANER);

		assertEquals(0, actual);
	}

}
