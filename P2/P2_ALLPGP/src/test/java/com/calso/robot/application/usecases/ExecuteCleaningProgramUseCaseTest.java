package com.calso.robot.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.model.ExecutionResult;
import com.calso.robot.domain.repositories.ICleaningInventory;
import com.calso.robot.domain.valueobjects.Energy;

class ExecuteCleaningProgramUseCaseTest {

	// Constructor

	@Test
	void testGivenNullInventoryWhenCreatingCleaningProgramUseCaseThenThrowsNullPointerException() {
		// Constructor: ExecuteCleaningProgramUseCase(ICleaningInventory inventory)
		// requiere inventario no nulo.

		assertThrows(NullPointerException.class, () -> new ExecuteCleaningProgramUseCase(null));
	}

	// Metodo execute

	@Test
	void testGivenNullCleaningProgramWhenExecutingCleaningProgramThenThrowsNullPointerException() {
		// execute(CleaningProgram program) requiere programa no nulo.

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(
				new StubCleaningInventory(Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 5)));

		assertThrows(NullPointerException.class, () -> programa.execute(null));
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenExecutedIsFalse() {
		// Antes de descontar cualquier cantidad, comprueba que existen suficientes
		// unidades de todos los productos.
		// Si algún producto falta, retorna ExecutionResult(false, faltantes) y no
		// modifica ninguna existencia.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 5)));

		assertFalse(resultado.executed());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenPreservesStock() {
		// Antes de descontar cualquier cantidad, comprueba que existen suficientes
		// unidades de todos los productos.
		// Si algún producto falta, retorna ExecutionResult(false, faltantes) y no
		// modifica ninguna existencia.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 5)));

		assertEquals(5, inventario.available(CleaningProduct.WATER));
		assertEquals(3, inventario.available(CleaningProduct.WAX));
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenResultMissingProductsWax2() {
		// Antes de descontar cualquier cantidad, comprueba que existen suficientes
		// unidades de todos los productos.
		// Si algún producto falta, retorna ExecutionResult(false, faltantes) y no
		// modifica ninguna existencia.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 5)));

		assertEquals(Map.of(CleaningProduct.WAX, 2), resultado.missingProducts());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenExecutedIsTrue() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenWater0Wax0() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertEquals(0, inventario.available(CleaningProduct.WATER));
		assertEquals(0, inventario.available(CleaningProduct.WAX));
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenMissingProductsIsEmpty() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertTrue(resultado.missingProducts().isEmpty());
	}
	
	@Test
	void testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenExecutedIsTrue() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 6, CleaningProduct.WAX, 4));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenWater1Wax1() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 6, CleaningProduct.WAX, 4));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertEquals(1, inventario.available(CleaningProduct.WATER));
		assertEquals(1, inventario.available(CleaningProduct.WAX));
	}

	@Test
	void testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenMissingProductsIsEmpty() {
		// Si todos alcanzan o superan lo requerido (incluida disponibilidad exacta),
		// descuenta las cantidades indicadas y retorna ExecutionResult(true,
		// mapaVacio).

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 6, CleaningProduct.WAX, 4));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3)));

		assertTrue(resultado.missingProducts().isEmpty());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramWithoutProductRequirementsThenExecutedIsTrue() {
		// Programa sin productos: ejecución correcta sin descuentos.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10), Map.of()));

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramWithoutProductRequirementsThenStockIsPreserved() {
		// Programa sin productos: ejecución correcta sin descuentos.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10), Map.of()));

		assertEquals(5, inventario.available(CleaningProduct.WATER));
		assertEquals(3, inventario.available(CleaningProduct.WAX));
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater0AndWax0ThenExecutedIsTrue() {
		// Requisitos de cantidad cero son válidos y no producen descuentos de
		// existencias.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		ExecutionResult resultado = programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 0, CleaningProduct.WAX, 0)));

		assertTrue(resultado.executed());
	}

	@Test
	void testGivenWater5AndWax3WhenExecutingProgramRequiringWater0AndWax0ThenStockIsPreserved() {
		// Requisitos de cantidad cero son válidos y no producen descuentos de
		// existencias.

		ICleaningInventory inventario = new StubCleaningInventory(
				Map.of(CleaningProduct.WATER, 5, CleaningProduct.WAX, 3));

		ExecuteCleaningProgramUseCase programa = new ExecuteCleaningProgramUseCase(inventario);

		programa.execute(new CleaningProgram("P1", "Programa 1", Energy.of(10),
				Map.of(CleaningProduct.WATER, 0, CleaningProduct.WAX, 0)));

		assertEquals(5, inventario.available(CleaningProduct.WATER));
		assertEquals(3, inventario.available(CleaningProduct.WAX));
	}

}
