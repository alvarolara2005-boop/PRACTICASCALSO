package es.um.calso.dojoreservation.application;

import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;

public class RegisterProposalUseCase {
	private final ProposalRepository proposalRepository;

	public RegisterProposalUseCase(DojoRepository dojoRepository, ProposalRepository proposalRepository) {
		this.proposalRepository = proposalRepository;
	}

	public boolean register(String dojoId, String sessionId, String proposalId, int durationMinutes) {
		if (durationMinutes > 0) {
			proposalRepository.save(new TrainingProposal(dojoId, sessionId, proposalId, durationMinutes));

			return true;
		}

		return false;
	}
}
