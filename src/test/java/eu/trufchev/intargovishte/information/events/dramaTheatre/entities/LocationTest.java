package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Location location = new Location();

        // Assert
        assertThat(location).isNotNull();
        assertThat(location.getAddress()).isNull();
        assertThat(location.getCityName()).isNull();
        assertThat(location.getCountryCode()).isNull();
        assertThat(location.getCountryName()).isNull();
        assertThat(location.getLat()).isEqualTo(0.0);
        assertThat(location.getLng()).isEqualTo(0.0);
        assertThat(location.getPlaceName()).isNull();
        assertThat(location.getPostCode()).isNull();
        assertThat(location.getTimezone()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String address = "123 Main Street";
        String cityName = "Targovishte";
        String countryCode = "BG";
        String countryName = "Bulgaria";
        double lat = 42.0;
        double lng = 25.0;
        String placeName = "Drama Theatre";
        String postCode = "7700";
        String timezone = "Europe/Sofia";

        // Act
        Location location = new Location(address, cityName, countryCode, countryName, lat, lng, placeName, postCode, timezone);

        // Assert
        assertThat(location.getAddress()).isEqualTo(address);
        assertThat(location.getCityName()).isEqualTo(cityName);
        assertThat(location.getCountryCode()).isEqualTo(countryCode);
        assertThat(location.getCountryName()).isEqualTo(countryName);
        assertThat(location.getLat()).isEqualTo(lat);
        assertThat(location.getLng()).isEqualTo(lng);
        assertThat(location.getPlaceName()).isEqualTo(placeName);
        assertThat(location.getPostCode()).isEqualTo(postCode);
        assertThat(location.getTimezone()).isEqualTo(timezone);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Location location = new Location();
        String address = "456 Elm Street";
        String cityName = "Sofia";
        String countryCode = "BG";
        String countryName = "Bulgaria";
        double lat = 43.0;
        double lng = 26.0;
        String placeName = "Opera House";
        String postCode = "1000";
        String timezone = "Europe/Sofia";

        // Act
        location.setAddress(address);
        location.setCityName(cityName);
        location.setCountryCode(countryCode);
        location.setCountryName(countryName);
        location.setLat(lat);
        location.setLng(lng);
        location.setPlaceName(placeName);
        location.setPostCode(postCode);
        location.setTimezone(timezone);

        // Assert
        assertThat(location.getAddress()).isEqualTo(address);
        assertThat(location.getCityName()).isEqualTo(cityName);
        assertThat(location.getCountryCode()).isEqualTo(countryCode);
        assertThat(location.getCountryName()).isEqualTo(countryName);
        assertThat(location.getLat()).isEqualTo(lat);
        assertThat(location.getLng()).isEqualTo(lng);
        assertThat(location.getPlaceName()).isEqualTo(placeName);
        assertThat(location.getPostCode()).isEqualTo(postCode);
        assertThat(location.getTimezone()).isEqualTo(timezone);
    }

    @Test
    public void testToString() {
        // Arrange
        Location location = new Location(
                "123 Main Street",
                "Targovishte",
                "BG",
                "Bulgaria",
                42.0,
                25.0,
                "Drama Theatre",
                "7700",
                "Europe/Sofia"
        );

        // Act
        String result = location.toString();

        // Assert
        assertThat(result).isEqualTo("Location(address=123 Main Street, cityName=Targovishte, countryCode=BG, countryName=Bulgaria, lat=42.0, lng=25.0, placeName=Drama Theatre, postCode=7700, timezone=Europe/Sofia)");
    }
}
