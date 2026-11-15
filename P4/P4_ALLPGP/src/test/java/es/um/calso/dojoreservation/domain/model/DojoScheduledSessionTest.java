package es.um.calso.dojoreservation.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DojoScheduledSessionTest {
    @Test
    void testGivenExistingSessionWhenFindWithinDojoAggregateThenReturnItsConditions() {
        Dojo dojo = new Dojo("DOJO-01", "Central", Money.euros("40"),
            Set.of(new Weapon("BO")),
            Set.of(new TrainingSession("SES-001", 60, TrainingLevel.BEGINNER)));
        TrainingSession session = dojo.findSessionById("SES-001").orElseThrow();
        assertEquals(60, session.maximumDurationMinutes());
        assertEquals(TrainingLevel.BEGINNER, session.level());
    }

    @Test
    void testGivenUnknownSessionWhenFindWithinDojoAggregateThenEmpty() {
        Dojo dojo = new Dojo("DOJO-01", "Central", Money.euros("40"),
            Set.of(new Weapon("BO")), Set.of());
        assertTrue(dojo.findSessionById("SES-404").isEmpty());
    }

    @Test
    void testGivenExternalMutableSessionsWhenCreateDojoThenAggregateCopiesThem() {
        var sessions = new java.util.HashSet<>(
            Set.of(new TrainingSession("SES-001", 60, TrainingLevel.BEGINNER)));
        Dojo dojo = new Dojo("DOJO-01", "Central", Money.euros("40"), Set.of(), sessions);
        sessions.clear();
        assertEquals(1, dojo.sessions().size());
        assertThrows(UnsupportedOperationException.class, () -> dojo.sessions().clear());
    }
}
