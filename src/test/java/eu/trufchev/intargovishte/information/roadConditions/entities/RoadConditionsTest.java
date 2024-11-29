package eu.trufchev.intargovishte.information.roadConditions.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoadConditionsTest {

    @Test
    void testIdGetterAndSetter() {
        // Given
        RoadConditions roadConditions = new RoadConditions();
        long expectedId = 1L;

        // When
        roadConditions.setId(expectedId);
        long actualId = roadConditions.getId();

        // Then
        assertEquals(expectedId, actualId, "The ID should match the set value");
    }

    @Test
    void testEndDateGetterAndSetter() {
        // Given
        RoadConditions roadConditions = new RoadConditions();
        String expectedEndDate = "2024-11-29";

        // When
        roadConditions.setEndDate(expectedEndDate);
        String actualEndDate = roadConditions.getEndDate();

        // Then
        assertEquals(expectedEndDate, actualEndDate, "The endDate should match the set value");
    }

    @Test
    void testDescriptionGetterAndSetter() {
        // Given
        RoadConditions roadConditions = new RoadConditions();
        String expectedDescription = "Clear road";

        // When
        roadConditions.setDescription(expectedDescription);
        String actualDescription = roadConditions.getDescription();

        // Then
        assertEquals(expectedDescription, actualDescription, "The description should match the set value");
    }

    @Test
    void testConstructorWithArguments() {
        // Given
        long expectedId = 1L;
        String expectedEndDate = "2024-11-29";
        String expectedDescription = "Clear road";

        // When
        RoadConditions roadConditions = new RoadConditions(expectedId, expectedEndDate, expectedDescription);

        // Then
        assertEquals(expectedId, roadConditions.getId(), "The ID should match the constructor value");
        assertEquals(expectedEndDate, roadConditions.getEndDate(), "The endDate should match the constructor value");
        assertEquals(expectedDescription, roadConditions.getDescription(), "The description should match the constructor value");
    }

    @Test
    void testNoArgsConstructor() {
        // Given
        RoadConditions roadConditions = new RoadConditions();

        // Then
        assertNotNull(roadConditions, "The no-args constructor should create a non-null object");
    }

    @Test
    void testToString() {
        // Given
        RoadConditions roadConditions = new RoadConditions(1L, "2024-11-29", "Clear road");

        // When
        String toStringResult = roadConditions.toString();

        // Then
        assertTrue(toStringResult.contains("1"), "The toString() method should contain the ID");
        assertTrue(toStringResult.contains("2024-11-29"), "The toString() method should contain the endDate");
        assertTrue(toStringResult.contains("Clear road"), "The toString() method should contain the description");
    }
}
