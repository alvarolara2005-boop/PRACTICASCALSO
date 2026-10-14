package com.calso.robot.application.ports.inbound;
import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.model.ExecutionResult;

public interface IExecuteCleaningProgram {
    ExecutionResult execute(CleaningProgram program);
}
