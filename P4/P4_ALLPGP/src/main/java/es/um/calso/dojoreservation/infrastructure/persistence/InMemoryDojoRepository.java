package es.um.calso.dojoreservation.infrastructure.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.Money;
import es.um.calso.dojoreservation.domain.model.Weapon;
import es.um.calso.dojoreservation.domain.model.TrainingSession;
import es.um.calso.dojoreservation.domain.model.TrainingLevel;
import es.um.calso.dojoreservation.domain.port.DojoRepository;

/**
 * Reutiliza el puerto de catálogo de dojos de P3.
 * Contiene sesiones previamente programadas en los agregados Dojo;
 * no permite crearlas ni gestiona propuestas de entrenamiento.
 */
public final class InMemoryDojoRepository implements DojoRepository {
    private final List<Dojo> dojos;

    public InMemoryDojoRepository() {
        this(List.of(
            new Dojo("DOJO-01", "Dojo Central", Money.euros("40"),
                Set.of(new Weapon("BO"), new Weapon("KATANA")),
                Set.of(new TrainingSession("SES-001", 60, TrainingLevel.BEGINNER))),
            new Dojo("DOJO-02", "Dojo Sur", Money.euros("35"),
                Set.of(new Weapon("BO")))
        ));
    }

    public InMemoryDojoRepository(List<Dojo> dojos) {
        this.dojos = List.copyOf(Objects.requireNonNull(dojos, "Dojos required"));
    }

    @Override
    public List<Dojo> findAllInSelectionOrder() {
        return dojos;
    }

    @Override
    public Optional<Dojo> findById(String dojoId) {
        return dojos.stream().filter(dojo -> dojo.id().equals(dojoId)).findFirst();
    }
}
