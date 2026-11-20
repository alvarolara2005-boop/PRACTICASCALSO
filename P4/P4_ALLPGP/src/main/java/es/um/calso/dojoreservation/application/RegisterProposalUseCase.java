package es.um.calso.dojoreservation.application;

import es.um.calso.dojoreservation.domain.port.DojoRepository;

public class RegisterProposalUseCase {
	public RegisterProposalUseCase(DojoRepository repository) {
	}

	public boolean register(String dojoId, String sessionId, String proposalId, int durationMinutes) {
		return true;
	}
}
