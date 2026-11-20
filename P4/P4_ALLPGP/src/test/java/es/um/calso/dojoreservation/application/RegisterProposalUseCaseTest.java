package es.um.calso.dojoreservation.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.TrainingSession;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryDojoRepository;

class RegisterProposalUseCaseTest {

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenAccepted() {
		DojoRepository repository = new InMemoryDojoRepository();
		RegisterProposalUseCase useCase = new RegisterProposalUseCase(repository);

		Dojo dojo = repository.findById("DOJO-01").orElseThrow();
		TrainingSession session = dojo.findSessionById("SES-001").orElseThrow();

		assertEquals(60, session.maximumDurationMinutes());
		assertTrue(useCase.register("DOJO-01", "SES-001", "PROP-001", 45));
	}

}
