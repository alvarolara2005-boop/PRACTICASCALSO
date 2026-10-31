package es.um.calso.dojoreservation.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import es.um.calso.dojoreservation.domain.model.*;

class DojoSearchServiceFindFirstTest {
	private final DojoSearchService service = new DojoSearchService();

	private static final LocalDate DATE = LocalDate.of(2026, 11, 20);
	private static final Weapon KATANA = new Weapon("Katana");
	private static final Weapon BOKKEN = new Weapon("Bokken");
	private static final Dojo D1 = new Dojo("D-001", "Norte", Money.euros("40"), Set.of(KATANA, BOKKEN));
	private static final Dojo D2 = new Dojo("D-002", "Centro", Money.euros("50"), Set.of(KATANA));

	@Test
	void testGivenNullDojosWhenFindFirstThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.findFirstAvailableDojo(null, Set.of(), List.of(), DATE));
		assertEquals("Dojos are required", ex.getMessage());
	}

	@Test
	void testGivenNullRequiredWeaponsWhenFindFirstThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.findFirstAvailableDojo(List.of(), null, List.of(), DATE));
		assertEquals("Required weapons are required", ex.getMessage());
	}

	@Test
	void testGivenNullReservationsWhenFindFirstThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.findFirstAvailableDojo(List.of(), Set.of(), null, DATE));
		assertEquals("Reservations are required", ex.getMessage());
	}

	@Test
	void testGivenNullTrainingDateWhenFindFirstThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.findFirstAvailableDojo(List.of(), Set.of(), List.of(), null));
		assertEquals("Training date is required", ex.getMessage());
	}

	@Test
	void testGivenEmptyDojosWhenFindFirstThenEmpty() {
		assertTrue(service.findFirstAvailableDojo(List.of(), Set.of(KATANA), List.of(), DATE).isEmpty());
	}

	@Test
	void testGivenIncompatibleDojoWhenFindFirstThenEmpty() {
		assertTrue(service.findFirstAvailableDojo(List.of(D2), Set.of(BOKKEN), List.of(), DATE).isEmpty());
	}

	@Test
	void testGivenCompatibleDojoAndNoReservationsWhenFindFirstThenDojoReturned() {
		assertEquals(D1, service.findFirstAvailableDojo(List.of(D1), Set.of(KATANA), List.of(), DATE).orElseThrow());
	}

	// Cobertura de condiciones: A=false (reserva de otro dojo). B NO se evalúa
	// (cortocircuito).
	@Test
	void testGivenReservationForOtherDojoWhenFindFirstThenDojoReturned() {
		Reservation other = new Reservation("D-999", DATE);
		assertEquals(D1,
				service.findFirstAvailableDojo(List.of(D1), Set.of(KATANA), List.of(other), DATE).orElseThrow());
	}

	// Cobertura de condiciones: A=true y B=true (misma dojo, distinta fecha) →
	// ocupado, se descarta.
	@Test
	void testGivenReservationForSameDojoOtherDateWhenFindFirstThenDojoSkipped() {
		Reservation otherDate = new Reservation("D-001", LocalDate.of(2027, 11, 20));
		assertTrue(service.findFirstAvailableDojo(List.of(D1), Set.of(KATANA), List.of(otherDate), DATE).isEmpty());
	}

	// Cobertura de condiciones: A=true y B=false (misma dojo, misma fecha) → NO
	// ocupado.
	@Test
	void testGivenReservationForSameDojoSameDateWhenFindFirstThenDojoReturned() {
		Reservation sameDate = new Reservation("D-001", DATE);
		assertEquals(D1,
				service.findFirstAvailableDojo(List.of(D1), Set.of(KATANA), List.of(sameDate), DATE).orElseThrow());
	}

	// Camino C11: primer dojo ocupado, segundo incompatible → vacío.
	@Test
	void testGivenFirstDojoOccupiedAndSecondIncompatibleWhenFindFirstThenEmpty() {
		Reservation occupied = new Reservation("D-001", LocalDate.of(2027, 1, 1));
		assertTrue(service.findFirstAvailableDojo(List.of(D1, D2), Set.of(BOKKEN), List.of(occupied), DATE).isEmpty());
	}
}