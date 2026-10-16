package com.calso.robot.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CleaningProductTest {

	@Test
	void testGivenWaterWhenCallingToStringThenReturnsAgua() {
		assertEquals("agua", CleaningProduct.WATER.toString());
	}

	@Test
	void testGivenSoapWhenCallingToStringThenReturnsJabon() {
		assertEquals("jabón", CleaningProduct.SOAP.toString());
	}

	@Test
	void testGivenWaxWhenCallingToStringThenReturnsCera() {
		assertEquals("cera", CleaningProduct.WAX.toString());
	}

	@Test
	void testGivenGlassCleanerWhenCallingToStringThenReturnsLimpiacristales() {
		assertEquals("limpiacristales", CleaningProduct.GLASS_CLEANER.toString());
	}
}