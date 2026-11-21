package es.um.calso.dojoreservation.acceptance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.um.calso.dojoreservation.application.RegisterProposalUseCase;
import es.um.calso.dojoreservation.application.RegistrationResult;
import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryDojoRepository;
import es.um.calso.dojoreservation.infrastructure.persistence.InMemoryProposalRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Hu01Steps {

	private final DojoRepository dojoRepository = new InMemoryDojoRepository();
	private final ProposalRepository proposalRepository = new InMemoryProposalRepository();
	private final RegisterProposalUseCase useCase = new RegisterProposalUseCase(dojoRepository, proposalRepository);
	private String dojoId;
	RegistrationResult result;

	@Given("dojo {string} has the scheduled session {string}")
	public void dojo_has_the_scheduled_session(String dojoId, String sessionId) {
		Dojo dojo = dojoRepository.findById(dojoId).orElseThrow();
		dojo.findSessionById(sessionId).orElseThrow();
		this.dojoId = dojoId;
	}

	@When("the sensei registers proposal {string} of {int} minutes for session {string}")
	public void the_sensei_registers_proposal_of_minutes_for_session(String proposalId, int durationMinutes,
			String sessionId) {
		result = useCase.register(dojoId, sessionId, proposalId, durationMinutes);
	}

	@Then("the proposal is accepted")
	public void the_proposal_is_accepted() {
		assertTrue(result.accepted());
	}

	@Then("proposal {string} can be retrieved with {int} minutes for dojo {string} and session {string}")
	public void proposal_can_be_retrieved_with_minutes_for_dojo_and_session(String proposalId, int durationMinutes,
			String dojoId, String sessionId) {
		TrainingProposal proposal = proposalRepository.findById(proposalId).orElseThrow();

		assertEquals(proposalId, proposal.proposalId());
		assertEquals(durationMinutes, proposal.durationMinutes());
		assertEquals(dojoId, proposal.dojoId());
		assertEquals(sessionId, proposal.sessionId());
	}

}
