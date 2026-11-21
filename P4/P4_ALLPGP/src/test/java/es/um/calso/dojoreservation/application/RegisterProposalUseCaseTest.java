package es.um.calso.dojoreservation.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.model.TrainingSession;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryDojoRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryProposalRepository;

class RegisterProposalUseCaseTest {

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenAccepted() {
		DojoRepository dojoRepository = new InMemoryDojoRepository();
		ProposalRepository proposalRepository = new InMemoryProposalRepository();

		RegisterProposalUseCase useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);

		Dojo dojo = dojoRepository.findById("DOJO-01").orElseThrow();
		TrainingSession session = dojo.findSessionById("SES-001").orElseThrow();

		assertEquals(60, session.maximumDurationMinutes());
		assertTrue(useCase.register("DOJO-01", "SES-001", "PROP-001", 45));
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenStoresValues() {
		DojoRepository dojoRepository = new InMemoryDojoRepository();
		ProposalRepository proposalRepository = new InMemoryProposalRepository();

		RegisterProposalUseCase useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);

		useCase.register("DOJO-01", "SES-001", "PROP-001", 45);

		TrainingProposal proposal = proposalRepository.findById("PROP-001").orElseThrow();

		assertEquals("DOJO-01", proposal.dojoId());
		assertEquals("SES-001", proposal.sessionId());
		assertEquals("PROP-001", proposal.proposalId());
		assertEquals(45, proposal.durationMinutes());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister0MinuteProposalThenRejectedAndNotStored() {
		DojoRepository dojoRepository = new InMemoryDojoRepository();
		ProposalRepository proposalRepository = new InMemoryProposalRepository();

		RegisterProposalUseCase useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);

		assertFalse(useCase.register("DOJO-01", "SES-001", "PROP-003", 0));
		assertTrue(proposalRepository.findById("PROP-003").isEmpty());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister75MinuteProposalThenRejectedAndNotStored() {
		DojoRepository dojoRepository = new InMemoryDojoRepository();
		ProposalRepository proposalRepository = new InMemoryProposalRepository();

		RegisterProposalUseCase useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);

		assertFalse(useCase.register("DOJO-01", "SES-001", "PROP-002", 75));
		assertTrue(proposalRepository.findById("PROP-002").isEmpty());
	}

}
