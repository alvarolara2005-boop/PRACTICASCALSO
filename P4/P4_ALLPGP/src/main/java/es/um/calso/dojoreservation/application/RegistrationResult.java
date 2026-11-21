package es.um.calso.dojoreservation.application;

import java.util.Optional;

import es.um.calso.dojoreservation.domain.model.RejectionReason;

public record RegistrationResult(boolean accepted, Optional<RejectionReason> rejectionReason) {
}
