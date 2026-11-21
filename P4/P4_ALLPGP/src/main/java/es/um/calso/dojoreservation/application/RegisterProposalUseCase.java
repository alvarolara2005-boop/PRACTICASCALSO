package es.um.calso.dojoreservation.application;

import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.model.TrainingSession;
import es.um.calso.dojoreservation.domain.port.DojoRepository;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;

public class RegisterProposalUseCase {

	private final DojoRepository dojoRepository;
	private final ProposalRepository proposalRepository;

	public RegisterProposalUseCase(DojoRepository dojoRepository, ProposalRepository proposalRepository) {
		this.dojoRepository = dojoRepository;
		this.proposalRepository = proposalRepository;
	}

	public boolean register(String dojoId, String sessionId, String proposalId, int durationMinutes) {
		Dojo dojo = dojoRepository.findById(dojoId).orElseThrow();
		TrainingSession session = dojo.findSessionById(sessionId).orElseThrow();

		if ((durationMinutes > 0) && (durationMinutes <= session.maximumDurationMinutes())) {
			proposalRepository.save(new TrainingProposal(dojoId, sessionId, proposalId, durationMinutes));

			return true;
		}

		return false;
	}

}
