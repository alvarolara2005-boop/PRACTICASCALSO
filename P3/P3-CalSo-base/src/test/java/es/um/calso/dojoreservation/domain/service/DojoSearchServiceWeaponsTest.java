package es.um.calso.dojoreservation.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;
import es.um.calso.dojoreservation.domain.model.Weapon;

class DojoSearchServiceWeaponsTest {
	private final DojoSearchService service = new DojoSearchService();

	@Test
	void testGivenNullAvailableWhenCheckWeaponsThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.containsAllRequiredWeapons(null, Set.of()));
		assertEquals("Available weapons are required", ex.getMessage());
	}

	@Test
	void testGivenNullRequiredWhenCheckWeaponsThenNullPointerWithMessage() {
		NullPointerException ex = assertThrows(NullPointerException.class,
				() -> service.containsAllRequiredWeapons(Set.of(), null));
		assertEquals("Required weapons are required", ex.getMessage());
	}

	@Test
	void testGivenEmptyRequiredWhenCheckWeaponsThenTrue() {
		assertTrue(service.containsAllRequiredWeapons(Set.of(new Weapon("Katana")), Set.of()));
	}

	@Test
	void testGivenEmptyAvailableAndNonEmptyRequiredWhenCheckWeaponsThenFalse() {
		assertFalse(service.containsAllRequiredWeapons(Set.of(), Set.of(new Weapon("Katana"))));
	}

	@Test
	void testGivenAllWeaponsAvailableWhenCheckWeaponsThenTrue() {
		assertTrue(service.containsAllRequiredWeapons(Set.of(new Weapon("Katana"), new Weapon("Bokken")),
				Set.of(new Weapon("Katana"))));
	}
}