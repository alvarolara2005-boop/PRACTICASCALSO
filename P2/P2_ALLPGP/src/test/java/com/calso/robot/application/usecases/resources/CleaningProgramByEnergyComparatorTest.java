package com.calso.robot.application.usecases.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.calso.robot.domain.model.CleaningProgram;
import com.calso.robot.domain.valueobjects.Energy;

class CleaningProgramByEnergyComparatorTest {

	private CleaningProgram program(String id, int energy) {
		return new CleaningProgram(id, "Programa " + id, Energy.of(energy), Map.of());
	}

	@Test
	void testGivenEnergy10AndEnergy20WhenComparingThenReturnsNegative() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		assertTrue(comparator.compare(program("A", 10), program("B", 20)) < 0);
	}

	@Test
	void testGivenEnergy20AndEnergy10WhenComparingThenReturnsPositive() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		assertTrue(comparator.compare(program("A", 20), program("B", 10)) > 0);
	}

	@Test
	void testGivenSameEnergyAndSameIdWhenComparingThenReturnsZero() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		assertEquals(0, comparator.compare(program("A", 10), program("A", 10)));
	}

	@Test
	void testGivenSameEnergyAndIdAAndIdBWhenComparingThenOrdersByIdAscending() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		assertTrue(comparator.compare(program("A", 10), program("B", 10)) < 0);
		assertTrue(comparator.compare(program("B", 10), program("A", 10)) > 0);
	}

	@Test
	void testGivenProgramsWithDifferentEnergyWhenSortingThenOrdersByEnergyAscending() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		List<CleaningProgram> programs = new ArrayList<>(List.of(program("A", 30), program("B", 10), program("C", 20)));

		programs.sort(comparator);

		assertEquals("B", programs.get(0).getId());
		assertEquals("C", programs.get(1).getId());
		assertEquals("A", programs.get(2).getId());
	}

	@Test
	void testGivenProgramsWithSameEnergyWhenSortingThenOrdersByIdAscending() {
		CleaningProgramByEnergyComparator comparator = new CleaningProgramByEnergyComparator();
		List<CleaningProgram> programs = new ArrayList<>(List.of(program("C", 10), program("A", 10), program("B", 10)));

		programs.sort(comparator);

		assertEquals("A", programs.get(0).getId());
		assertEquals("B", programs.get(1).getId());
		assertEquals("C", programs.get(2).getId());
	}
}