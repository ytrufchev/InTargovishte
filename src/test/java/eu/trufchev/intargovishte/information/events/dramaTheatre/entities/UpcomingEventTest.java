package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpcomingEventTest {

    @Test
    public void testUpcomingEventGettersAndSetters() {
        // Arrange
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(43.12);
        coordinates.add(25.42);
        GeoPoint geoPoint = new GeoPoint("Point", coordinates);
        Location location = new Location("123 Main St", "Targovishte", "BG", "Bulgaria", 43.123, 26.456, "Theatre", "7700", "EET");
        UpcomingEvent upcomingEvent = new UpcomingEvent();

        // Act
        upcomingEvent.setEventId("event123");
        upcomingEvent.setGeoPoint(geoPoint);
        upcomingEvent.setLocation(location);

        // Assert
        assertThat(upcomingEvent.getEventId()).isEqualTo("event123");
        assertThat(upcomingEvent.getGeoPoint()).isEqualTo(geoPoint);
        assertThat(upcomingEvent.getLocation()).isEqualTo(location);
    }

    @Test
    public void testUpcomingEventConstructorAndToString() {
        // Arrange
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(43.12);
        coordinates.add(25.42);
        GeoPoint geoPoint = new GeoPoint("Point", coordinates);
        Location location = new Location("123 Main St", "Targovishte", "BG", "Bulgaria", 43.123, 26.456, "Theatre", "7700", "EET");
        UpcomingEvent upcomingEvent = new UpcomingEvent("event123", geoPoint, location);

        // Act & Assert
        assertThat(upcomingEvent.getEventId()).isEqualTo("event123");
        assertThat(upcomingEvent.getGeoPoint()).isEqualTo(geoPoint);
        assertThat(upcomingEvent.getLocation()).isEqualTo(location);

        // Test toString method
        String toStringResult = upcomingEvent.toString();
        assertThat(toStringResult).contains("event123");
        assertThat(toStringResult).contains("GeoPoint");
        assertThat(toStringResult).contains("Location");
    }

    @Test
    public void testUpcomingEventEquality() {
        // Arrange
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(43.12);
        coordinates.add(25.42);
        GeoPoint geoPoint1 = new GeoPoint("point", coordinates);
        Location location1 = new Location("123 Main St", "Targovishte", "BG", "Bulgaria", 43.123, 26.456, "Theatre", "7700", "EET");
        GeoPoint geoPoint2 = new GeoPoint("point", coordinates);
        Location location2 = new Location("456 Another St", "Sofia", "BG", "Bulgaria", 44.000, 27.000, "Opera", "1000", "EET");

        UpcomingEvent event1 = new UpcomingEvent("event123", geoPoint1, location1);
        UpcomingEvent event2 = new UpcomingEvent("event123", geoPoint1, location1);
        UpcomingEvent event3 = new UpcomingEvent("event456", geoPoint2, location2);

        // Act & Assert
        assertThat(event1.toString()).isEqualTo(event2.toString()); // Same values
        assertThat(event1.toString()).isNotEqualTo(event3.toString()); // Different values
    }
}
