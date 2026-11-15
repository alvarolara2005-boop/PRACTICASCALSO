package es.um.calso.dojoreservation.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import es.um.calso.dojoreservation.domain.model.Dojo;
import es.um.calso.dojoreservation.domain.model.Money;
import es.um.calso.dojoreservation.domain.model.Weapon;
import es.um.calso.dojoreservation.domain.port.DojoRepository;

class InMemoryDojoRepositoryTest {

    @Test
    void testGivenDefaultCatalogWhenFindByIdThenDojoAndAvailableWeaponsAreReturned() {
        DojoRepository repository = new InMemoryDojoRepository();
        Dojo dojo = repository.findById("DOJO-01").orElseThrow();
        assertEquals("DOJO-01", dojo.id());
        assertEquals(Set.of(new Weapon("BO"), new Weapon("KATANA")), dojo.weapons());
        var session = dojo.findSessionById("SES-001").orElseThrow();
        assertEquals(60, session.maximumDurationMinutes());
        assertEquals(es.um.calso.dojoreservation.domain.model.TrainingLevel.BEGINNER, session.level());
    }

    @Test
    void testGivenUnknownIdentifierWhenFindByIdThenEmpty() {
        DojoRepository repository = new InMemoryDojoRepository();
        assertTrue(repository.findById("DOJO-404").isEmpty());
    }

    @Test
    void testGivenCatalogWhenListThenOriginalSelectionOrderIsPreserved() {
        DojoRepository repository = new InMemoryDojoRepository();
        assertEquals(List.of("DOJO-01", "DOJO-02"),
            repository.findAllInSelectionOrder().stream().map(Dojo::id).toList());
    }

    @Test
    void testGivenExternalCatalogWhenConstructThenInputIsDefensivelyCopied() {
        Dojo original = new Dojo("D-TEST", "Testing", Money.euros("0"), Set.of());
        var input = new java.util.ArrayList<>(List.of(original));
        DojoRepository repository = new InMemoryDojoRepository(input);
        input.clear();
        assertEquals(1, repository.findAllInSelectionOrder().size());
    }
}
