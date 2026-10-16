package com.calso.robot.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnergyTest {

	@Test
	void testGivenEnergy10WhenCreatingEnergyThenStoresValue10() {
		Energy energy = Energy.of(10);
		assertEquals(10, energy.getValue());
	}

	@Test
	void testGivenEnergy1WhenCreatingEnergyThenStoresValue1() {
		Energy energy = Energy.of(1);
		assertEquals(1, energy.getValue());
	}

	@Test
	void testGivenEnergy0WhenCreatingEnergyThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> Energy.of(0));
	}

	@Test
	void testGivenEnergyMinus1WhenCreatingEnergyThenThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> Energy.of(-1));
	}

	@Test
	void testGivenTwoEnergyValues10WhenComparingThenTheyAreEqual() {
		Energy first = Energy.of(10);
		Energy second = Energy.of(10);
		assertEquals(first, second);
	}

	@Test
	void testGivenTwoEnergyValues10WhenComparingThenHashCodesAreEqual() {
		Energy first = Energy.of(10);
		Energy second = Energy.of(10);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void testGivenEnergy10AndEnergy20WhenComparingThenTheyAreNotEqual() {
		Energy first = Energy.of(10);
		Energy second = Energy.of(20);
		assertNotEquals(first, second);
	}
}