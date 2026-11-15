package es.um.calso.dojoreservation.domain.model;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Raiz del agregado Dojo: inventario de armas y sesiones ya programadas.
 * La programacion de sesiones no forma parte de P4.
 */
public record Dojo(String id, String name, Money basePrice, Set<Weapon> weapons,
                   Set<TrainingSession> sessions) {

    /** Constructor compatible con las pruebas y los clientes de P3. */
    public Dojo(String id, String name, Money basePrice, Set<Weapon> weapons) {
        this(id, name, basePrice, weapons, Set.of());
    }

    public Dojo {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Dojo id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Dojo name is required");
        Objects.requireNonNull(basePrice, "Base price is required");
        Objects.requireNonNull(weapons, "Inventory is required");
        Objects.requireNonNull(sessions, "Scheduled sessions are required");
        if (weapons.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Null weapon");
        if (sessions.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Null session");
        if (sessions.stream().map(TrainingSession::id).distinct().count() != sessions.size()) {
            throw new IllegalArgumentException("Duplicate session id within dojo");
        }
        id = id.trim();
        name = name.trim();
        weapons = Set.copyOf(weapons);
        sessions = Set.copyOf(sessions);
    }

    public Optional<TrainingSession> findSessionById(String sessionId) {
        return sessions.stream().filter(s -> s.id().equals(sessionId)).findFirst();
    }
}
