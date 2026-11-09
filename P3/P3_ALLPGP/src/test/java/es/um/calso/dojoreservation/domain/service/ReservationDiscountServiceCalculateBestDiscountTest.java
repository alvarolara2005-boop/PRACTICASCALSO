package es.um.calso.dojoreservation.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.um.calso.dojoreservation.domain.model.Discount;
import es.um.calso.dojoreservation.domain.model.Fighter;
import es.um.calso.dojoreservation.domain.model.UnemploymentPeriod;

class ReservationDiscountServiceCalculateBestDiscountTest {

	private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 11, 9);

	private final ReservationDiscountService service = new ReservationDiscountService();
	
	private static Fighter adult() {
		return new Fighter("Ren", LocalDate.of(1998, 6, 1), null);
	}

	private static Fighter unemployedSince(LocalDate startDate) {
		return new Fighter("Akira", LocalDate.of(2000, 1, 1), new UnemploymentPeriod(startDate));
	}

	// C1: 1-2-3-23
	@Test
	void testGivenNullFightersWhenCalculateBestDiscountThenThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> service.calculateBestDiscount(null, TRAINING_DATE));
	}

	// C2: 1-2-4-5-23
	@Test
	void testGivenNullTrainingDateWhenCalculateBestDiscountThenThrowsNullPointerException() {
		List<Fighter> fighters = List.of(adult());
		
		assertThrows(NullPointerException.class, () -> service.calculateBestDiscount(fighters, null));
	}

	// C3: 1-2-4-6-7-10-22-23 lista vacía
	@Test
	void testGivenEmptyFightersWhenCalculateBestDiscountThenZeroPercent() {
		Discount result = service.calculateBestDiscount(List.of(), TRAINING_DATE);
		
		assertEquals(new Discount(0), result);
	}

	// C4: 1-2-4-6-7-8-10-11-13-10-22-23 un adulto sin paro
	@Test
	void testGivenAdultWithoutUnemploymentWhenCalculateBestDiscountThenZeroPercent() {
		Discount result = service.calculateBestDiscount(List.of(adult()), TRAINING_DATE);
		
		assertEquals(new Discount(0), result);
	}

	// C5: 1-2-4-6-7-8-9-10-11-13-10-22-23 17 años (Nora, 2009-11-20, cumple 18 en 2027-11-20)
	@Test
	void testGivenSeventeenYearOldFighterWhenCalculateBestDiscountThenFifteenPercent() {
		Fighter nora = new Fighter("Nora", LocalDate.of(2009, 11, 20), null);
		Discount result = service.calculateBestDiscount(List.of(nora), TRAINING_DATE);
		
		assertEquals(new Discount(15), result);
	}

	// C7: 1-2-4-6-7-8-10-11-13-14-16-18-20-10-22-23 0 años completos de paro
	@Test
	void testGivenZeroCompleteYearsUnemploymentWhenCalculateBestDiscountThenZeroPercent() {
		Fighter akira = unemployedSince(LocalDate.of(2026, 11, 2));
		Discount result = service.calculateBestDiscount(List.of(akira), TRAINING_DATE);
		
		assertEquals(new Discount(0), result);
	}

	// C8: 1-2-4-6-7-8-10-11-13-14-15-20-21-10-22-23 3 años completos (límite exacto)
	@Test
	void testGivenThreeYearsUnemploymentWhenCalculateBestDiscountThenThirtyPercent() {
		Fighter akira = unemployedSince(LocalDate.of(2023, 11, 9));
		Discount result = service.calculateBestDiscount(List.of(akira), TRAINING_DATE);
		
		assertEquals(new Discount(30), result);
	}

	// C9: 1-2-4-6-7-8-10-11-13-14-16-17-20-21-10-22-23 2 años completos (límite exacto) -> detecta D-3 (completeYears > 2)
	@Test
	void testGivenTwoYearsUnemploymentWhenCalculateBestDiscountThenTwentyPercent() {
		Fighter akira = unemployedSince(LocalDate.of(2024, 11, 9));
		Discount result = service.calculateBestDiscount(List.of(akira), TRAINING_DATE);
		
		assertEquals(new Discount(20), result);
	}

	// C10: 1-2-4-6-7-8-10-11-13-14-16-18-19-20-21-10-22-23 1 año completo (límite exacto)
	@Test
	void testGivenOneYearUnemploymentWhenCalculateBestDiscountThenTenPercent() {
		Fighter akira = unemployedSince(LocalDate.of(2025, 11, 9));
		Discount result = service.calculateBestDiscount(List.of(akira), TRAINING_DATE);
		
		assertEquals(new Discount(10), result);
	}

	// C11: 1-2-4-6-7-8-10-11-13-10-11-13-10-22-23 dos luchadores sin paro (dos iteraciones)
	@Test
	void testGivenTwoAdultsWithoutUnemploymentWhenCalculateBestDiscountThenZeroPercent() {
		Fighter ren = adult();
		Fighter akira = new Fighter("Akira", LocalDate.of(2000, 1, 1), null);
		Discount result = service.calculateBestDiscount(List.of(ren, akira), TRAINING_DATE);
		
		assertEquals(new Discount(0), result);
	}

	// C12: 1-2-4-6-7-8-9-10-11-13-14-16-18-19-20-10-22-23 juventud 15 % y paro 10 % -> solo el mayor
	@Test
	void testGivenSeventeenYearOldWithOneYearUnemploymentWhenCalculateBestDiscountThenFifteenPercent() {
		Fighter nora = new Fighter("Nora", LocalDate.of(2009, 11, 9),
				new UnemploymentPeriod(LocalDate.of(2025, 11, 9)));
		Discount result = service.calculateBestDiscount(List.of(nora), TRAINING_DATE);
		
		assertEquals(new Discount(15), result);
	}

}
