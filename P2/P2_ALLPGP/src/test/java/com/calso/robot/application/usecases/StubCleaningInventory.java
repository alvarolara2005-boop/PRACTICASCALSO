package com.calso.robot.application.usecases;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.repositories.ICleaningInventory;

/**
 * Suplantador de inventario para pruebas. IMPLEMENTACIÓN PENDIENTE. La
 * implementación debe mantenerse exclusivamente bajo src/test/java.
 */
public final class StubCleaningInventory implements ICleaningInventory {

	private Map<CleaningProduct, Integer> stock;

	public StubCleaningInventory(Map<CleaningProduct, Integer> initialStock) {
		// Comprobar que el mapa pasado como argumento no tiene valor nulo
		Objects.requireNonNull(initialStock, "El mapa initialStock no puede tener valor nulo");

		// Validar las cantidades comprobando que ninguna de ellas tenga valor menor que
		// cero
		boolean claveNoValida = initialStock.keySet().stream().allMatch(n -> n == null);
		
		boolean valorNoValido = initialStock.values().stream().anyMatch(n -> n == null || n < 0);
		
		if (claveNoValida || valorNoValido) {
			throw new IllegalArgumentException("Un producto de limpieza no puede tener una cantidad menor que cero");
		}

		// Guardar el mapa recibido en el mapa de la clase
		stock = new EnumMap<>(CleaningProduct.class);
		stock.putAll(initialStock);
	}

	@Override
	public int available(CleaningProduct product) {
		Objects.requireNonNull(product, "El valor de product no puede ser nulo");
		
		// Devolver existencia actual o cero si el producto no está presente.
		Integer existencia = stock.get(product);

		// Si el producto indicado no esta en el mapa como clave asignamos valor cero a
		// la existencia
		if (existencia == null) {
			existencia = 0;
		}

		return existencia;
	}

	@Override
	public void consume(CleaningProduct product, int quantity) {
		// Validar y descontar sin permitir existencias negativas.

		Objects.requireNonNull(product, "El valor de product no puede ser nulo");
		Objects.requireNonNull(quantity, "El valor de quantity no puede ser nulo");

		if (quantity < 0) {
			throw new IllegalArgumentException("La cantidad a restar de un producto no puede ser menor que cero");
		}

		// Obtener la cantidad de existencia del producto indicado
		Integer existenciaActual = stock.get(product);

		if (quantity > existenciaActual) {
			throw new IllegalArgumentException(
					"No puede restarse una cantidad mayor que la de la existencia actual de un producto");
		}

		// Restar la cantidad indicada a la existencia del producto
		stock.put(product, existenciaActual - quantity);
	}
}
