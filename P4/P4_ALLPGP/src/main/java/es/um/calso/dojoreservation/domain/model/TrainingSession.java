package es.um.calso.dojoreservation.domain.model;

import java.util.Objects;

/**
 * Datos inmutables de una sesión YA programada por el dojo.
 * No permite crear reservas, asignar fechas ni planificar sesiones.
 * Su pertenencia a un dojo la establece el agregado Dojo.
 */
public record TrainingSession(String id, int maximumDurationMinutes, TrainingLevel level) {
    public TrainingSession {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Session id is required");
        }
        if (maximumDurationMinutes <= 0) {
            throw new IllegalArgumentException("Maximum session duration must be positive");
        }
        Objects.requireNonNull(level, "Training level is required");
        id = id.trim();
    }
}
