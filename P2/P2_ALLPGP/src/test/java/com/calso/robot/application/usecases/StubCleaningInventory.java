package com.calso.robot.application.usecases;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.repositories.ICleaningInventory;

/**
 * Suplantador de inventario para pruebas.
 */
public final class StubCleaningInventory implements ICleaningInventory {

	private Map<CleaningProduct, Integer> stock;

	public StubCleaningInventory(Map<CleaningProduct, Integer> initialStock) {
		// Comprobar que el mapa pasado como argumento no tiene valor nulo

		Objects.requireNonNull(initialStock, "El mapa initialStock no puede tener valor nulo");

		// Validar las cantidades comprobando que ninguna de ellas tenga valor menor que
		// cero

		boolean claveNoValida = initialStock.keySet().stream().anyMatch(n -> n == null);

		boolean valorNoValido = initialStock.values().stream().anyMatch(n -> n == null || n < 0);

		if (claveNoValida || valorNoValido) {
			throw new IllegalArgumentException("Un producto de limpieza no puede tener una cantidad menor que cero");
		}

		// Guardar el mapa recibido en el mapa de la clase

		stock = new EnumMap<>(CleaningProduct.class);
		stock.putAll(initialStock);

		// En caso de que falte algun producto de limpieza en el mapa lo añadimos y
		// ponemos 0 como cantidad

		for (CleaningProduct producto : CleaningProduct.values()) {
			if (!stock.containsKey(producto)) {
				stock.put(producto, 0);
			}
		}
	}

	@Override
	public int available(CleaningProduct product) {
		Objects.requireNonNull(product, "El valor de product no puede ser nulo");

		return stock.get(product);
	}

	@Override
	public void consume(CleaningProduct product, int quantity) {
		// Validar y descontar sin permitir existencias negativas.

		Objects.requireNonNull(product, "El valor de product no puede ser nulo");

		if (quantity < 0) {
			throw new IllegalArgumentException("La cantidad a restar de un producto no puede ser menor que cero");
		}

		// Obtener la cantidad de existencia del producto indicado

		Integer existenciaActual = stock.get(product);

		// Comprobar que no se esta restando una cantidad mayor que la disponible de
		// producto

		if (quantity > existenciaActual) {
			throw new IllegalArgumentException(
					"No puede restarse una cantidad mayor que la de la existencia actual de un producto");
		}

		// Restar la cantidad indicada a la existencia del producto

		stock.put(product, existenciaActual - quantity);
	}
}
