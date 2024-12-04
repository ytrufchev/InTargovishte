package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Photo;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Poster;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotoTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Photo photo = new Photo();

        // Assert
        assertThat(photo).isNotNull();
        assertThat(photo.getPoster()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        Poster poster = new Poster("url-to-poster", "another-url-to-poster", "another-url-to-poster");

        // Act
        Photo photo = new Photo(poster);

        // Assert
        assertThat(photo.getPoster()).isEqualTo(poster);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Photo photo = new Photo();
        Poster poster = new Poster("another-url-to-poster", "another-url-to-poster", "another-url-to-poster");

        // Act
        photo.setPoster(poster);

        // Assert
        assertThat(photo.getPoster()).isEqualTo(poster);
    }

    @Test
    public void testToString() {
        // Arrange
        Poster poster = new Poster("url-for-poster", "another-url-to-poster", "another-url-to-poster");
        Photo photo = new Photo(poster);

        // Act
        String result = photo.toString();

        // Assert
        assertThat(result).isEqualTo("Photo(poster=Poster(large=url-for-poster, medium=another-url-to-poster, small=another-url-to-poster))");
    }
}
