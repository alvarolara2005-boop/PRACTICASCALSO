package com.calso.robot.application.usecases;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import com.calso.robot.application.ports.inbound.IExecuteCleaningProgram;
import com.calso.robot.domain.model.CleaningProduct;
import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.model.ExecutionResult;
import com.calso.robot.domain.repositories.ICleaningInventory;

public final class ExecuteCleaningProgramUseCase implements IExecuteCleaningProgram {

	private final ICleaningInventory inventory;

	public ExecuteCleaningProgramUseCase(ICleaningInventory inventory) {
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	@Override
	public ExecutionResult execute(CleaningProgram program) {
		Objects.requireNonNull(program, "El valor de program no puede ser nulo");
		Map<CleaningProduct, Integer> missing = new EnumMap<>(CleaningProduct.class);

		for (var requirement : program.getRequirements().entrySet()) {
			CleaningProduct product = requirement.getKey(); // producto actual
			int required = requirement.getValue(); // cantidad requerida

			int available = inventory.available(product); // cantidad disponible

			if (required > 0 && available < required) {
				missing.put(product, required - available);
			}
		}

		// En caso de que no falte ningún producto descontar cantidades del inventario.
		// En otro caso no descontar ninguna cantidad

		if (missing.isEmpty()) {
			for (var requirement : program.getRequirements().entrySet()) {
				CleaningProduct product = requirement.getKey(); // producto actual
				int required = requirement.getValue(); // cantidad requerida

				inventory.consume(product, required);
			}
		}

		// Devolver el resultado

		return new ExecutionResult(missing.isEmpty(), missing);
	}
}
