package com.calso.robot.adapters.inbound;
import java.util.Objects;
import com.calso.robot.application.ports.inbound.IExecuteCleaningProgram;
import com.calso.robot.application.usecases.ExecuteCleaningProgramUseCase;
import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.model.ExecutionResult;
import com.calso.robot.domain.repositories.ICleaningInventory;

public final class CleaningRobotService {
    private final IExecuteCleaningProgram useCase;
    
    public CleaningRobotService(ICleaningInventory inventory) {
        this.useCase = new ExecuteCleaningProgramUseCase(Objects.requireNonNull(inventory));
    }
    
    public ExecutionResult executeProgram(CleaningProgram program) { 
        return useCase.execute(program); 
    }
}
