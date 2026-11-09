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
import es.um.calso.dojoreservation.domain.model.UnemploymentPeriod;

class ReservationDiscountServiceFindYoungestFighterTest {

	private ReservationDiscountService discountService;

	@BeforeEach
	void setUp() {
		discountService = new ReservationDiscountService();
	}

	@Test
	void testGivenNullFightersWhenFindYoungestThenThrowsNullPointerException() {
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);
		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(null, referenceDate));
	}

	@Test
	void testGivenNullReferenceDateWhenFindYoungestThenThrowsNullPointerException() {
		List<Fighter> fighters = Collections.emptyList();
		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(fighters, null));
	}

	@Test
	void testGivenEmptyFightersWhenFindYoungestThenResultEmpty() {
		List<Fighter> fighters = Collections.emptyList();
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isEmpty());
	}

	@Test
	void testGivenNullFighterInListWhenFindYoungestThenThrowsNullPointerException() {
		List<Fighter> fighters = Arrays.asList((Fighter) null);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		assertThrows(NullPointerException.class, () -> discountService.findYoungestFighter(fighters, referenceDate));
	}

	@Test
	void testGivenSingleFighterWhenFindYoungestThenReturnsFighter() {
		Fighter fighter = new Fighter("Luchador 1", LocalDate.of(2000, 1, 1),
				new UnemploymentPeriod(LocalDate.of(2026, 8, 2)));
		List<Fighter> fighters = List.of(fighter);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isPresent());
		assertEquals(fighter, result.get());
	}

	@Test
	void testGivenMultipleFightersWhenFindYoungestThenReturnsYoungest() {
		Fighter older = new Fighter("Mayor", LocalDate.of(1990, 5, 10), new UnemploymentPeriod(LocalDate.now()));
		Fighter youngest = new Fighter("Joven", LocalDate.of(2008, 12, 15),
				new UnemploymentPeriod(LocalDate.now()));
		Fighter middle = new Fighter("Mediano", LocalDate.of(2000, 3, 20),
				new UnemploymentPeriod(LocalDate.of(2026, 2, 24)));

		List<Fighter> fighters = List.of(older, youngest, middle);
		LocalDate referenceDate = LocalDate.of(2026, 10, 20);

		Optional<Fighter> result = discountService.findYoungestFighter(fighters, referenceDate);

		assertTrue(result.isPresent());
		assertEquals(youngest.name(), result.get().name());
	}

}
