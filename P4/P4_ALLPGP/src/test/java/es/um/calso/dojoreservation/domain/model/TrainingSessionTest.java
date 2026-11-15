package es.um.calso.dojoreservation.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TrainingSessionTest {
    @Test
    void testGivenProgrammedSessionWhenReadThenIdentityMaximumAndLevelArePreserved() {
        TrainingSession session = new TrainingSession(" SES-001 ", 60, TrainingLevel.BEGINNER);
        assertEquals("SES-001", session.id());
        assertEquals(60, session.maximumDurationMinutes());
        assertEquals(TrainingLevel.BEGINNER, session.level());
    }

    @Test
    void testGivenNonPositiveMaximumWhenConstructSessionThenReject() {
        assertThrows(IllegalArgumentException.class,
            () -> new TrainingSession("SES-001", 0, TrainingLevel.BEGINNER));
    }

    @Test
    void testGivenNoLevelWhenConstructSessionThenReject() {
        assertThrows(NullPointerException.class,
            () -> new TrainingSession("SES-001", 60, null));
    }
}
