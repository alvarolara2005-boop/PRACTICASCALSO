package es.um.calso.dojoreservation.domain.port;

import java.util.Optional;

import es.um.calso.dojoreservation.domain.model.TrainingProposal;

public interface ProposalRepository {
	void save(TrainingProposal proposal);

	Optional<TrainingProposal> findById(String proposalId);
}
