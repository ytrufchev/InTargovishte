package eu.trufchev.intargovishte.information.vikOutage.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VikOutageTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        VikOutage vikOutage = new VikOutage();

        // Act
        vikOutage.setId(1L);
        vikOutage.setDate("2024-11-29");
        vikOutage.setStartTime("09:00");
        vikOutage.setEndTime("15:00");
        vikOutage.setDescription("Scheduled maintenance in Targovishte.");

        // Assert
        assertEquals(1L, vikOutage.getId());
        assertEquals("2024-11-29", vikOutage.getDate());
        assertEquals("09:00", vikOutage.getStartTime());
        assertEquals("15:00", vikOutage.getEndTime());
        assertEquals("Scheduled maintenance in Targovishte.", vikOutage.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        // Act
        VikOutage vikOutage = new VikOutage(
                2L,
                "2024-11-30",
                "08:00",
                "12:00",
                "Unexpected outage due to repairs."
        );

        // Assert
        assertEquals(2L, vikOutage.getId());
        assertEquals("2024-11-30", vikOutage.getDate());
        assertEquals("08:00", vikOutage.getStartTime());
        assertEquals("12:00", vikOutage.getEndTime());
        assertEquals("Unexpected outage due to repairs.", vikOutage.getDescription());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        VikOutage vikOutage = new VikOutage();

        // Assert
        assertNotNull(vikOutage);
        assertEquals(0L, vikOutage.getId());
        assertNull(vikOutage.getDate());
        assertNull(vikOutage.getStartTime());
        assertNull(vikOutage.getEndTime());
        assertNull(vikOutage.getDescription());
    }

    @Test
    void testToString() {
        // Arrange
        VikOutage vikOutage = new VikOutage(
                3L,
                "2024-12-01",
                "10:00",
                "14:00",
                "Short outage for maintenance."
        );

        // Act
        String result = vikOutage.toString();

        // Assert
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("date=2024-12-01"));
        assertTrue(result.contains("startTime=10:00"));
        assertTrue(result.contains("endTime=14:00"));
        assertTrue(result.contains("description=Short outage for maintenance."));
    }
}
