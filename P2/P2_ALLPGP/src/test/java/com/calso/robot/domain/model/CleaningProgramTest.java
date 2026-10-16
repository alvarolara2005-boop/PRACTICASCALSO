package com.calso.robot.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.calso.robot.domain.valueobjects.Energy;

class CleaningProgramTest {

	private Map<CleaningProduct, Integer> validRequirements() {
		Map<CleaningProduct, Integer> req = new EnumMap<>(CleaningProduct.class);
		req.put(CleaningProduct.WATER, 2);
		return req;
	}

	@Test
	void testGivenValidDataWhenCreatingProgramThenPropertiesAreStored() {
		CleaningProgram program = new CleaningProgram("P1", "Programa 1", Energy.of(10), validRequirements());

		assertEquals("P1", program.getId());
		assertEquals("Programa 1", program.getName());
		assertEquals(Energy.of(10), program.getEnergy());
		assertEquals(validRequirements(), program.getRequirements());
	}

	@Test
	void testGivenNullIdWhenCreatingProgramThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> new CleaningProgram(null, "Programa", Energy.of(10), validRequirements()));
	}

	@Test
	void testGivenBlankIdWhenCreatingProgramThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> new CleaningProgram("   ", "Programa", Energy.of(10), validRequirements()));
	}

	@Test
	void testGivenNullNameWhenCreatingProgramThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> new CleaningProgram("P1", null, Energy.of(10), validRequirements()));
	}

	@Test
	void testGivenBlankNameWhenCreatingProgramThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> new CleaningProgram("P1", "   ", Energy.of(10), validRequirements()));
	}

	@Test
	void testGivenNullEnergyWhenCreatingProgramThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class,
				() -> new CleaningProgram("P1", "Programa", null, validRequirements()));
	}

	@Test
	void testGivenNullRequirementsWhenCreatingProgramThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new CleaningProgram("P1", "Programa", Energy.of(10), null));
	}

	@Test
	void testGivenNegativeQuantityWhenCreatingProgramThenThrowsIllegalArgumentException() {
		Map<CleaningProduct, Integer> req = new EnumMap<>(CleaningProduct.class);
		req.put(CleaningProduct.WATER, -1);
		assertThrows(IllegalArgumentException.class, () -> new CleaningProgram("P1", "Programa", Energy.of(10), req));
	}

	@Test
	void testGivenEmptyRequirementsWhenCreatingProgramThenProgramIsCreated() {
		CleaningProgram program = new CleaningProgram("P1", "Programa", Energy.of(10), Map.of());
		assertTrue(program.getRequirements().isEmpty());
	}

	@Test
	void testGivenTwoProgramsWithSameIdAndDifferentNameWhenComparingThenTheyAreEqual() {
		CleaningProgram first = new CleaningProgram("P1", "Nombre A", Energy.of(10), validRequirements());
		CleaningProgram second = new CleaningProgram("P1", "Nombre B", Energy.of(20), Map.of());

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void testGivenTwoProgramsWithSameIdAndDifferentRequirementsWhenComparingThenTheyAreEqual() {
		Map<CleaningProduct, Integer> otherReq = new EnumMap<>(CleaningProduct.class);
		otherReq.put(CleaningProduct.SOAP, 5);

		CleaningProgram first = new CleaningProgram("P1", "Nombre", Energy.of(10), validRequirements());
		CleaningProgram second = new CleaningProgram("P1", "Nombre", Energy.of(10), otherReq);

		assertEquals(first, second);
	}

	@Test
	void testGivenTwoProgramsWithDifferentIdWhenComparingThenTheyAreNotEqual() {
		CleaningProgram first = new CleaningProgram("P1", "Nombre", Energy.of(10), validRequirements());
		CleaningProgram second = new CleaningProgram("P2", "Nombre", Energy.of(10), validRequirements());

		assertNotEquals(first, second);
	}

	@Test
	void testGivenProgramWhenModifyingOriginalMapThenProgramRequirementsAreUnchanged() {
		Map<CleaningProduct, Integer> original = new EnumMap<>(CleaningProduct.class);
		original.put(CleaningProduct.WATER, 2);

		CleaningProgram program = new CleaningProgram("P1", "Programa", Energy.of(10), original);

		original.put(CleaningProduct.WATER, 99);

		assertEquals(2, program.getRequirements().get(CleaningProduct.WATER));
	}

	@Test
	void testGivenProgramWhenGettingRequirementsThenMapIsUnmodifiable() {
		CleaningProgram program = new CleaningProgram("P1", "Programa", Energy.of(10), validRequirements());

		assertThrows(UnsupportedOperationException.class,
				() -> program.getRequirements().put(CleaningProduct.WATER, 5));
	}
}