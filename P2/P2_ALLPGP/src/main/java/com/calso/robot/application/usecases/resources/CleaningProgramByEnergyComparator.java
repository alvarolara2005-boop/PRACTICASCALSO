package com.calso.robot.application.usecases.resources;
import java.util.Comparator;
import com.calso.robot.domain.model.CleaningProgram;

public final class CleaningProgramByEnergyComparator implements Comparator<CleaningProgram> {
    
    @Override
    public int compare(CleaningProgram first, CleaningProgram second) {
        int byEnergy = Integer.compare(second.getEnergy().getValue(), first.getEnergy().getValue());
        return byEnergy != 0 ? byEnergy : first.getId().compareTo(second.getId());
    }
}
