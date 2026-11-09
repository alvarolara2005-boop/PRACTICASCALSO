package es.um.calso.dojoreservation.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.um.calso.dojoreservation.domain.model.Fighter;

class ReservationDiscountServiceFindYoungestFighterTest {

	private ReservationDiscountService discountService;

	@BeforeEach
	void setUp() {
		discountService = new ReservationDiscountService();
	}

	// C1: 1-2-3-13
	@Test
	void testGivenNullFightersWhenFindYoungestThenThrowsNullPointerException() {
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);
		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(null, referenceDate));
	}

	// C2: 1-2-4-5-13
	@Test
	void testGivenNullReferenceDateWhenFindYoungestThenThrowsNullPointerException() {
		List<Fighter> fighters = Collections.emptyList();
		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(fighters, null));
	}

	// C3: 1-2-4-6-7-12-13
	@Test
	void testGivenEmptyFightersWhenFindYoungestThenResultEmpty() {
		List<Fighter> fighters = Collections.emptyList();
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isEmpty());
	}

	// C4: 1-2-4-6-7-8-9-13
	@Test
	void testGivenNullFighterInListWhenFindYoungestThenThrowsNullPointerException() {
		List<Fighter> fighters = Arrays.asList((Fighter) null);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(fighters, referenceDate));
	}

	// C5: 1-2-4-6-7-8-10-11-7-12-13
	@Test
	void testGivenSingleFighterWhenFindYoungestThenReturnsFighter() {
		Fighter fighter = new Fighter("Luchador 1", LocalDate.of(2000, 1, 1), null);
		List<Fighter> fighters = List.of(fighter);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isPresent());
		assertEquals(fighter, result.get());
	}

	// C6: 1-2-4-6-7-8-10-11-7-8-10-7-12-13
	@Test
	void testGivenMultipleFightersWhenFindYoungestThenReturnsYoungest() {
		Fighter older = new Fighter("Mayor", LocalDate.of(1990, 5, 10), null);
		Fighter youngest = new Fighter("Joven", LocalDate.of(2008, 12, 15), null);
		Fighter middle = new Fighter("Mediano", LocalDate.of(2000, 3, 20), null);

		List<Fighter> fighters = List.of(older, youngest, middle);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isPresent());
		assertEquals(youngest.name(), result.get().name());
	}

	// Comprobación extra de acuerdo con la especificación
	@Test
	void testGivenMultipleFightersSameBirthDateWhenFindYoungestThenReturnsFirst() {
		Fighter first = new Fighter("Luchador 1", LocalDate.of(2008, 12, 15), null);
		Fighter second = new Fighter("Luchador 2", LocalDate.of(2008, 12, 15), null);

		List<Fighter> fighters = List.of(first, second);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isPresent());
		assertEquals(first.name(), result.get().name());
	}

}
