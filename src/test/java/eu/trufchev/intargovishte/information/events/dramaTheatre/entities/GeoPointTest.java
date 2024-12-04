package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.GeoPoint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class GeoPointTest {

    @Test
    public void testGeoPointConstructorAndGetters() {
        // Arrange
        String type = "Point";
        List<Double> coordinates = Arrays.asList(42.0, 25.0);

        // Act
        GeoPoint geoPoint = new GeoPoint(type, coordinates);

        // Assert
        assertThat(geoPoint.getType()).isEqualTo(type);
        assertThat(geoPoint.getCoordinates()).isEqualTo(coordinates);
    }

    @Test
    public void testSetters() {
        // Arrange
        GeoPoint geoPoint = new GeoPoint();

        // Act
        geoPoint.setType("Polygon");
        geoPoint.setCoordinates(Arrays.asList(43.0, 26.0));

        // Assert
        assertThat(geoPoint.getType()).isEqualTo("Polygon");
        assertThat(geoPoint.getCoordinates()).isEqualTo(Arrays.asList(43.0, 26.0));
    }

    @Test
    public void testToString() {
        // Arrange
        String type = "Point";
        List<Double> coordinates = Arrays.asList(42.0, 25.0);
        GeoPoint geoPoint = new GeoPoint(type, coordinates);

        // Act
        String result = geoPoint.toString();

        // Assert
        String expected = "GeoPoint(type=Point, coordinates=[42.0, 25.0])";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCoordinatesCanBeNull() {
        // Arrange
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setCoordinates(null);

        // Assert
        assertThat(geoPoint.getCoordinates()).isNull();
    }

    @Test
    public void testEmptyCoordinates() {
        // Arrange
        GeoPoint geoPoint = new GeoPoint("Point", Arrays.asList());

        // Assert
        assertThat(geoPoint.getCoordinates()).isEmpty();
    }
}
