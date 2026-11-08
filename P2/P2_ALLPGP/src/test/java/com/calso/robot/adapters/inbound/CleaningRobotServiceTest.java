package com.calso.robot.adapters.inbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calso.robot.application.usecases.StubCleaningInventory;
import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.model.ExecutionResult;
import com.calso.robot.domain.repositories.ICleaningInventory;
import com.calso.robot.domain.valueobjects.Energy;

class CleaningRobotServiceTest {

	private ICleaningInventory inventario;
	private CleaningRobotService servicio;

	@BeforeEach
	void initializeValues() {
		Map<CleaningProduct, Integer> stock = new EnumMap<>(CleaningProduct.class);
		stock.put(CleaningProduct.GLASS_CLEANER, 1);

		inventario = new StubCleaningInventory(stock);
		servicio = new CleaningRobotService(inventario);
	}

	// Constructor

	@Test
	void testGivenCleaningInventoryNullWhenCreatingCleaningRobotServiceThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new CleaningRobotService(null));
	}

	// Metodo executeProgram

	@Test
	void testGivenNullCleaningProgramWhenExecutingProgramThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> servicio.executeProgram(null));
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenResultExecutedIsTrue() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 1));

		ExecutionResult resultado = servicio.executeProgram(programa);

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenResultMissingProductsIsEmpty() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 1));

		ExecutionResult resultado = servicio.executeProgram(programa);

		assertTrue(resultado.missingProducts().isEmpty());
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenResultExecutedIsFalse() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 2));

		ExecutionResult resultado = servicio.executeProgram(programa);

		assertFalse(resultado.executed());
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenResultMissingProductGlassCleaner1() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 2));

		ExecutionResult resultado = servicio.executeProgram(programa);

		assertEquals(Map.of(CleaningProduct.GLASS_CLEANER, 1), resultado.missingProducts());
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenGlassCleanerStockRemains1() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 2));

		servicio.executeProgram(programa);

		assertEquals(1, inventario.available(CleaningProduct.GLASS_CLEANER));
	}

	@Test
	void testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenGlassCleanerStockBecomes0() {
		CleaningProgram programa = new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.GLASS_CLEANER, 1));

		servicio.executeProgram(programa);

		assertEquals(0, inventario.available(CleaningProduct.GLASS_CLEANER));
	}

}
