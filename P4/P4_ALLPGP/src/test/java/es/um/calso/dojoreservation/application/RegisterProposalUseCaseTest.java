package es.um.calso.dojoreservation.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.RejectionReason;
import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.model.TrainingSession;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryDojoRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryProposalRepository;

class RegisterProposalUseCaseTest {

	DojoRepository dojoRepository;
	ProposalRepository proposalRepository;
	RegisterProposalUseCase useCase;

	@BeforeEach
	void initialise() {
		dojoRepository = new InMemoryDojoRepository();
		proposalRepository = new InMemoryProposalRepository();

		useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenAccepted() {
		Dojo dojo = dojoRepository.findById("DOJO-01").orElseThrow();
		TrainingSession session = dojo.findSessionById("SES-001").orElseThrow();

		assertEquals(60, session.maximumDurationMinutes());
		assertTrue(useCase.register("DOJO-01", "SES-001", "PROP-001", 45).accepted());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenStoresValues() {
		useCase.register("DOJO-01", "SES-001", "PROP-001", 45);

		TrainingProposal proposal = proposalRepository.findById("PROP-001").orElseThrow();

		assertEquals("DOJO-01", proposal.dojoId());
		assertEquals("SES-001", proposal.sessionId());
		assertEquals("PROP-001", proposal.proposalId());
		assertEquals(45, proposal.durationMinutes());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister0MinuteProposalThenRejectedAndNotStored() {
		assertFalse(useCase.register("DOJO-01", "SES-001", "PROP-003", 0).accepted());
		assertTrue(proposalRepository.findById("PROP-003").isEmpty());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister75MinuteProposalThenRejectedAndNotStored() {
		assertFalse(useCase.register("DOJO-01", "SES-001", "PROP-002", 75).accepted());
		assertTrue(proposalRepository.findById("PROP-002").isEmpty());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister61MinuteProposalThenRejectedAndNotStored() {
		assertFalse(useCase.register("DOJO-01", "SES-001", "PROP-007", 61).accepted());
		assertTrue(proposalRepository.findById("PROP-007").isEmpty());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister75MinuteProposalThenRejectedAndReasonProposalExceedsAvailableDuration() {
		RegistrationResult result = useCase.register("DOJO-01", "SES-001", "PROP-002", 75);

		assertFalse(result.accepted());
		assertEquals(RejectionReason.PROPOSAL_EXCEEDS_AVAILABLE_DURATION, result.rejectionReason().orElseThrow());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister61MinuteProposalThenRejectedAndReasonProposalExceedsAvailableDuration() {
		RegistrationResult result = useCase.register("DOJO-01", "SES-001", "PROP-007", 61);

		assertFalse(result.accepted());
		assertEquals(RejectionReason.PROPOSAL_EXCEEDS_AVAILABLE_DURATION, result.rejectionReason().orElseThrow());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister0MinuteProposalThenRejectedAndReasonInvalidProposalDuration() {
		RegistrationResult result = useCase.register("DOJO-01", "SES-001", "PROP-003", 0);

		assertFalse(result.accepted());
		assertEquals(RejectionReason.INVALID_PROPOSAL_DURATION, result.rejectionReason().orElseThrow());
	}

	@Test
	void testGivenScheduledSessionOf60MinutesWhenRegister45MinuteProposalThenResultIsAcceptedWithoutRejectionReason() {
		RegistrationResult result = useCase.register("DOJO-01", "SES-001", "PROP-001", 45);

		assertTrue(result.accepted());
		assertTrue(result.rejectionReason().isEmpty());
		assertTrue(proposalRepository.findById("PROP-001").isPresent());
	}

}
