package es.um.calso.dojoreservation.infrastructure.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import es.um.calso.dojoreservation.domain.model.TrainingProposal;
import es.um.calso.dojoreservation.domain.port.ProposalRepository;

public final class InMemoryProposalRepository implements ProposalRepository {
	private final List<TrainingProposal> proposals;

	public InMemoryProposalRepository() {
		proposals = new LinkedList<TrainingProposal>();
	}

	@Override
	public void save(TrainingProposal proposal) {
		proposals.add(proposal);
	}

	@Override
	public Optional<TrainingProposal> findById(String proposalId) {
		return proposals.stream().filter(proposal -> proposal.proposalId().equals(proposalId)).findFirst();
	}

}
