package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Poster;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PosterTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Poster poster = new Poster();

        // Assert
        assertThat(poster).isNotNull();
        assertThat(poster.getLarge()).isNull();
        assertThat(poster.getMedium()).isNull();
        assertThat(poster.getSmall()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String largePoster = "large.jpg";
        String mediumPoster = "medium.jpg";
        String smallPoster = "small.jpg";

        // Act
        Poster poster = new Poster(largePoster, mediumPoster, smallPoster);

        // Assert
        assertThat(poster.getLarge()).isEqualTo(largePoster);
        assertThat(poster.getMedium()).isEqualTo(mediumPoster);
        assertThat(poster.getSmall()).isEqualTo(smallPoster);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Poster poster = new Poster();

        // Act
        poster.setLarge("large2.jpg");
        poster.setMedium("medium2.jpg");
        poster.setSmall("small2.jpg");

        // Assert
        assertThat(poster.getLarge()).isEqualTo("large2.jpg");
        assertThat(poster.getMedium()).isEqualTo("medium2.jpg");
        assertThat(poster.getSmall()).isEqualTo("small2.jpg");
    }

    @Test
    public void testToString() {
        // Arrange
        Poster poster = new Poster("large3.jpg", "medium3.jpg", "small3.jpg");

        // Act
        String result = poster.toString();

        // Assert
        assertThat(result).isEqualTo("Poster(large=large3.jpg, medium=medium3.jpg, small=small3.jpg)");
    }
}
