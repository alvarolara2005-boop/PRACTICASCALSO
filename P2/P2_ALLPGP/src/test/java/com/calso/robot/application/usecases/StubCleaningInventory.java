package com.calso.robot.application.usecases;

import java.util.Map;
import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.repositories.ICleaningInventory;

/**
 * Suplantador de inventario para pruebas. IMPLEMENTACIÓN PENDIENTE.
 * La implementación debe mantenerse exclusivamente bajo src/test/java.
 */
public final class StubCleaningInventory implements ICleaningInventory {

    public StubCleaningInventory(Map<CleaningProduct, Integer> initialStock) {
        // TODO: validar cantidades, copiar existencias y conservar estado propio.
    }

    @Override
    public int available(CleaningProduct product) {
        // TODO: devolver existencia actual o cero si el producto no está presente.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public void consume(CleaningProduct product, int quantity) {
        // TODO: validar y descontar sin permitir existencias negativas.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
