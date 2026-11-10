package es.um.calso.dojoreservation.infrastructure.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// Incorporar las importaciones JUnit 5 y Selenium necesarias para los casos diseñados.
// La configuración del navegador y las esperas se heredan de BrowserE2ESupport.
class ReservationCostE2ETest extends BrowserE2ESupport {

	// Alumno 2: incorporar aquí un método @Test por cada caso de caja negra
	// de cálculo de costes y bonificaciones.

	// Desglose económico sin descuento (EQ-002, 1 arma)

	@Test
	void testGivenTigerTeamNorthDojoTomorrowWhenCalculateCostThenDiscountIsZero() {
		calculateCost("EQ-002", "D-001", "2026-10-21");
		assertEquals("0", textOf("discount"));
	}

	@Test
	void testGivenTigerTeamNorthDojoTomorrowWhenCalculateCostThenFinalPriceIs45() {
		calculateCost("EQ-002", "D-001", "2026-10-21");
		assertEquals("45.00", textOf("final-price"));
	}

	@Test
	void testGivenTigerTeamNorthDojoTomorrowWhenCalculateCostThenWeaponsSupplementIs5() {
		calculateCost("EQ-002", "D-001", "2026-10-21");
		assertEquals("5.00", textOf("weapons-price"));
	}

	// Desglose con 2 armas y mejor bonificación (EQ-001)

	@Test
	void testGivenDragonTeamCentroDojoTomorrowWhenCalculateCostThenWeaponsSupplementIs10() {
		calculateCost("EQ-001", "D-002", "2026-10-21");
		assertEquals("10.00", textOf("weapons-price"));
	}

	@Test
	void testGivenDragonTeamCentroDojoTomorrowWhenCalculateCostThenSubtotalIs60() {
		calculateCost("EQ-001", "D-002", "2026-10-21");
		assertEquals("60.00", textOf("subtotal"));
	}

	@Test
	void testGivenDragonTeamCentroDojoTomorrowWhenCalculateCostThenDiscountIs15() {
		calculateCost("EQ-001", "D-002", "2026-10-21");
		assertEquals("15", textOf("discount"));
	}

	@Test
	void testGivenDragonTeamCentroDojoTomorrowWhenCalculateCostThenFinalPriceIs51() {
		calculateCost("EQ-001", "D-002", "2026-10-21");
		assertEquals("51.00", textOf("final-price"));
	}

	@Test
	void testGivenDragonTeamCentroDojoOn20Nov2027WhenCalculateCostThenDiscountIs30() {
		calculateCost("EQ-001", "D-002", "2027-11-20");
		assertEquals("30", textOf("discount"));
	}

	@Test
	void testGivenDragonTeamCentroDojoOn20Nov2027WhenCalculateCostThenFinalPriceIs42() {
		calculateCost("EQ-001", "D-002", "2027-11-20");
		assertEquals("42.00", textOf("final-price"));
	}

	// Presupuesto de un dojo ocupado (no se comprueba disponibilidad)

	@Test
	void testGivenDragonTeamBookedNorthDojoOn20Nov2026WhenCalculateCostThenFinalPriceIs40() {
		calculateCost("EQ-001", "D-001", "2026-11-20");
		assertEquals("40.00", textOf("final-price"));
	}

	// Límite de edad: Nora (EQ-003) cumple 18 el 2027-11-20

	@Test
	void testGivenAgeBoundaryTeamDayBeforeEighteenthBirthdayWhenCalculateCostThenDiscountIs15() {
		calculateCost("EQ-003", "D-003", "2027-11-19");
		assertEquals("15", textOf("discount"));
	}

	@Test
	void testGivenAgeBoundaryTeamEighteenthBirthdayWhenCalculateCostThenDiscountIsZero() {
		calculateCost("EQ-003", "D-003", "2027-11-20");
		assertEquals("0", textOf("discount"));
	}

	@Test
	void testGivenAgeBoundaryTeamDayBeforeEighteenthBirthdayWhenCalculateCostThenFinalPriceIs34() {
		calculateCost("EQ-003", "D-003", "2027-11-19");
		assertEquals("34.00", textOf("final-price"));
	}

	// Límites de desempleo: Izan (EQ-004) en paro desde 2025-11-20

	@Test
	void testGivenTwoYearTeamDayBeforeFirstAnniversaryWhenCalculateCostThenDiscountIsZero() {
		calculateCost("EQ-004", "D-003", "2026-11-19");
		assertEquals("0", textOf("discount"));
	}

	@Test
	void testGivenTwoYearTeamFirstAnniversaryWhenCalculateCostThenDiscountIs10() {
		calculateCost("EQ-004", "D-003", "2026-11-20");
		assertEquals("10", textOf("discount"));
	}

	@Test
	void testGivenTwoYearTeamDayBeforeSecondAnniversaryWhenCalculateCostThenDiscountIs10() {
		calculateCost("EQ-004", "D-003", "2027-11-19");
		assertEquals("10", textOf("discount"));
	}

	@Test
	void testGivenTwoYearTeamSecondAnniversaryWhenCalculateCostThenDiscountIs20() {
		calculateCost("EQ-004", "D-003", "2027-11-20");
		assertEquals("20", textOf("discount"));
	}

	// Límites de desempleo: Lea (EQ-005) en paro desde 2024-11-20

	@Test
	void testGivenThreeYearTeamDayBeforeThirdAnniversaryWhenCalculateCostThenDiscountIs20() {
		calculateCost("EQ-005", "D-003", "2027-11-19");
		assertEquals("20", textOf("discount"));
	}

	@Test
	void testGivenThreeYearTeamThirdAnniversaryWhenCalculateCostThenDiscountIs30() {
		calculateCost("EQ-005", "D-003", "2027-11-20");
		assertEquals("30", textOf("discount"));
	}

	// Validación de la fecha (reloj fijo 2026-10-20)

	@Test
	void testGivenTodayWhenCalculateCostThenTrainingDateMustBeFutureError() {
		calculateCost("EQ-001", "D-001", "2026-10-20");
		assertEquals("Training date must be in the future", textOf("error"));
	}

	@Test
	void testGivenYesterdayWhenCalculateCostThenTrainingDateMustBeFutureError() {
		calculateCost("EQ-001", "D-001", "2026-10-19");
		assertEquals("Training date must be in the future", textOf("error"));
	}

	// Validación de identificadores (URL manipulada)

	@Test
	void testGivenUnknownTeamWhenCalculateCostThenTeamDoesNotExistError() {
		open("/coste?teamId=EQ-999&dojoId=D-001&trainingDate=2026-11-20");
		assertEquals("Team does not exist", textOf("error"));
	}

	@Test
	void testGivenUnknownDojoWhenCalculateCostThenDojoDoesNotExistError() {
		open("/coste?teamId=EQ-001&dojoId=D-999&trainingDate=2026-11-20");
		assertEquals("Dojo does not exist", textOf("error"));
	}

	@Test
	void testGivenBlankTeamIdWhenCalculateCostThenTeamIdRequiredError() {
		open("/coste?teamId=%20&dojoId=D-001&trainingDate=2026-11-20");
		assertEquals("Team id is required", textOf("error"));
	}

	@Test
	void testGivenBlankDojoIdWhenCalculateCostThenDojoIdRequiredError() {
		open("/coste?teamId=EQ-001&dojoId=%20&trainingDate=2026-11-20");
		assertEquals("Dojo id is required", textOf("error"));
	}

	// Formato de fecha inválido: lo rechaza Spring MVC

	@Test
	void testGivenMalformedDateWhenCalculateCostThenSpringRejectsWithBadRequest() {
		open("/coste?teamId=EQ-001&dojoId=D-001&trainingDate=20%2F11%2F2026");
		assertTrue(browser.getPageSource().contains("400"));
	}

}
