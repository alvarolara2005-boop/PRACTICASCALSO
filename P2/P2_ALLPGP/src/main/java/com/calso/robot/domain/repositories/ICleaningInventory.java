package com.calso.robot.domain.repositories;
import com.calso.robot.domain.model.CleaningProduct;

public interface ICleaningInventory {
    int available(CleaningProduct product);
    void consume(CleaningProduct product, int quantity);
}
